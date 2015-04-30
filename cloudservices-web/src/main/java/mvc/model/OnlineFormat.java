package mvc.model;

public class OnlineFormat {

	private String status;
	private long updateTime;
	private String circle;

	public OnlineFormat(String status, long updateTime, String circle) {
		this.status = status;
		this.updateTime = updateTime;
		this.circle = circle;
	}
	
	public static OnlineFormat parseValue(String value) {
		String[] as = value.split("_");
		if (as.length != 3) {
			return new OnlineFormat("0", System.currentTimeMillis(), "30");
		}
		return new OnlineFormat(as[0], Long.parseLong(as[1]), as[2]);
	}

	@Override
	public String toString() {
		return String.format("%s_%d_%s", status, updateTime, circle);
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}
}
