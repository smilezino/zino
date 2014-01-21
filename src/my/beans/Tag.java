package my.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
		if(tags==null)
			return;
		tags = Unique(tags);
		List<Tag> Tags = all();
		ObjTag.delete(obj, type);
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
		//t.setId(0);
		t.setTag(tag);
		t.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		return t.Save();
	}
	
	/**
	 * 获取所有的tag
	 * @return
	 */
	public List<Tag> all() {
		String sql = "SELECT * FROM z_tag";
		return QueryHelper.query(Tag.class, sql);
	}
	
	/**
	 * 列出所有blog的tag，按tag数由高到低排列
	 * @param count
	 * @return
	 */
	public List<Tag> listTagByBlog(int count) {
		return listByFilter(Tag.TYPE_BLOG, count);
	}
	/**
	 * 列出固定个数的某类tag,tag数由高到低排列
	 * @param type
	 * @param count
	 * @return
	 */
	public List<Tag> listByFilter(int type, int count) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT * FROM z_tag WHERE id IN (SELECT tag FROM (SELECT * FROM z_obj_tag WHERE type=? GROUP BY tag ORDER BY COUNT(tag) DESC");
		params.add(type);
		if(count!=-1) {
			sql.append(" LIMIT ?) AS t)");
			params.add(count);
		}else
			sql.append(") AS t)");
		
		return QueryHelper.query(Tag.class, sql.toString(), params.toArray());
	}
	
	/**
	 * 获取一篇blog的所有tag
	 * @param id
	 * @return
	 */
	public List<Tag> listByBlog(long id) {
		return listByType(id, Tag.TYPE_BLOG);
	}
	/**
	 * 获取某一对象的所有tag
	 * @param obj
	 * @param type
	 * @return
	 */
	public List<Tag> listByType(long obj, int type) {
		String sql = "SELECT * FROM z_tag WHERE id IN(SELECT tag FROM z_obj_tag WHERE obj=? AND type=?)";
		return QueryHelper.query(Tag.class, sql, obj, type);
	}
	
	/**
	 * 某一类标签个数
	 * @param tag
	 * @param type
	 * @return
	 */
	public long countByFilter(long tag, int type) {
		String sql = "SELECT COUNT(*) FROM z_obj_tag WHERE tag=? AND type=?";
		return QueryHelper.stat(sql, tag, type);
	}
	
	/**
	 * 计算blog某一标签的总数
	 * @param tag
	 * @return
	 */
	public long countByBlogTag(long tag) {
		return countByFilter(tag, TYPE_BLOG);
	}
	
	/**
	 * 查询以key开头的tag
	 * @param key
	 * @return
	 */
	public List<String> find(String key) {
		String sql = "SELECT tag FROM z_tag WHERE tag LIKE ?";
		return QueryHelper.query(String.class, sql, key+"%");
	}
	
	/**
	 * remove same string from array
	 * @param a
	 * @return
	 */
	private static String[] Unique(String[] a) {
		List<String> list = new LinkedList<String>();
		for (int i = 0; i < a.length; i++) {
			String str = a[i].trim();
			if (!list.contains(str) && str.length()>0) {
				list.add(str);
			}
		}
		return (String[]) list.toArray(new String[list.size()]);
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
