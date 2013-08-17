package my.beans;

import java.sql.Timestamp;
import my.db.DBbean;
import my.db.QueryHelper;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 登陆用户资料
 * @author smile
 * @date 2013-2-18 下午10:36:48
 */

public class User extends DBbean {
	public final static transient User INSTANCE = new User();
	public static int ROLE_ADMIN = 1;
	public static int ROLE_MANAGER = 10;

	@Override
	protected String TableName() {
		return "z_user";
	}
	/**
	 * 登陆
	 * @param email
	 * @param pwd
	 * @return
	 */
	public static User Login(String email, String pwd) {
		if(StringUtils.isBlank(email) || StringUtils.isBlank(pwd)) return null;
		User user;
		String sql = "SELECT * FROM z_user WHERE email = ?";
		user = (User)QueryHelper.read(User.class, sql, email);
		if(user != null && StringUtils.equals(user.pwd, pwd)) {
			return user;
		}
		return null;

	}
	
	/**
	 * 生成校验码
	 */
	public void createActiveCode() {
		this.activeCode = RandomStringUtils.randomAlphanumeric(20);
		UpdateField("activeCode", this.activeCode);
	}
	
	/**
	 * 校验码登录
	 * @param activeCode
	 * @return
	 */
	public static User activeUser(String activeCode) {
		if(StringUtils.isBlank(activeCode) || activeCode.length()!=20) return null;
		String sql = "SELECT * FROM z_user WHERE activeCode=?";
		return (User)QueryHelper.read(User.class, sql, activeCode);
	}
	/**
	 * 是否是超级管理员
	 * @return
	 */
	public boolean isAdmin(){
		return role == ROLE_ADMIN;
	}
	/**
	 * 是否是管理员
	 * @return
	 */
	public boolean isManager(){
		return role >= ROLE_MANAGER;
	}
	
	private String name;
	private String email;
	private String pwd;
	private int role;
	private String ident;
	private String activeCode;
	private Timestamp time;

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
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}

}
