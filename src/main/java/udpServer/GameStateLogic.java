package udpServer;

import java.net.DatagramSocket;
import java.util.List;
import java.util.Queue;

public interface GameStateLogic {
	public void tick( UDPGameServer owner);
}
