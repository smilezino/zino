package my.beans;

import java.sql.Timestamp;

import my.db.DBbean;
import my.db.QueryHelper;

public class Blog extends DBbean{
	public static final Blog INSTANCE = new Blog();
	public static final byte UNDRAFT = 0;//非草稿
	public static final byte DRAFT = 1;//草稿
	
	/**
	 * 修改博客
	 * @return
	 */
	public long update() {
		String sql = "UPDATE " + TableName() + " SET title=?,text=?,draft=?";
		return QueryHelper.update(sql, title, text, draft);
	}
	/**
	 * 博客浏览数+1
	 * @return
	 */
	public boolean addViewCount() {
		return UpdateField("viewCount", viewCount+1);
	}
	/**
	 * 分享次数+1
	 * @return
	 */
	public boolean addShareCount() {
		return UpdateField("shareCount", shareCount+1);
	}

	private long user;
	private String title;
	private String text;
	private int viewCount;
	private int shareCount;
	private byte status;
	private byte draft;
	private Timestamp createTime;
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
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public int getShareCount() {
		return shareCount;
	}
	public void setShareCount(int shareCount) {
		this.shareCount = shareCount;
	}
	public byte getStatus() {
		return status;
	}
	public void setStatus(byte status) {
		this.status = status;
	}
	public byte getDraft() {
		return draft;
	}
	public void setDraft(byte draft) {
		this.draft = draft;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
	protected String TableName() {
		return "z_blog";
	}
	
}
