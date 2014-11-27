package kr.co.tmon.util.clip.vo;

import java.io.Serializable;

import com.jcraft.jsch.UserInfo;

public class ClientInfo implements UserInfo, Serializable {
	private static final long serialVersionUID = 972959269539360091L;

	private String id;
	private String password;
	private String ip;
	private int port;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "ClientInfo [id=" + id + ", password=" + password + ", ip=" + ip + ", port=" + port + "]";
	}

	@Override
	public String getPassphrase() {
		return null;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean promptPassword(String message) {
		return true;
	}

	@Override
	public boolean promptPassphrase(String message) {
		return false;
	}

	@Override
	public boolean promptYesNo(String message) {
		return true;
	}

	@Override
	public void showMessage(String message) {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
