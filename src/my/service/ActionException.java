package my.service;

/**
 * action异常
 * @author smile
 * @date 2013-2-18 下午11:34:14
 */
public class ActionException extends RuntimeException {

	private String key;
	
	public ActionException(String message){
		super(message);
	}
	
	public ActionException(String key, String message){
		super(message);
		this.key = key;
	}
	
	public String getKey(){
		return this.key;
	}
}
