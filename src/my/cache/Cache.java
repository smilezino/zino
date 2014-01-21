package my.cache;

import java.util.List;

/**
 * 
 * @author smile
 * @date 2013-2-19 下午3:59:17
 */
public interface Cache {
	public Object get(Object key) throws CacheException;
	public void put(Object key, Object value) throws CacheException;
	public void update(Object key, Object value) throws CacheException;
	@SuppressWarnings("rawtypes")
	public List keys() throws CacheException;
	public void remove(Object key) throws CacheException;
	public void clear() throws CacheException;
	public void destroy() throws CacheException;
}
