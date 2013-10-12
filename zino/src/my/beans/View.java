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
public class View extends DBbean{
	public static final View INSTANCE = new View();
	public static final byte STATUS_ALL = -1; //所有todo
	public static final byte STATUS_UNDO = 0; //未做todo
	public static final byte STATUS_DO = 1;   //已做todo
	public static final byte STATUS_ARCHIVE=8;//收藏todo
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
		UpdateField("status", STATUS_DO);
	}
	/**
	 * 收藏
	 */
	public void archive() {
		UpdateField("status", STATUS_ARCHIVE);
	}
	
	/**
	 * 列出条件列出todo
	 * @param user
	 * @return
	 */
	public List<View> listByUser(long user, int status) {
		StringBuffer sql = new StringBuffer("SELECT * FROM " + TableName() + " WHERE user=?");
		List<Object> params = new ArrayList<Object>();
		params.add(user);
		if(status != STATUS_ALL) {
			sql.append(" AND status=?");
			params.add(status);
		}
		sql.append(" ORDER BY status ASC, sort DESC, createTime DESC");
		return QueryHelper.query(View.class, sql.toString(), params.toArray());
	}
	
	/**
	 * 分页列出user所有todo
	 * @param user
	 * @param page
	 * @param size
	 * @return
	 */
	public List<View> listByFilter(long user, int status, int page, int size) {
		StringBuffer sql = new StringBuffer("SELECT * FROM " + TableName() + " WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if(user>0){
			sql.append(" AND user=?");
			params.add(user);
		}
		if(status != STATUS_ALL) {
			sql.append(" AND status=?");
			params.add(status);
		}
		sql.append(" ORDER BY status ASC, sort DESC, createTime DESC");
		return QueryHelper.query_slice(View.class, sql.toString(), page, size, params.toArray());
	}
	
	/**
	 * 用户todo总数
	 * @param user
	 * @return
	 */
	public long countByFilter(long user, int status) {
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM " + TableName() + " WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if(user>0) {
			sql.append(" AND user=?");
			params.add(user);
		}
		if(status != STATUS_ALL) {
			sql.append(" AND status=?");
			params.add(status);
		}
		return QueryHelper.stat(sql.toString(), params.toArray());
	}
	
	/**
	 * 分页列出所有view
	 * @return
	 */
	public List<View> list(int page, int size) {
		String sql = "SELECT * FROM " + TableName();
		return QueryHelper.query_slice(View.class, sql, page, size);
	}
	/**
	 * 所有view个数
	 * @return
	 */
	public long count() {
		String sql = "SELECT COUNT(*) FROM " + TableName();
		return QueryHelper.stat(sql);
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

	@Override
	protected String TableName() {
		return "z_todo";
	}
	
}
