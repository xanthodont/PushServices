package request.writer;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import cloudservices.client.packets.Packet;

public class SendMessageWriter extends DBWriter<MessageInfo>{
	private static SendMessageWriter instance = new SendMessageWriter();
	public static SendMessageWriter getInstance() {
		return instance;
	}
	
	private final static String SQL_STATEMENT = "call pro_update_pushmessage(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"; 

	private SendMessageWriter() {
		super(SQL_STATEMENT);
	}

	@Override
	protected void setPrepareStatement(PreparedStatement ps, MessageInfo info)
			throws SQLException {
		// TODO Auto-generated method stub
		if (info.getPacketType() != Packet.SUB) {
			long ct = System.currentTimeMillis();
			ps.setString(1, info.getUsername());
			ps.setString(2, info.getPublic2Topic());
			ps.setInt(3, info.getMessageId());
			ps.setInt(4, info.getPacketType());
			ps.setBoolean(5, info.isAck());
			ps.setBoolean(6, info.isSub());
			ps.setInt(7, info.getTotal());
			ps.setInt(8, info.getNo());
			ps.setBoolean(9, info.isStatus());
			ps.setLong(10, ct);
			ps.setLong(11, ct);
			ps.setString(12, info.getDescription());
			
			ps.addBatch();
		}
	}

}
