package com.trendy.fw.common.cache.memcached;

public class MemcacheConfigBean {
	private String appId = "";
	private String[] servers;// 缓存服务器列表，组成：ip+"："+端口号，多个用半角逗号分隔
	private Integer[] weights;// 缓存服务器权重，多个用半角逗号分隔
	private int initConn = 0;// 初始化连接数
	private int minConn = 0;// 最小连接数
	private int maxConn = 0;// 最大连接数
	private long maxIdle = 0;// 最大空闲时间
	private long maxBusyTime = 0;// 最大处理时间
	private int maintSleep = 0;// 主线程最大睡眠时间
	private int socketTimeOut = 0;// Socket超时
	private int socketConnectTimeOut = 0;// Socket连接超时
	private boolean nagle = false;// TCP算法
	private boolean failover = false;// 当某台服务器链接失效时，是否自动访问到另外一台
	private boolean failback = false;// 当该台服务器回复时，是否自动恢复
	private boolean aliveCheck = true;// 检查服务器状态

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String[] getServers() {
		return servers;
	}

	public void setServers(String[] servers) {
		this.servers = servers;
	}

	public Integer[] getWeights() {
		return weights;
	}

	public void setWeights(Integer[] weights) {
		this.weights = weights;
	}

	public int getInitConn() {
		return initConn;
	}

	public void setInitConn(int initConn) {
		this.initConn = initConn;
	}

	public int getMinConn() {
		return minConn;
	}

	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}

	public int getMaxConn() {
		return maxConn;
	}

	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	public long getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(long maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getMaxBusyTime() {
		return maxBusyTime;
	}

	public void setMaxBusyTime(long maxBusyTime) {
		this.maxBusyTime = maxBusyTime;
	}

	public int getMaintSleep() {
		return maintSleep;
	}

	public void setMaintSleep(int maintSleep) {
		this.maintSleep = maintSleep;
	}

	public int getSocketTimeOut() {
		return socketTimeOut;
	}

	public void setSocketTimeOut(int socketTimeOut) {
		this.socketTimeOut = socketTimeOut;
	}

	public int getSocketConnectTimeOut() {
		return socketConnectTimeOut;
	}

	public void setSocketConnectTimeOut(int socketConnectTimeOut) {
		this.socketConnectTimeOut = socketConnectTimeOut;
	}

	public boolean getNagle() {
		return nagle;
	}

	public void setNagle(boolean nagle) {
		this.nagle = nagle;
	}

	public boolean getFailover() {
		return failover;
	}

	public void setFailover(boolean failover) {
		this.failover = failover;
	}

	public boolean getFailback() {
		return failback;
	}

	public void setFailback(boolean failback) {
		this.failback = failback;
	}

	public boolean getAliveCheck() {
		return aliveCheck;
	}

	public void setAliveCheck(boolean aliveCheck) {
		this.aliveCheck = aliveCheck;
	}
}
