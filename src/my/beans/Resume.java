package my.beans;

import my.db.DBbean;
import my.db.QueryHelper;
import org.markdownj.MarkdownProcessor;

import java.sql.Timestamp;
import java.util.List;

public class Resume extends DBbean {
    public static final Resume INSTANCE = new Resume();

    private long id;
    private long user;
    private String content;
    private Timestamp createTime;
    private Timestamp updateTime;
    private long viewCount;

    @Override
    protected String TableName() {
        return "z_resumes";
    }

    /**
     * 解析markdown
     * @return
     */
    public String markdown() {
        return new MarkdownProcessor().markdown(content);
    }

    /**
     * 浏览数+1
     */
    public void incViewCount() {
        UpdateField("viewCount", viewCount + 1);
    }

    /**
     * 分页列出简历信息
     * @param page
     * @param count
     * @return
     */
    public List<Resume> list(int page, int count) {
        String sql = "SELECT * FROM " + TableName() + " ORDER BY id DESC";
        return QueryHelper.query_slice(Resume.class, sql, page, count);
    }

    /**
     * 取出最近的一条resume
     * @return
     */
    public Resume resume() {
        return Get("1=1 ORDER BY id DESC LIMIT 1");
    }

    public long getId() {
        return id;
    }

    public long getUser() {
        return user;
    }

    public void setUser(long user) {
        this.user = user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public long getViewCount() {
        return viewCount;
    }

    public void setViewCount(long viewCount) {
        this.viewCount = viewCount;
    }
}
