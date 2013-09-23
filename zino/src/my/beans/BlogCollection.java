package my.beans;

import java.sql.Timestamp;

import my.db.DBbean;
import my.db.QueryHelper;

public class BlogCollection extends DBbean {
	private long user;
	private String name;
	private String description;
	private byte status;
	private byte type;
	private Timestamp createTime;
	
	/**
	 * 判断user是否有集合
	 * @param user
	 * @param id
	 * @return
	 */
	public static boolean exist(long user, long id) {
		String sql = "SEELCT COUNT(*) FROM z_blog_collection WHERE user=? AND id=?";
		return QueryHelper.stat(sql, user, id) > 0;
	}
	
	@Override
	protected String TableName() {
		return "z_blog_collection";
	}
	public long getUser() {
		return user;
	}
	public void setUser(long user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
}
