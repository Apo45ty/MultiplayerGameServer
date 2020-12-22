package games.apolion.http.dtos;

import java.util.List;

import games.apolion.http.persistance.Users;
import games.apolion.udp.GameServerStates;

public class GameDescriptorDTO {
	public String serverName;
	public int port;
	public String ip;
	public Iterable<String> users;
	public String host;
	public GameServerStates state;
	public String errorMessage;
	public GameDescriptorDTO() {
		// TODO Auto-generated constructor stub
	}
	public GameDescriptorDTO(String serverName, int port, String ip, Iterable<String> users, String host) {
		super();
		this.serverName = serverName;
		this.port = port;
		this.ip = ip;
		this.users = users;
		this.host=host;
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(Users host) {
		this.host = host.getUsername();
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Iterable<String> getUsers() {
		return users;
	}
	public void setUsers(Iterable<String> users) {
		this.users = users;
	}
}
