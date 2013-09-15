package my.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;

import my.beans.View;
import my.beans.User;
import my.service.RequestContext;

public class ViewAction {
	
	/**
	 * 添加一个todo
	 * @param ctx
	 * @throws IOException 
	 */
	public void add(RequestContext ctx) throws IOException {
		User user = ctx.user();
		View form = ctx.form(View.class);
		if(form==null || form.getUrl()==null || form.getUrl().length()<=0)
			throw ctx.error("form_empty");
		form.setUser(user.getId());
		form.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		long id = form.Save();
		ctx.output_json("id", id);
	}
	/**
	 * 标记为已做
	 * @param ctx
	 * @throws IOException
	 */
	public void mark(RequestContext ctx) throws IOException {
		User user = ctx.user();
		long id = ctx.id();
		View todo = View.INSTANCE.Get(id);
		if(todo==null || todo.getUser()!=user.getId())
			throw ctx.error("no_permission");
		todo.mark();
		ctx.output_json("id", todo.getId());
	}
	
	/**
	 * 存档
	 * @param ctx
	 * @throws IOException
	 */
	public void archive(RequestContext ctx) throws IOException {
		User user = ctx.user();
		long id = ctx.id();
		View todo = View.INSTANCE.Get(id);
		if(todo==null || todo.getUser()!=user.getId())
			throw ctx.error("no_permission");
		todo.archive();
		ctx.output_json("id", todo.getId());
	}
	/**
	 * 删除
	 * @param ctx
	 * @throws IOException
	 */
	public void delete(RequestContext ctx) throws IOException {
		User user = ctx.user();
		long id = ctx.id();
		View todo = View.INSTANCE.Get(id);
		if(todo==null || todo.getUser()!=user.getId())
			throw ctx.error("no_permission");
		todo.Delete();
		ctx.output_json("id", todo.getId());
	}
	
	/**
	 * 返回undo 列表
	 * @param ctx
	 * @throws IOException
	 */
	public void list(RequestContext ctx) throws IOException {
		User user = ctx.user();
		List<View> list = View.INSTANCE.listByFilter(user, View.STATUS_UNDO);
		ctx.print(new Gson().toJson(list));
	}
}
