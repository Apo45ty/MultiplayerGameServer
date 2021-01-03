package games.apolion.udp.server.entities;

import games.apolion.udp.server.UDPGameServer;

public interface GameStateLogic {
	public void tick( UDPGameServer owner);
}
