package my.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;

import my.beans.Blog;
import my.beans.BlogCollection;
import my.beans.ObjTag;
import my.beans.Tag;
import my.beans.User;
import my.service.Annotation;
import my.service.RequestContext;

/**
 * Blog 操作
 * @author zino
 *
 */

@Annotation.PostMethod
public class BlogAction {
	
	/**
	 * 发表博客
	 * @param ctx
	 * @throws IOException
	 */
	public void post(RequestContext ctx) throws IOException {
		User user = ctx.user();
		Blog form = ctx.form(Blog.class);
		String result = checkBlog(form);
		if(result!=null)
			throw ctx.error(result);
		if(user==null || !user.IsBlog()) {
			form.setUser(user==null?0:user.getId());
			form.setStatus(Blog.STATUS_POST);
		}else{
			form.setUser(user.getId());
		}
		form.setDraft(Blog.UNDRAFT);
		form.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		long id = form.save();
		//处理标签
		addtags(ctx, id);
		ctx.output_json("id", id);
	}
	
	/**
	 * 采纳投稿
	 * @param ctx
	 * @throws IOException 
	 */
	@Annotation.UserRequired(role=User.ROLE_MANAGER)
	public void accept(RequestContext ctx) throws IOException {
		long id = ctx.id();
		Blog bean = Blog.INSTANCE.Get(id);
		if(bean==null)
			throw ctx.error("form_empty");
		bean.UpdateField("status", Blog.STATUS_NORMAL);
		ctx.output_json("id", id);
	}
	/**
	 * 存为草稿
	 * @param ctx
	 * @throws IOException
	 */
	@Annotation.UserRequired
	public void draft(RequestContext ctx) throws IOException {
		User user = ctx.user();
		Blog draft = ctx.form(Blog.class);
		String result = checkBlog(draft);
		if(result!=null)
			throw ctx.error(result);
		draft.setUser(user.getId());
		draft.setDraft(Blog.DRAFT);
		draft.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		long id = draft.save();
		addtags(ctx, id);
		ctx.output_json("id", id);
	}
	
	/**
	 * 修改草稿
	 * @param ctx
	 * @throws IOException 
	 */
	@Annotation.UserRequired
	public void update_draft(RequestContext ctx) throws IOException {
		update(ctx, Blog.DRAFT);
	}
	/**
	 * 修改博客
	 * @param ctx
	 * @throws IOException 
	 */
	@Annotation.UserRequired
	public void update_post(RequestContext ctx) throws IOException {
		update(ctx, Blog.UNDRAFT);
	}
	
	/**
	 * 从草稿发表博客
	 * @param ctx
	 * @throws IOException 
	 */
	@Annotation.UserRequired
	public void post_form_draft(RequestContext ctx) throws IOException {
		update(ctx, Blog.UNDRAFT);
	}
	
	/**
	 * 删除博客
	 * @param ctx
	 * @throws IOException
	 */
	@Annotation.UserRequired
	public void delete(RequestContext ctx) throws IOException {
		User user = ctx.user();
		long id = ctx.id();
		Blog blog = Blog.INSTANCE.Get(id);
		if(blog==null)
			throw ctx.error("form_empty");
		if(!(user.getId()==blog.getUser() || user.IsManager()))
			throw ctx.error("no_permission");
		//删除blog的标签
		ObjTag.delete(id, Tag.TYPE_BLOG);
		blog.Delete();
		ctx.output_json("id", blog.getId());
	}
	
	/**
	 * 添加合集
	 * @param ctx
	 * @throws IOException 
	 */
	@Annotation.UserRequired
	public void collect(RequestContext ctx) throws IOException {
		User user = ctx.user();
		String name = ctx.param("name", "");
		if(name=="")
			throw ctx.error("blog_collection_not_null");
		if(name.length()>80)
			throw ctx.error("blog_collection_too_long");
		BlogCollection bean = new BlogCollection();
		bean.setName(name);
		bean.setUser(user.getId());
		bean.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		long id = bean.Save();
		ctx.output_json("id", id);
	}
	
	/**
	 * 返回相关tags的json数据
	 * @param ctx
	 * @throws IOException
	 */
	public void tags(RequestContext ctx) throws IOException {
		String key = ctx.param("key", "");
		List<String> tags = Tag.INSTANCE.find(key);
		ctx.print(new Gson().toJson(tags));
	}
	/**
	 * 更新blog
	 * @param ctx
	 * @param user
	 * @param blog
	 * @return
	 * @throws IOException 
	 */
	private long update(RequestContext ctx, byte type) throws IOException {
		User user = ctx.user();
		long id = ctx.id();
		Blog blog = Blog.INSTANCE.Get(id);
		if(blog==null)
			throw ctx.error("form_empty");
		if(!(user.IsManager() || user.getId()==blog.getUser()))
			throw ctx.error("no_permission");
		Blog form = ctx.form(Blog.class);
		String result = checkBlog(form);
		if(result!=null)
			throw ctx.error(result);
		blog.setText(form.getText());
		blog.setTitle(form.getTitle());
		blog.setDraft(type);
		blog.setCollection(form.getCollection());
		addtags(ctx, id);
		ctx.output_json("id", id);
		return blog.update();
	}
	/**
	 * blog 格式验证
	 * @param user
	 * @param blog
	 * @return
	 */
	private String checkBlog(Blog blog) {
		String code = null;
		if(blog.getTitle()==null || blog.getTitle().length()<1)
			code = "blog_title_not_null";
		else if(blog.getTitle().length()>120)
			code = "blog_title_too_long";
		if(blog.getText()==null || blog.getText().length()<1)
			code = "blog_text_not_null";
		return code;
	}
	
	/**
	 * 添加标签
	 * @param ctx
	 * @param id
	 */
	private void addtags(RequestContext ctx, long id) {
		//处理标签
		String[] tags = ctx.params("tag");
		if(tags==null)
			return;
		for(String t : tags) {
			if(t.length()>80)
				throw ctx.error("tag_too_long");
		}
		Tag.INSTANCE.add(tags, Tag.TYPE_BLOG, id);
	}
}
