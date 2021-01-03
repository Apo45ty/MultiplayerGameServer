package games.apolion.http.authentication;

import games.apolion.http.authentication.dtos.TokenDTO;
import games.apolion.http.persistance.Users;
import games.apolion.udp.server.UDPGameServer;

public class Session {
	public TokenDTO t;
	public Users u;
	public UDPGameServer serverInstanceJoined;
}
