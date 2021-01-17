package games.apolion.http.authentication.dtos;

import java.net.InetAddress;
import java.util.Date;

public class TokenDTO {
	public String username;
	public String token;
	public Date expirationDate;
	public InetAddress ip;
	public int port = -1;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		expirationDate = expirationDate;
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
		expirationDate = new Date(temp.getYear(), temp.getMonth(), temp.getDay(), temp.getHours() + 1, temp.getMinutes());
	}
	public TokenDTO(){}

	public TokenDTO(String token, Date expirationDate, InetAddress ip, int port, int chatPort) {
		this.token = token;
		this.expirationDate = expirationDate;
		this.ip = ip;
		this.port = port;
	}

	public TokenDTO(String username, String token, Date expirationDate, InetAddress ip, int port) {
		this.username = username;
		this.token = token;
		this.expirationDate = expirationDate;
		this.ip = ip;
		this.port = port;
	}
}
