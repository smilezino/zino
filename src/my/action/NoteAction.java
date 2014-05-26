package my.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import my.beans.Files;
import my.beans.Notes;
import my.beans.User;
import my.service.Annotation;
import my.service.RequestContext;
import my.toolbox.FormatTool;

/**
 * 记事action
 * @author smile
 *
 */
@Annotation.PostMethod
@Annotation.UserRequired
public class NoteAction {
	/**
	 * 添加note
	 * @param ctx
	 * @throws IOException
	 */
	public void add(RequestContext ctx) throws IOException {
		User user = ctx.user();
		Notes form = ctx.form(Notes.class);
		String msg = check(form);
		if(msg!=null) {
			throw ctx.error(msg);
		}
		form.setUser(user.getId());
		form.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		form.setTitle(FormatTool.date("yyyy-MM-dd", Calendar.getInstance().getTime()));
		long id = form.Save();
		ctx.output_json("id", id);
	}
	
	/**
	 * 删除note
	 * @param ctx
	 * @throws IOException
	 */
	public void delete(RequestContext ctx) throws IOException {
		User user = ctx.user();
		long id= ctx.id();
		Notes bean = Notes.INSTANCE.Get(id);
		if(bean==null || !(bean.getUser()==user.getId() || user.IsManager())){
			throw ctx.error("no_permission");
		}
		bean.Delete();
		ctx.output_json("id", id);
	}
	
	/**
	 * 编辑note
	 * @param ctx
	 * @throws IOException
	 */
	public void edit(RequestContext ctx) throws IOException {
		User user = ctx.user();
		Notes form = ctx.form(Notes.class);
		long id = ctx.id();
		String msg = check(form);
		if(msg!=null) {
			throw ctx.error(msg);
		}
		Notes bean = Notes.INSTANCE.Get(id);
		if(bean==null || !(bean.getUser()==user.getId() || user.IsManager())) {
			throw ctx.error("no_permission");
		}
		bean.UpdateField("text", form.getText());
		ctx.output_json("id", id);
	}
	
	/**
	 * 设置note是否公开
	 * @param ctx
	 * @throws IOException
	 */
	public void changeStatus(RequestContext ctx) throws IOException {
		User user = ctx.user();
		long id = ctx.id();
		int status = ctx.param("s", Notes.STATUS_UNPOST);
		Notes bean = Notes.INSTANCE.Get(id);
		if(bean==null || !(bean.getUser()==user.getId() || user.IsManager())) {
			throw ctx.error("no_permission");
		}
		bean.UpdateField("status", status);
		ctx.output_json("id", id);
	}
	
	/**
	 * note 表单检查
	 * @param bean
	 * @return
	 */
	private String check(Notes bean) {
		String msg = null;
		if(bean==null) {
			msg = "form_empty";
		}else if(bean.getText().length()<1) {
			msg = "note_text_not_null";
		}
		return msg;
	}
}
