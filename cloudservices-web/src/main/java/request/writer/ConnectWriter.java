package request.writer;

import java.sql.PreparedStatement;
import java.sql.SQLException;



public class ConnectWriter extends DBWriter<ConnectInfo>{
	private final static String SQL_STATEMENT = "call pro_update_pushuser(?, ?, ?, ?, ?);"; 

	public ConnectWriter() {
		super(SQL_STATEMENT);
	}

	@Override
	protected void setPrepareStatement(PreparedStatement ps, ConnectInfo info) throws SQLException {
		long ct = System.currentTimeMillis();
		ps.setString(1, info.getUsername());
		ps.setString(2, info.getPassword());
		ps.setLong(3, ct);
		ps.setLong(4, ct);
		ps.setString(5, info.getResource());
		
		ps.addBatch();
	}

}
