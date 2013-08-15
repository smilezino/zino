package my.beans;

import java.util.List;
import my.db.DBbean;
import my.db.QueryHelper;

import org.apache.commons.lang.StringUtils;

/**
 * 登陆用户资料
 * @author smile
 * @date 2013-2-18 下午10:36:48
 */

public class User extends DBbean {
	public final static transient User INSTANCE = new User();

	@Override
	protected String TableName() {
		return "z_user";
	}
	/**
	 * 登陆
	 * @param username
	 * @param pwd
	 * @param role
	 * @return
	 */
	public static User Login(String username, String pwd, int role) {
		if(StringUtils.isBlank(username) || StringUtils.isBlank(pwd)) return null;
		User user;
		//教师登陆
		if(role == 0) {
			String sql = "SELECT * FROM p WHERE num = ?";
			user = (User)QueryHelper.read(User.class, sql, username);
			if(user != null && StringUtils.equals(user.pwd, pwd)) {
				user.setRole(0);
				return user;
			}
			return null;
		}
		//管理员登陆
		else if(role == 1) {
			String sql = "SELECT * FROM admins WHERE num = ? LIMIT 1";
			user = (User)QueryHelper.read(User.class, sql, username);
			if(user != null && StringUtils.equals(user.pwd, pwd)) {
				return user;
			}
			return null;
		}
		else{
			return null;
		}
	}
	public boolean isAdmin(){
		return role == 1;
	}
	public boolean isManager(){
		return role >= 10;
	}
	/**
	 * 获取所有评分员
	 * @return
	 */
	public List<User> getAdmins() {
		String sql = "SELECT * FROM admins WHERE role != ?";
		return QueryHelper.query(User.class, sql, 1);
	}
	
	@Override
	public long Save() {
		String sql = "INSERT INTO admins (num,pwd,name,role) values(?,?,?,?)";
		return QueryHelper.update(sql, num,pwd,name,role);
	}
	
	/**
	 * 修改密码
	 * @param password
	 * @param table
	 * @param user
	 * @return
	 */
	public long password(String password,String table) {
		String sql = "UPDATE " + table + " SET pwd = ? WHERE num = ?";
		return QueryHelper.update(sql, password, num);
	}
	public long permissions() {
		String sql = "UPDATE p SET permissions=? WHERE num=?";
		return QueryHelper.update(sql, permissions, num);
	}
	/**
	 * 自动补全
	 * @param field
	 * @param key
	 * @return
	 */
	public List<User> List(String field, String key) {
		String sql = "SELECT num,name FROM p WHERE "+field+" like ? LIMIT 10";
		return QueryHelper.query(User.class, sql, key+"%");
	}

	protected String num;
	protected String pwd;
	protected String name;
	protected int role;
	protected String department;
	protected byte permissions;
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public byte getPermissions() {
		return permissions;
	}
	public void setPermissions(byte permissions) {
		this.permissions = permissions;
	}
	
	public static void main(String[] args) {
		User user = User.INSTANCE.Login("123", "123", 0);
		System.out.println(user.getPwd());
	}
}
