package my.beans;


import java.sql.Timestamp;

import my.db.DBbean;

/**
 * 文件存储
 * @author smile
 *
 */
public class Files extends DBbean {
	public static final transient Files INSTANCE = new Files();
	
	public static final byte STATUS_LOCK = 0x01; //不允许普通用户下载
	public static final byte STATUS_UNLOCK = 0x00;//公开的，可下载

	private long user;
	private String filename;
	private String filepath;
	private int downloadCount;
	private byte type;
	private byte status;
	private Timestamp createTime;
	
	/**
	 * 文件状态提示
	 * @param s
	 * @return
	 */
	public static String status(int s) {
		switch(s){
		case Files.STATUS_LOCK:
			return "私有";
		case Files.STATUS_UNLOCK:
		default :
			return "公开";
		}
	}
	/**
	 * 文件下载次数+1
	 */
	public void count() {
		UpdateField("downloadCount", downloadCount+1);
	}
	
	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public int getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(int downloadCount) {
		this.downloadCount = downloadCount;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	@Override
	protected String TableName() {
		return "z_files";
	}
	
}
