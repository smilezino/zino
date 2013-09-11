package my.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import my.toolbox.LinkTool;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import my.service.RequestContext;
import my.utils.ImageUtils;

/**
 * 下载文件
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
		download(ctx, file, path, name);
	}
	
	public void uploadImg(RequestContext ctx) throws IOException {
		ctx.output_json("url", "uploads/asd.jpg");
	}
	
	private void download(RequestContext ctx,File file, String path, String filename) throws IOException{
		FileInputStream f = null;
		try {
			f = new FileInputStream(file);
			ctx.response().setContentLength((int)file.length());
			String ext = FilenameUtils.getExtension(path);
			String mine_type = mime_types.get(ext);
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
	@SuppressWarnings("serial")
	public final static HashMap<String, String> mime_types = new HashMap<String, String>()
			{{
				put("jar","application/java-archive");
				put("jad","text/vnd.sun.j2me.app-descriptor");
				put("sis","application/vnd.symbian.install");
				put("sisx","x-epoc/x-sisx-app");
				put("thm","application/vnd.eri.thm");
				put("nth","application/vnd.nok-s40theme");
				put("zip","application/zip");
				put("rar","application/octet-stream");
				put("cab","application/octet-stream");
				put("gz","application/x-gzip");
				put("bz2","application/bzip2");
				put("tar","application/x-tar");
				
				put("gif","image/gif");
				put("jpg","image/jpeg");
				put("jpeg","image/jpeg");
				put("png","image/png");
				put("bmp","image/bmp");

				put("avi","video/x-msvideo");
				put("rm","application/vnd.rn-realmedia"); 
				put("3gp","video/3gpp");
				put("wmv","video/x-ms-wmv");
				put("mpg","video/mpg");
				put("asf","video/x-ms-asf");
				put("flv","video/x-flv");
				put("mp4","video/mp4");

				put("wma","audio/x-ms-wma"); 
				put("mp3","audio/mp3");
				put("arm","audio/amr");
				put("mid","audio/x-midi");
				put("aac","audio/aac");
				put("imy","audio/imelody");

				put("swf", "application/x-shockwave-flash");

				put("txt","text/plain");
				put("htm","text/html");
				put("html","text/html");
				put("pdf","application/pdf");
				put("doc","application/msword");
				put("rtf","application/msword");
				put("docx","application/msword");
				put("xls","application/vnd.ms-excel");
				put("ppt","application/vnd.ms-powerpoint");
				put("pps","application/vnd.ms-pps");
				put("xlsx","application/vnd.ms-excel");
				put("pptx","application/vnd.ms-powerpoint");
				put("chm","application/octet-stream");
			}};
}
