package mvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import cloudservices.client.packets.Packet;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="push_message")
public class PushMessage {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="username", length=23)
	private String username;
	
	@Column(name="topic", length=512)
	private String topic;
	
	@Column(name="messageId")
	private int messageId;
	
	@Column(name="type")
	private int type;
	@Transient
	private String packetType;
	
	
	@Column(name="ack")
	private boolean ack;
	
	@Column(name="sub")
	private boolean sub;
	
	@Column(name="total")
	private int total;
	
	@Column(name="no")
	private int no;
	
	@Column(name="status")
	private boolean status;
	
	@Column(name="payload", length=1024)
	private String payload;
	
	@Column(name="createTime")
	private long createTime;
	
	@Column(name="updateTime")
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isAck() {
		return ack;
	}

	public void setAck(boolean ack) {
		this.ack = ack;
	}

	public boolean isSub() {
		return sub;
	}

	public void setSub(boolean sub) {
		this.sub = sub;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getPacketType() {
		switch (type) {
		case Packet.TEXT:
			return "TEXT"; 
		case Packet.FILE:
			return "FILE";
		case Packet.HTTP:
			return "HTTP";
		case Packet.ACK: 
			return "ACK";
		case Packet.SUB: 
			return "SUB";	
		default: 
			return "UNDEFINED";
		}
	}

	public void setPacketType(String packetType) {
		this.packetType = packetType;
	}
	
	
	
}
