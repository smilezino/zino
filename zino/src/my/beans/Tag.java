package my.beans;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import my.db.DBbean;
import my.db.QueryHelper;

public class Tag extends DBbean{
	public static final Tag  INSTANCE = new Tag();
	public static final byte TYPE_BLOG = 1;
	public static final byte TYPE_TODO = 2;
	
	/**
	 * 添加标签
	 * @param tags
	 * @param type
	 * @param obj
	 */
	public void add(String[] tags, byte type, long obj) {
		ObjTag.delete(obj, type);
		List<Tag> Tags = all();
		for(String tag : tags) {
			Tag t = exist(Tags, tag);
			if(t!=null) {
				ObjTag.addOne(obj, t.getId(), type);
			}else{
				long id = addOne(tag);
				ObjTag.addOne(obj, id, type);
			}
		}
		
	}
	
	/**
	 * 判断tag是否存在
	 * @param tag
	 * @return
	 */
	public Tag exist(List<Tag> tags, String tag) {
		for(Tag t : tags) {
			if(t.getTag().equalsIgnoreCase(tag))
				return t;
		}
		return null;
	}
	
	/**
	 * 添加标签
	 * @param tag
	 * @return
	 */
	public long addOne(String tag) {
		Tag t = new Tag();
		t.setTag(tag);
		t.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		return Save();
	}
	
	/**
	 * 获取所有的tag
	 * @return
	 */
	public List<Tag> all() {
		String sql = "SELECT * FROM z_tag";
		return QueryHelper.query(Tag.class, sql);
	}
	
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
