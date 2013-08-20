package my.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import my.db.DBbean;
import my.db.QueryHelper;

/**
 * 待做任务
 * @author zino
 *
 */
public class Todo extends DBbean{
	public static final Todo INSTANCE = new Todo();
	public static final byte STATUS_ALL = -1; //所有todo
	public static final byte STATUS_UNDO = 0; //未做todo
	public static final byte STATUS_DO = 1;   //已做todo
	private long user;
	private String title;
	private String url;
	private String description;
	private int sort;
	private byte status;
	private Timestamp createTime;
	
	/**
	 * 标记为已做
	 */
	public void mark() {
		UpdateField("status", 1);
	}
	
	/**
	 * 列出条件列出todo
	 * @param user
	 * @return
	 */
	public List<Todo> listByFilter(User user, byte status) {
		StringBuffer sql = new StringBuffer("SELECT * FROM " + TableName() + " WHERE user=?");
		List<Object> params = new ArrayList<Object>();
		params.add(user.getId());
		if(status != STATUS_ALL) {
			sql.append(" AND status=?");
			params.add(status);
		}
		sql.append(" ORDER BY status ASC, sort DESC, createTime DESC");
		return QueryHelper.query(Todo.class, sql.toString(), params.toArray());
	}
	
	
	public long getUser() {
		return user;
	}
	public void setUser(long user) {
		this.user = user;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
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
}
