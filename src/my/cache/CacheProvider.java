package my.cache;

/**
 * 
 * @author smile
 * @date 2013-2-18 下午4:45:03
 */
public interface CacheProvider {
	public Cache buildCache(String regionName, boolean autoCreate) throws CacheException;
	public void start() throws CacheException;
	public void stop() throws CacheException;
}
