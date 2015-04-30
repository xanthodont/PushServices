package request.writer;

import java.sql.PreparedStatement;
import java.sql.SQLException;



public class ConnectWriter extends DBWriter<ConnectInfo> {
	// 单例形式
	public static ConnectWriter getInstance() {
		return InstanceHolder.instance;
	}
	static class InstanceHolder {
		static ConnectWriter instance = new ConnectWriter();
	}
	
	private final static String SQL_STATEMENT = "call pro_update_pushuser(?, ?, ?, ?, ?);"; 

	private ConnectWriter() {
		super(SQL_STATEMENT);
	}

	@Override
	protected void setPrepareStatement(PreparedStatement ps, ConnectInfo info) throws SQLException {
		long ct = System.currentTimeMillis();
		//Date now = new Date();
		ps.setString(1, info.getUsername());
		ps.setString(2, info.getPassword());
		ps.setLong(3, ct);
		ps.setLong(4, ct);
		ps.setString(5, info.getResource());
		
		ps.addBatch();
	}

}
