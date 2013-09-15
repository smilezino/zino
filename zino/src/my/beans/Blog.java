package my.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.markdownj.MarkdownProcessor;





import my.db.DBbean;
import my.db.QueryHelper;

public class Blog extends DBbean{
	public static final Blog INSTANCE = new Blog();
	public static final byte UNDRAFT = 0;//非草稿
	public static final byte DRAFT = 1;//草稿
	
	public static final byte STATUS_POST = 1; //投稿
	public static final byte STATUS_NORMAL = 0;
	public static final byte STATUS_ALL = -1;
	
	public static List<String> Anchor = new ArrayList<String>(){{
		add("h1");
		add("h2");
		add("h3");
		add("h4");
		add("h5");
		add("h6");
	}};
	
	/**
	 * 修改博客
	 * @return
	 */
	public long update() {
		String sql = "UPDATE " + TableName() + " SET title=?,text=?,draft=? WHERE id=?";
		return QueryHelper.update(sql, title, text, draft, getId());
	}
	
	/**
	 * 处理markdown格式
	 * @return
	 */
	public String markdown() {
		MarkdownProcessor m = new MarkdownProcessor();
		return m.markdown(text);
	}
	
	/**
	 * h标签添加锚点
	 * @return
	 */
	public String htmlAnchor() {
		Document doc = Jsoup.parseBodyFragment(markdown());
		Elements anchors = doc.select("*");
		int i = 0;
		for(Element anchor : anchors){
			String tagName = anchor.tagName().toLowerCase();
			if(Anchor.contains(tagName) && anchor.hasText()) {
				 i++;
				anchor.before("<span id='"+tagName+"_"+String.valueOf(i)+"'></span>");
			}
		}
		return doc.body().html();
	}
	
	/**
	 * 自动生成目录内容
	 * @return
	 */
	public String htmlContent() {
		StringBuffer content = new StringBuffer();
		Document doc = Jsoup.parseBodyFragment(markdown());
		Elements anchors = doc.select("*");
		int i = 0;
		for(Element anchor : anchors) {
			String tagName = anchor.tagName().toLowerCase();
			if(Anchor.contains(tagName) && anchor.hasText()) {
				i++;
				content.append("<li><a href='#"+tagName+"_"+String.valueOf(i)+"'>"+anchor.text()+"</a></li>");
			}
		}
		return content.toString();
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

	/**
	 * 根据条件分页列出blog列表
	 * @param status
	 * @param tag
	 * @param page
	 * @param count
	 * @return
	 */
	public static List<Blog> listByFilter(int status, String tag, int page, int count) {
		StringBuffer sql = new StringBuffer("SELECT * FROM z_blog WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if(status!=STATUS_ALL) {
			sql.append(" AND status=?");
			params.add(status);
		}
		if(tag!=null && tag.length()>0) {
			sql.append(" AND id IN(SELECT obj FROM z_obj_tag WHERE tag IN(SEELCT id FROM z_tag WHERE tag=? AND type=?))");
			params.add(tag);
			params.add(Tag.TYPE_BLOG);
		}
		sql.append(" ORDER BY status DESC, id DESC");
		return QueryHelper.query_slice(Blog.class, sql.toString(), page, count, params.toArray());
	}
	
	/**
	 * 
	 * @param status
	 * @param tag
	 * @return
	 */
	public static long countByFilter(int status, String tag) {
		StringBuffer sql = new StringBuffer("SELECT COUNT(*) FROM z_blog WHERE 1=1");
		List<Object> params = new ArrayList<Object>();
		if(status!=STATUS_ALL) {
			sql.append(" AND status=?");
			params.add(status);
		}
		if(tag!=null && tag.length()>0) {
			sql.append(" AND id IN(SELECT obj FROM z_obj_tag WHERE tag IN(SEELCT id FROM z_tag WHERE tag=? AND type=?))");
			params.add(tag);
			params.add(Tag.TYPE_BLOG);
		}
		return QueryHelper.stat(sql.toString(), params.toArray());
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
