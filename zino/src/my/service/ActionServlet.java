package my.service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import my.beans.User;
import my.db.DBException;
import my.service.Annotation;
import my.utils.ResourceUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author smile
 * @date 2013-2-4 下午10:16:40
 */
public class ActionServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(ActionServlet.class);
	private final static HashMap<String, Object> actions = new HashMap<String, Object>();
	private final static HashMap<String, Method> methods = new HashMap<String, Method>();
	private String packages = null;
	
	@Override
	public void init() throws ServletException {
		packages = getInitParameter("packages");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		process(RequestContext.get(), false);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		process(RequestContext.get(), true);
	}
	
	protected void process(RequestContext ctx, boolean is_post) 
			throws ServletException, IOException
	{
		try {
			ctx.response().setContentType("text/html;charset=utf-8");
			if(_process(ctx, is_post));
		}catch(InvocationTargetException e){
			Throwable t = e.getCause();
			if(t instanceof ActionException)
				handleActionException(ctx, (ActionException)t);
			else if(t instanceof DBException)
				handleDBException(ctx, (DBException)t);
			else
				throw new ServletException(t);
		}catch(ActionException t){
			handleActionException(ctx, t);
		}catch(IOException e){
			handleException(ctx, "unknow_error");
			throw e;
		}catch(DBException e){
			handleDBException(ctx, e);
		}catch(Exception e){
			log("Exception in action process.", e);
			handleException(ctx, "unknow_error");
			throw new ServletException(e);
		}finally{
		}
	}
	/**
	 * Action业务异常
	 * @param req
	 * @param resp
	 * @param t
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void handleActionException(RequestContext req, ActionException t)	throws ServletException, IOException {		
		handleException(req, t.getMessage());
	}
	
	protected void handleDBException(RequestContext req, DBException e) throws ServletException, IOException {
		log("DBException in action process.", e);
		handleException(req, ResourceUtils.getString("error", "database_error"));
	}
	protected void handleException(RequestContext req, String msg) throws ServletException, IOException {
		if(StringUtils.equalsIgnoreCase("login", msg)) {
			req.output_json(new String[]{"unlogin","msg"}, new Object[]{1,"unlogin"});
		}else
			req.output_json(new String[]{"error","msg"}, new Object[]{1,msg});
	}
	private boolean _process(RequestContext ctx, boolean is_post)
			throws  IllegalAccessException, 
					IllegalArgumentException, 
					InvocationTargetException, 
					IOException {
		
		String requestURI = ctx.uri();
		User user = ctx.user();
		log.info("[ACTION] >>> " + requestURI);
		String[] parts = StringUtils.split(requestURI, "/");
		if(parts.length < 2) {
			ctx.not_found();
			return false;
		}
		Object action = this._LoadAction(parts[1]);
		if(action == null){
			ctx.not_found();
			return false;
		}
		//类上注解，该方法的执行必须是post方式
		if (!is_post && action.getClass().isAnnotationPresent(Annotation.PostMethod.class)) {
			ctx.not_found();
			return false;
		}
		// 类上注解，方法执行必须是登录用户
		if(user==null && action.getClass().isAnnotationPresent(Annotation.User.class)) {
			ctx.output_json(new String[]{"unlogin","msg"}, new Object[]{1,"unlogin"});
			return false;
		}
		
		String action_method_name = parts.length>2?parts[2]:"index";
		Method m_action = _GetActionMethod(action, action_method_name);
		if(m_action == null){
			ctx.not_found();
			return false;
		}
		
		//判断action方法是否只支持POST
		if (!is_post && m_action.isAnnotationPresent(Annotation.PostMethod.class)){
			ctx.not_found();
			return false;
		}
		
		//登录用户操作
		if(user==null && m_action.isAnnotationPresent(Annotation.User.class)) {
			ctx.output_json(new String[]{"unlogin","msg"}, new Object[]{1,"unlogin"});
			return false;
		}
		
		//调用Action方法之准备参数
		int arg_c = m_action.getParameterTypes().length;
		switch(arg_c){
		case 0:
			m_action.invoke(action);
			break ;
		case 1:
			m_action.invoke(action, ctx);
			break;
		default:
			return false;
		}
		return true;
	}
	
	public Object _LoadAction(String act_name) {
		Object action = actions.get(act_name);
		if(action ==null) {
			String cls = packages + "." + StringUtils.capitalize(act_name) + "Action";
			action = this._LoadActionOfFullname(act_name, cls);
		}
		return action;
	}
	
	public Object _LoadActionOfFullname(String act_name, String cls) {
		Object action = null;
		try {
			action = Class.forName(cls).newInstance();
		} catch(Exception e) {}
		if(!actions.containsKey(act_name)){
			synchronized(actions){
				actions.put(act_name, action);
			}
		}
		return action;
	}
	private Method _GetActionMethod(Object action, String method) {
		String key = action.getClass().getSimpleName() + '.' + method;
		Method m = methods.get(key);
		if(m != null) return m;
		for(Method m1 : action.getClass().getMethods()){
			if(m1.getModifiers()==Modifier.PUBLIC && m1.getName().equals(method)){
				synchronized(methods){
					methods.put(key, m1);
				}
				return m1 ;
			}
		}
		return null;
	}
	@Override
	public void destroy() {
		for(Object action : actions.values())  {
			try {
				Method dm = action.getClass().getMethod("destory");
				if(dm != null)
					dm.invoke(action);
			} catch(Exception e) {}
		}
		super.destroy();
	}
}
