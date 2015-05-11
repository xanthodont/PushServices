package cloudservices.client;

public interface ConnectionListener {
	//public void connectionClosedOnError(Exception e);
	public void configSuccess(ClientConfiguration config);
	public void startWriterAndReader();
	public void connectionSuccessful();
	public void connectionFail(ConnectException e);
	public void reconnectStart(long reconnectTimes, long reconnectDelay);
	public void connectionClosed();
}
