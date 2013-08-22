package my.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import com.google.gson.Gson;

import my.beans.Todo;
import my.beans.User;
import my.service.RequestContext;

public class TodoAction {
	
	/**
	 * 添加一个todo
	 * @param ctx
	 * @throws IOException 
	 */
	public void add(RequestContext ctx) throws IOException {
		User user = ctx.user();
		Todo form = ctx.form(Todo.class);
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
		Todo todo = Todo.INSTANCE.Get(id);
		if(todo==null || todo.getUser()!=user.getId())
			throw ctx.error("no_permission");
		todo.mark();
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
		Todo todo = Todo.INSTANCE.Get(id);
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
		List<Todo> list = Todo.INSTANCE.listByFilter(user, Todo.STATUS_UNDO);
		ctx.print(new Gson().toJson(list));
	}
}
