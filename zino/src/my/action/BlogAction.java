package my.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import my.beans.Blog;
import my.beans.ObjTag;
import my.beans.Tag;
import my.beans.User;
import my.service.RequestContext;
import my.service.Annotation;

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
		if(user==null) {
			form.setUser(0);
			form.setStatus(Blog.STATUS_POST);
		}else{
			form.setUser(user.getId());
		}
		form.setDraft(Blog.UNDRAFT);
		form.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		long id = form.Save();
		//处理标签
		addtags(ctx, id);
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
		long id = draft.Save();
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
		if(user.getId()!=blog.getUser() || !user.IsManager())
			throw ctx.error("no_permission");
		//删除blog的标签
		ObjTag.delete(id, Tag.TYPE_BLOG);
		blog.Delete();
		ctx.output_json("id", blog.getId());
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
		if(!user.IsManager() || user.getId()!=blog.getUser())
			throw ctx.error("no_permission");
		Blog form = ctx.form(Blog.class);
		String result = checkBlog(form);
		if(result!=null)
			throw ctx.error(result);
		blog.setText(form.getText());
		blog.setTitle(form.getTitle());
		blog.setDraft(type);
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
