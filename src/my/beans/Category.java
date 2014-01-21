package my.beans;

import java.sql.Timestamp;

import my.db.DBbean;

public class Category extends DBbean {
	public static final Category INSTANCE = new Category();
	
	private String category;
	private byte type;
	private byte status;
	private Timestamp createTime;
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
	protected String TableName() {
		return "z_category";
	}
	
}
