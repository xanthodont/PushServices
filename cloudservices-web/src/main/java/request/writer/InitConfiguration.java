package request.writer;

public class InitConfiguration {
	private int cacheQueueSize;
	private int cacheQueueThreadSize;
	
	private InitConfiguration() {}
	
	private static class InstanceHolder {
		private static InitConfiguration instance = new InitConfiguration();
	}
	
	public static InitConfiguration getInstance() {
		return InstanceHolder.instance;
	}

	public int getCacheQueueSize() {
		return cacheQueueSize;
	}

	public void setCacheQueueSize(int cacheQueueSize) {
		this.cacheQueueSize = cacheQueueSize;
	}

	public int getCacheQueueThreadSize() {
		return cacheQueueThreadSize;
	}

	public void setCacheQueueThreadSize(int cacheQueueThreadSize) {
		this.cacheQueueThreadSize = cacheQueueThreadSize;
	}
	
}
