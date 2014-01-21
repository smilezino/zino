package my.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 调用shell
 */
public class CommandUtils {
	
	/**
	 * 执行备份命令
	 * @return
	 * @throws IOException
	 */
	public static String backup() throws IOException {
		
		Properties cmd = new Properties();
		cmd.load(CommandUtils.class.getResourceAsStream("command.properties"));
		String c = cmd.getProperty("backup");
		return exec(c);
		
	}
	
	/**
	 * 执行命令,返回命令结果
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	public static String exec(String cmd){
		if(cmd==null || cmd.length()<1) {
			return "cmd error";
		}
		StringBuffer msg = new StringBuffer();
		Process process;
		try {
			process = Runtime.getRuntime().exec(cmd);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			BufferedReader br = new BufferedReader(ir);
			String m;
			while((m = br.readLine())!=null) {
				msg.append("<p>");
				msg.append(m);
				msg.append("</p>");
			}
		} catch (IOException e) {
			return e.getMessage();
		}
		
		return msg.toString();
	}
	public static void main(String[] args) throws IOException {
		System.out.println(CommandUtils.exec("df"));
	}
}
