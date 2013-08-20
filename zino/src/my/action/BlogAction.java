package my.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import my.beans.Blog;
import my.beans.User;
import my.service.RequestContext;

/**
 * Blog 操作
 * @author zino
 *
 */
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
		form.setUser(user.getId());
		form.setDraft(Blog.UNDRAFT);
		form.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		long id = form.Save();
		ctx.output_json("id", id);
	}
	/**
	 * 存为草稿
	 * @param ctx
	 * @throws IOException
	 */
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
		ctx.output_json("id", id);
	}
	
	/**
	 * 修改草稿
	 * @param ctx
	 */
	public void update_draft(RequestContext ctx) {
		Blog draft = ctx.form(Blog.class);
		draft.setDraft(Blog.DRAFT);
		update(ctx, draft);
	}
	/**
	 * 修改博客
	 * @param ctx
	 */
	public void update_post(RequestContext ctx) {
		Blog bean = ctx.form(Blog.class);
		bean.setDraft(Blog.UNDRAFT);
		update(ctx, bean);
	}
	
	/**
	 * 从草稿发表博客
	 * @param ctx
	 */
	public void post_form_draft(RequestContext ctx) {
		Blog bean = ctx.form(Blog.class);
		bean.setDraft(Blog.UNDRAFT);
		update(ctx, bean);
	}
	
	/**
	 * 删除博客
	 * @param ctx
	 * @throws IOException
	 */
	public void delete(RequestContext ctx) throws IOException {
		User user = ctx.user();
		long id = ctx.id();
		Blog blog = Blog.INSTANCE.Get(id);
		if(blog==null || user.getId()!=blog.getUser())
			throw ctx.error("no_permission");
		blog.Delete();
		ctx.output_json("id", blog.getId());
	}
	
	/**
	 * 更新blog
	 * @param ctx
	 * @param user
	 * @param blog
	 * @return
	 */
	private long update(RequestContext ctx, Blog blog) {
		User user = ctx.user();
		Blog bean = Blog.INSTANCE.Get(blog.getId());
		if(bean==null || bean.getUser()!=user.getId())
			throw ctx.error("no_permission");
		String result = checkBlog(blog);
		if(result!=null)
			throw ctx.error(result);
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
		if(blog.getTitle()==null)
			code = "blog_title_not_null";
		else if(blog.getTitle().length()>120)
			code = "blog_title_too_long";
		if(blog.getText()==null)
			code = "blog_text_not_null";
		return code;
	}
}
