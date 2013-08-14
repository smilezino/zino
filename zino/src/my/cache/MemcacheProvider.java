package my.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.danga.MemCached.Logger;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

public class MemcacheProvider implements CacheProvider{
	private static final Log log = LogFactory.getLog(MemcacheProvider.class);   
	  
	private final static String DEFAULT_REGION_NAME = "____DEFAULT_CACHE_REGION";
	private final static String CACHE_IDENT 	= "cache.";
	private final static String SERVERS_CONF 	= "servers";

	private Hashtable<String, MemCache> _CacheManager;
	private Properties _cache_properties = new Properties();

	/* (non-Javadoc)
	 * @see org.hibernate.cache.CacheProvider#start(java.util.Properties)
	 */
    @SuppressWarnings({ "rawtypes" })
	public void start() throws CacheException {		
		String conf = "/memcached.properties";
		InputStream in = getClass().getResourceAsStream(conf);
		Properties memcached_conf = new Properties();
		try {
			memcached_conf.load(in);
		} catch (IOException e) {
			throw new CacheException("Unabled to load properties from "+ conf, e);
		} finally{
			IOUtils.closeQuietly(in);
		}
		
		String servers = memcached_conf.getProperty(SERVERS_CONF);
		if(StringUtils.isBlank(servers)){
			throw new CacheException("configuration 'memcached.servers' get a empty value");
		}
		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers( servers.split(",") );
				
		Properties base_conf = (Properties)memcached_conf.clone();
		base_conf.remove(SERVERS_CONF);
		Iterator keys = base_conf.keySet().iterator();
		while(keys.hasNext()){
			String key = (String)keys.next();
			if(key.startsWith(CACHE_IDENT)){
				_cache_properties.put(key.substring(CACHE_IDENT.length()), base_conf.getProperty(key));
				keys.remove();
			}
		}
		try {
			BeanUtils.populate(pool, base_conf);
		} catch (Exception e) {
			throw new CacheException("Unabled to set properties to SockIOPool", e);
		}
		
		pool.initialize();

		Logger.getLogger( MemCachedClient.class.getName() ).setLevel( Logger.LEVEL_WARN );

		_CacheManager = new Hashtable<String, MemCache>();
	}

	/* (non-Javadoc)
	 * @see org.hibernate.cache.CacheProvider#buildCache(java.lang.String, java.util.Properties)
	 */
	public MemCache buildCache(String name, boolean autoCreate) throws CacheException {
		if(StringUtils.isEmpty(name))
			name = DEFAULT_REGION_NAME;
		MemCache mCache = _CacheManager.get(name);
		if(mCache == null && autoCreate){
			String timeToLive = _cache_properties.getProperty(name);
			int secondToLive = -1;
			if(StringUtils.isNotBlank(timeToLive)){
				timeToLive = timeToLive.toLowerCase().trim();
				secondToLive = _GetSeconds(timeToLive);
			}
			log.info("Building cache named "+name+" using secondToLive is "+secondToLive);
			mCache = new MemCache(name, secondToLive);
			_CacheManager.put(name, mCache);
		}
		return mCache;
	}
	
	private static int _GetSeconds(String str){
		try{
			switch(str.charAt(str.length()-1)){
			case 's':
				return Integer.parseInt(str.substring(0, str.length()-1));
			case 'm':
				return Integer.parseInt(str.substring(0, str.length()-1)) * 60;
			case 'h':
				return Integer.parseInt(str.substring(0, str.length()-1)) * 3600;
			case 'd':
				return Integer.parseInt(str.substring(0, str.length()-1)) * 86400;
			default:
				return Integer.parseInt(str);
			}
		}catch(NumberFormatException e){
			log.warn("Illegal configuration value : " + str, e);
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see org.hibernate.cache.CacheProvider#stop()
	 */
	public void stop() {
	}
}
