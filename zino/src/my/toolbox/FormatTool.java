package my.toolbox;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatTool {
	/**
	 * 格式化日期显示
	 * @param format
	 * @param date
	 * @return
	 */
	public static String date(String format, Date date) {
		SimpleDateFormat  sf = new SimpleDateFormat(format);
		return sf.format(date);
	}
	
	/**
	 * 分页数
	 * @param recordCount
	 * @param perPage
	 * @return
	 */
	public static int pageCount(long recordCount, int perPage) {
		int pc = (int)Math.ceil(recordCount / (double)perPage);
		return (pc==0)?1:pc;
	}
	public static int pageCount(long recordCount, long perPage) {
		int pc = (int)Math.ceil(recordCount / (double)perPage);
		return (pc==0)?1:pc;
	}
	public static int pageCount(int recordCount, long perPage) {
		int pc = (int)Math.ceil(recordCount / (double)perPage);
		return (pc==0)?1:pc;
	}
	public static int pageCount(int recordCount, int perPage) {
		int pc = (int)Math.ceil(recordCount / (double)perPage);
		return (pc==0)?1:pc;
	}
}
