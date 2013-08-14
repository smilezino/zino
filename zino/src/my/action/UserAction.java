package my.action;

import java.io.IOException;

import my.beans.User;
import my.service.RequestContext;
import my.utils.ImageUtils;
import my.service.Annotation;
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
	@Annotation.PostMethod
	@Annotation.JSONOutputEnabled
	public void login(RequestContext ctx) throws IOException {
		String email = ctx.param("username");
		String pwd = ctx.param("password");
		int role = ctx.param("role",-1);
		if(!ImageUtils.validate(ctx.request()))
			throw ctx.error("verify_code_error");
		User user = User.Login(email, pwd, role);
		if(user == null)
			throw ctx.error("user_login_failed");
		ctx.session().setAttribute("g_user", user);
		ctx.print(user.getId());
	}
	/**
	 * 退出登录
	 * @param ctx
	 */
	public void loginout(RequestContext ctx) {
		ctx.session().setAttribute("g_user", null);
	}
}
