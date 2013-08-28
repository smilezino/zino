package my.action;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;


import my.beans.User;
import my.service.RequestContext;
import my.utils.RegexUtils;
/**
 * 用户登陆注销action
 * @author smile
 * @date 2013-2-19 下午3:58:05
 */
public class UserAction {
	
	/**
	 * 登陆
	 * @param ctx
	 * @throws IOException
	 */
	public void login(RequestContext ctx) throws IOException {
		String username = ctx.param("username");
		String pwd = ctx.param("password");
//		if(!ImageUtils.validate(ctx.request()))
//			throw ctx.error("verify_code_error");
		User user = User.Login(username, pwd);
		if(user == null)
			throw ctx.error("user_login_failed");
		ctx.saveUserInCookie(user);
		ctx.output_json("id", user.getId());
	}
	/**
	 * 退出登录
	 * @param ctx
	 * @throws IOException 
	 */
	public void loginout(RequestContext ctx) throws IOException {
		ctx.request().setAttribute(User.G_USER, null);
		ctx.deleteUserInCookie();
		ctx.output_json("success", "success");
	}
	
	/**
	 * 注册
	 * @param ctx
	 * @throws IOException
	 */
	public void register(RequestContext ctx) throws IOException {
		User user = ctx.form(User.class);
		String p1 = ctx.param("pwd1", "");
		String p2 = ctx.param("pwd2", "");
		if(p1==null || p2==null || !p1.equals(p2))
			throw ctx.error("user_pwd_error");
		if(p1.length() < User.PWD_MIN_LENGTH)
			throw ctx.error("user_pwd_too_short");
		String msg = check(user);
		if(msg!=null)
			throw ctx.error(msg);
		if(user.isExistEmail())
			throw ctx.error("user_email_exist");
		if(user.isExistName())
			throw ctx.error("user_name_exist");
		user.setPwd(p1);
		user.setRole(User.ROLE_USER);
		user.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		long id = user.Save();
		ctx.saveUserInCookie(user);
		ctx.output_json("id", id);
	}
	
	/**
	 * 用户信息格式检查
	 * @param user
	 * @return
	 */
	private String check(User user) {
		String msg = null;
		if(user==null)
			msg = "form_empty";
		if(user.getName()==null)
			msg = "user_name_not_null";
		else if(user.getName().length()>80)
			msg = "user_name_too_long";
		else if(RegexUtils.isEmail(user.getName()))
			msg = "user_name_not_email";
		if(user.getEmail()==null)
			msg = "user_email_not_null";
		else if(user.getEmail().length()>120)
			msg = "user_email_too_long";
		else if(!RegexUtils.isEmail(user.getEmail()))
			msg = "user_email_not_email";
			
		return msg;
	}
}
