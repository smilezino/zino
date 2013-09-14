package my.beans;

import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;

import my.db.DBbean;
import my.db.QueryHelper;
import my.service.RequestContext;
import my.utils.RegexUtils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 用户资料
 * @author smile
 * @date 2013-2-18 下午10:36:48
 */

public class User extends DBbean {
	public final static transient User INSTANCE = new User();
	public static int ROLE_ADMIN = 127;
	public static int ROLE_MANAGER = 10;
	public static int ROLE_USER = 1;
	
	public static final String G_USER = "g_user";
	public static final int ACTIVE_CODE_LENGTH = 40;
	public static final int PWD_MIN_LENGTH = 6;

	@Override
	protected String TableName() {
		return "z_user";
	}
	
	@Override
	public long Save() {
		this.pwd = DigestUtils.shaHex(this.pwd);
		return super.Save();
	}

	/**
	 * 生成校验码
	 */
	public void createActiveCode() {
		this.activeCode = RandomStringUtils.randomAlphanumeric(ACTIVE_CODE_LENGTH);
		UpdateField("activeCode", this.activeCode);
	}
	/**
	 * 检查用户名是否存在
	 * @param user
	 * @return
	 */
	public boolean existName() {
		String sql = "SELECT COUNT(*) FROM " + TableName() + " WHERE name=?";
		return QueryHelper.stat(sql, name) > 0;
	}
	/**
	 * 检查邮箱是否存在
	 * @param email
	 * @return
	 */
	public boolean existEmail() {
		String sql = "SELECT * FROM "+TableName()+" WHERE email=?";
		return QueryHelper.stat(sql, email) > 0;
	}
	/**
	 * 修改密码
	 * @param pwd
	 * @return
	 */
	public boolean changePwd(String pwd) {
		this.pwd = DigestUtils.shaHex(pwd);
		return UpdateField("pwd", this.pwd);
	}
	/**
	 * 登陆
	 * @param email
	 * @param pwd
	 * @return
	 */
	public static User Login(String username, String pwd) {
		if(StringUtils.isBlank(username) || StringUtils.isBlank(pwd)) return null;
		User user;
		String sql;
		if(RegexUtils.isEmail(username))
			sql = "SELECT * FROM z_user WHERE email = ?";
		else
			sql = "SELECT * FROM z_user WHERE name = ?";
		user = QueryHelper.read(User.class, sql, username);
		if(user != null && StringUtils.equals(user.pwd, DigestUtils.shaHex(pwd))) {
			return user;
		}
		return null;

	}
	/**
	 * 获取登录用户
	 * @param req
	 * @return
	 */
	public static User getUser(HttpServletRequest req) {
		User loginUser = (User) req.getAttribute(G_USER);
		if(loginUser == null) {
			User cookieUser = RequestContext.get().getUserFromCookie();
			if(cookieUser == null) return null;
			User user = cookieUser.getUserFromActiveCode(cookieUser.getActiveCode());
			if(user!=null && user.getId()==cookieUser.getId()) {
				req.setAttribute(G_USER, user);
				return user;
			}
		}
		return loginUser;
	}
	
	/**
	 * 校验码登录
	 * @param activeCode
	 * @return
	 */
	public User getUserFromActiveCode(String activeCode) {
		if(StringUtils.isBlank(activeCode) || activeCode.length()!=ACTIVE_CODE_LENGTH) return null;
		String sql = "SELECT * FROM z_user WHERE activeCode = ?";
		return QueryHelper.read_cache(User.class, "user", getId() ,sql, activeCode);
		//System.out.println(CacheRegion());
		//return null;
	}
	/**
	 * 是否是超级管理员
	 * @return
	 */
	public boolean IsAdmin(){
		return role == ROLE_ADMIN;
	}
	/**
	 * 是否是管理员
	 * @return
	 */
	public boolean IsManager(){
		return role >= ROLE_MANAGER;
	}
	
	

	private String name;
	private String email;
	private String pwd;
	private int role;
	private String ident;
	private String activeCode;
	private Timestamp createTime;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getIdent() {
		return ident;
	}
	public void setIdent(String ident) {
		this.ident = ident;
	}
	public String getActiveCode() {
		return activeCode;
	}
	public void setActiveCode(String activeCode) {
		this.activeCode = activeCode;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
