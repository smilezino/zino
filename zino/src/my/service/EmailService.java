package my.service;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import my.utils.RegexUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class EmailService {
	private final static Log log = LogFactory.getLog(EmailService.class);
	private final static Properties smtp = new Properties();
	
	static {
		try {
			smtp.load(EmailService.class.getResourceAsStream("smtp.properties"));
		} catch(Exception e) {
			throw new RuntimeException("Unabled to load smtp.properties",e);
		}
	}
	
	/**
	 * 发送邮件
	 * @param email
	 * @param title
	 * @param text
	 */
	public static void send(String email, String title, String text) {
		try {
			HtmlEmail body = (HtmlEmail)NewEmail(Arrays.asList(email), true);
			body.setSubject(title);
			body.setHtmlMsg(text);
			body.send();
		} catch (Exception e) {
			throw new RuntimeException("Unabled to send mail", e);
		}
	}
	public static void sendText(String email, String title, String text) {
		try {
			HtmlEmail body = (HtmlEmail)NewEmail(Arrays.asList(email), false);
			body.setSubject(title);
			body.setHtmlMsg(text);
			body.send();
		} catch (Exception e) {
			throw new RuntimeException("Unabled to send mail", e);
		}
	}
	
	/**
	 * 初始化邮件
	 * @param emails
	 * @param html
	 * @return
	 * @throws EmailException
	 */
	private final static Email NewEmail(List<String> emails, boolean html) throws EmailException {
		Email body = html?new HtmlEmail():new SimpleEmail();
		body.setCharset("UTF-8");
		body.setHostName(smtp.getProperty("hostname"));
		body.setSmtpPort(NumberUtils.toInt(smtp.getProperty("port"), 25));
		body.setSSL("true".equalsIgnoreCase(smtp.getProperty("ssl")));
		body.setAuthentication(smtp.getProperty("username"), smtp.getProperty("password"));
		String[] senders = StringUtils.split(smtp.getProperty("sender"),':');
		body.setFrom(senders[1], senders[0]);
		for(String m : emails){
			if(RegexUtils.isEmail(m))
				body.addTo(m);	
		}
		return body;
	}
	public static void main(String[] args) {
		EmailService.send("619828889@qq.com", "test", "test");
	}
}
