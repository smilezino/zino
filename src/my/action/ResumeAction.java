package my.action;

import my.beans.Resume;
import my.beans.User;
import my.service.Annotation;
import my.service.RequestContext;
import my.toolbox.FormatTool;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zino on 7/1/14.
 */
@Annotation.PostMethod
@Annotation.UserRequired
public class ResumeAction {

    /**
     * 更新简历
     * @param ctx
     * @throws IOException
     */
    public void post(RequestContext ctx) throws IOException {
        User user = ctx.user();
        Resume form = ctx.form(Resume.class);
        form.setUser(user.getId());
        if(form.getContent()==null) {
            throw ctx.error("resume_null");
        }
        Timestamp time = new Timestamp(Calendar.getInstance().getTimeInMillis());
        form.setCreateTime(time);
        form.setUpdateTime(time);
        long id = form.Save();
        ctx.output_json("id", id);
    }

    /**
     * 删除简历
     * @param ctx
     * @throws IOException
     */
    public void delete(RequestContext ctx) throws IOException {
        User user = ctx.user();
        long id = ctx.id();
        Resume bean = Resume.INSTANCE.Get(id);
        if(bean==null || bean.getUser()!=user.getId()) {
            throw ctx.error("form_empty");
        }
        bean.Delete();
        ctx.output_json("id", id);
    }

    /**
     * 更新简历时间
     * @param ctx
     * @throws IOException
     */
    @Annotation.UserRequired(role = User.ROLE_MANAGER)
    public void resume_time(RequestContext ctx) throws IOException {
        long id = ctx.id();
        Resume bean = Resume.INSTANCE.Get(id);
        if(bean==null) {
            throw ctx.error("form_empty");
        }
        String time = ctx.param("time", FormatTool.date("yyyy-MM-dd HH:mm:ss", new Date()));
        bean.UpdateField("updateTime", time);
        ctx.output_json("id", id);
    }
}
