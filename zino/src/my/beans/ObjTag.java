package my.beans;

import java.util.List;

import my.db.DBbean;
import my.db.QueryHelper;

public class ObjTag extends DBbean {
	
	private long obj;
	private long tag;
	private byte type;
	

	/**
	 * 获取一个关联的 对象 标签
	 * @param obj
	 * @param tag
	 * @param type
	 * @return
	 */
	public static ObjTag getOne(long obj, long tag, byte type) {
		String sql = "SELECT * FROM z_obj_tag WHERE obj=? AND tag=? AND type=?";
		return QueryHelper.read(ObjTag.class, sql, obj, tag, type);
	}
	
	/**
	 * 给对象添加标签
	 * @param obj
	 * @param tag
	 * @param type
	 * @return
	 */
	public static long addOne(long obj, long tag, byte type) {
		ObjTag ot = new ObjTag();
		ot.setObj(obj);
		ot.setTag(tag);
		ot.setType(type);
		return ot.Save();
	}
	/**
	 * 删除对象的所有标签
	 * @param obj
	 * @param type
	 * @return
	 */
	public static int delete(long obj, byte type) {
		String sql = "DELETE FROM z_obj_tag WHERE obj=? AND type=?";
		return QueryHelper.update(sql, obj, type);
	}
	
	/**
	 * 获取一类型的所有标签
	 * @param type
	 * @return
	 */
	public static List<ObjTag> listByFilter(int type) {
		String sql = "SELECT * FROM z_obj_tag WHERE type=?";
		return QueryHelper.query(ObjTag.class, sql, type);
	}
	public long getObj() {
		return obj;
	}
	public void setObj(long obj) {
		this.obj = obj;
	}
	public long getTag() {
		return tag;
	}
	public void setTag(long tag) {
		this.tag = tag;
	}
	public byte getType() {
		return type;
	}
	public void setType(byte type) {
		this.type = type;
	}
	@Override
	protected String TableName() {
		return "z_obj_tag";
	}

}
