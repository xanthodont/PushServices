package mvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.junit.Ignore;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Push 用户表
 * 
 * @author xanthodont
 *
 */
@Entity
@Table(name="push_user")
public class PushUser {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	@Column(name="username", length=23)
	private String username;
	
	@JsonIgnore
	@Column(name="password", length=20)
	private String password;
	
	@Column(name="registerTime")
	private long registerTime;
	
	@Column(name="updateTime")
	private long updateTime;
	
	@Column(name="resource", length=50)
	private String resource;
	
	@Transient
	private int status;
	
	

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(long registerTime) {
		this.registerTime = registerTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	
}	
