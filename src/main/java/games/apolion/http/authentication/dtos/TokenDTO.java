package games.apolion.http.authentication.dtos;

import java.net.InetAddress;
import java.util.Date;

public class TokenDTO {
	public String token;
	public Date ExpirationDate;
	public InetAddress ip;
	public int port = -1;
	public int chatPort = -1;
	public int getChatPort() {
		return chatPort;
	}

	public void setChatPort(int chatPort) {
		this.chatPort = chatPort;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpirationDate() {
		return ExpirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		ExpirationDate = expirationDate;
	}

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public TokenDTO(String token) {
		super();
		this.token = token;
		Date temp = new Date();
		ExpirationDate = new Date(temp.getYear(), temp.getMonth(), temp.getDay(), temp.getHours() + 1, temp.getMinutes());
	}
	
}
