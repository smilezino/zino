package my.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
/**
 * 数据库管理
 * @author smile
 * @date 2013-2-5 上午10:30:47
 */
public class DBmanager {
	private final static ThreadLocal<Connection> conns = new ThreadLocal<Connection>();
	private static DataSource dataSource;
	
	static {
		initDataSource();
	}
	
	/**
	 * 数据库连接初始化
	 */
	private final static void initDataSource() {
		try {
			Properties dbProperties = new Properties();
			dbProperties.load(DBmanager.class.getResourceAsStream("db.properties"));
			Properties cp_props = new Properties();
			for(Object key : dbProperties.keySet()) {
				String k = (String) key;
				if(k.startsWith("jdbc.")) {
					String name = k.substring(5);
					cp_props.put(name, dbProperties.getProperty(k));
				}
			}
			
			dataSource = (DataSource) Class.forName(cp_props.getProperty("datasource")).newInstance();
			if(dataSource.getClass().getName().indexOf("c3p0")>0){
				//Disable JMX in C3P0
				System.setProperty("com.mchange.v2.c3p0.management.ManagementCoordinator", 
						"com.mchange.v2.c3p0.management.NullManagementCoordinator");
			}
			BeanUtils.populate(dataSource, cp_props);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DBException(e);
		}
	}
	/**
	 * 获取一连接
	 * @return
	 */
	public final static Connection getConnection() {
		Connection conn = conns.get();
		try {
			if(conn==null || conn.isClosed()) {
				conn = dataSource.getConnection();
				conns.set(conn);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}
		return conn;
	}
	/**
	 * 关闭数据库连接
	 */
	public final static void closeConnection(){
		Connection conn = conns.get();
		try {
			if(conn!=null && !conn.isClosed()) {
				conn.setAutoCommit(true);
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e);
		}
		conns.set(null);
	}
}
