package games.apolion.http.authentication;

import games.apolion.http.persistance.Users;
import games.apolion.udp.UDPGameServer;

public class Session {
	public Token t;
	public Users u;
	public UDPGameServer serverInstanceJoined;
}
