package my.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

import my.toolbox.LinkTool;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import my.beans.Files;
import my.beans.User;
import my.service.RequestContext;
import my.service.Annotation;
import my.utils.ImageUtils;
import my.utils.StorageUtils;

/**
 * 上传下载文件
 * @author smile
 * @date 2013-2-22 下午5:40:02
 */
public class FileAction {
	/**
	 * 生成验证码
	 * @param ctx
	 * @throws IOException
	 */
	public void pic(RequestContext ctx) throws IOException {
		ImageUtils.get(ctx);
	}
	
	/**
	 * 下载chrome插件
	 * @param ctx
	 * @throws IOException
	 */
	public void crx(RequestContext ctx) throws IOException {
		String path = RequestContext.root()+"files"+File.separator;
		String name = "view.crx";
		File file = new File(path+name);
		download(ctx, file, name);
	}
	
	/**
	 * 上传图片
	 * @param ctx
	 * @throws IOException
	 */
	@Annotation.PostMethod
	public void uploadImg(RequestContext ctx) throws IOException {
		File file = ctx.file("img");
		if(file==null)
			throw ctx.error("choose_a_file");
		if(!StorageUtils.isLegalImg(file.getName()))
			throw ctx.error("img_not_allow");
		String path = StorageUtils.Image.save(file);
		ctx.output_json("url", "/"+path);
	}
	
	/**
	 * 上传文档
	 * @param ctx
	 * @throws IOException
	 */
	@Annotation.UserRequired
	@Annotation.PostMethod
	public void uploadDoc(RequestContext ctx) throws IOException {
		User user = ctx.user();
		File file = ctx.file("doc");
		if(file==null)
			throw ctx.error("choose_a_file");
		if(!StorageUtils.isLegalFile(file.getName()))
			throw ctx.error("file_not_allow");
		Files f = new Files();
		f.setUser(user.getId());
		f.setCreateTime(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		String filename = ctx.param("filename", file.getName());
		f.setFilename(filename);
		f.setFilepath(StorageUtils.DOC.save(file));
		long id = f.Save();
		ctx.output_json("id", id);
	}
	/**
	 * 下载文件
	 * @param ctx
	 * @throws IOException 
	 */
	public void download(RequestContext ctx) throws IOException {
		long id = ctx.id();
		Files file = Files.INSTANCE.Get(id);
		if(file==null)
			throw ctx.error("form_empty");
		File f = StorageUtils.INSTANCE.read(file.getFilepath());
		if(f!=null) {
			download(ctx, f, file.getFilename());
			file.count();
		}
	}
	
	/**
	 * 删除文件
	 * @param ctx
	 * @throws IOException
	 */
	@Annotation.UserRequired(role=User.ROLE_MANAGER)
	public void delete(RequestContext ctx) throws IOException {
		User user = ctx.user();
		if(!user.IsManager())
			throw ctx.error("no_permission");
		long id = ctx.id();
		Files file = Files.INSTANCE.Get(id);
		if(file==null)
			throw ctx.error("form_empty");
		StorageUtils.INSTANCE.delete(file.getFilepath());
		file.Delete();
		ctx.output_json("id", file.getId());
	}
	
	/**
	 * 下载文件 <br>
	 * filename 下文要显示的文件名
	 * @param ctx
	 * @param file
	 * @param filename
	 * @throws IOException
	 */
	private void download(RequestContext ctx, File file, String filename) throws IOException{
		FileInputStream f = null;
		try {
			f = new FileInputStream(file);
			ctx.response().setContentLength((int)file.length());
			String ext = FilenameUtils.getExtension(file.getName());
			String mine_type = StorageUtils.mime_types.get(ext);
			if(mine_type != null)
				ctx.response().setContentType(mine_type);
			String ua = ctx.header("user-agent");
			if(ua != null && ua.indexOf("Firefox")>=0)
				ctx.header("Content-Disposition","attachment; filename*=\"utf8''" + LinkTool.encode_url(filename)+"\"");
			else
				ctx.header("Content-Disposition","attachment; filename=" + LinkTool.encode_url(filename));
			IOUtils.copy(f, ctx.response().getOutputStream());
		} catch (FileNotFoundException e) {
			ctx.not_found();
		}finally{
			IOUtils.closeQuietly(f);
		}
	}
}
