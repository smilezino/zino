package my.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.markdownj.MarkdownProcessor;

import my.db.DBbean;
import my.db.QueryHelper;

/**
 * 记事
 * @author smile
 *
 */
public class Notes extends DBbean {
	public static final int STATUS_UNPOST = 0;
	public static final int STATUS_POST = 1;
	public static final Notes INSTANCE = new Notes();
	private long user;
	private String title;
	private String text;
	private int status;
	private Timestamp createTime;
	
	/**
	 * 解析note文本
	 * @return
	 */
	public String markdown() {
		MarkdownProcessor m = new MarkdownProcessor();
		return m.markdown(text.replaceAll("\n", "\n\n"));
	}
	
	/**
	 * 解析要显示的title文本
	 * @return
	 */
	public String prefixText() {
		String[] strs = StringUtils.split(text, "\n");
		return strs[0].length()>0?strs[0]:"记事";
	}
	/**
	 * 统计note个数
	 * @param user
	 * @return
	 */
	public static long count(long user) {
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM z_notes WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if(user!=0) {
			sql.append(" AND user=?");
			params.add(user);
		}
		return QueryHelper.stat(sql.toString(), params.toArray());
	}
	
	/**
	 * 获取notes
	 * @param user
	 * @param page
	 * @param count
	 * @return
	 */
	public static List<Notes> list(long user, int page, int count) {
		StringBuffer sql = new StringBuffer("SELECT * FROM z_notes WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if(user!=0) {
			sql.append(" AND user=?");
			params.add(user);
		}
		sql.append(" ORDER BY id DESC");
		return QueryHelper.query_slice(Notes.class, sql.toString(), page, count, params.toArray());
	}
	
	
	@Override
	protected String TableName() {
		return "z_notes";
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
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}	
}
