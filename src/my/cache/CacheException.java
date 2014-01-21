package my.cache;

/**
 * 缓存异常
 * @author smile
 * @date 2013-2-18 下午4:40:30
 */
public class CacheException extends RuntimeException {

	public CacheException(String s) {
		super(s);
	}

	public CacheException(String s, Throwable e) {
		super(s, e);
	}

	public CacheException(Throwable e) {
		super(e);
	}

}
