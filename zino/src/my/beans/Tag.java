package my.beans;

import java.sql.Timestamp;

import my.db.DBbean;

public class Tag extends DBbean{
	public static final Tag  INSTANCE = new Tag();
	
	private String tag;
	private byte type;
	private byte status;
	private Timestamp createTime;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
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
		return "z_tag";
	}
	
}
