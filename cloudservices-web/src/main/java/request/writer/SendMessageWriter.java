package request.writer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SendMessageWriter extends DBWriter<MessageInfo>{
	private final static String SQL_STATEMENT = "call pro_update_pushuser(?, ?, ?, ?, ?);"; 

	public SendMessageWriter(String sqlStatement) {
		super(sqlStatement);
	}

	@Override
	protected void setPrepareStatement(PreparedStatement ps, MessageInfo info)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
