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
}
