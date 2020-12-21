package games.apolion.udp;

import java.net.InetAddress;

public abstract class MessageFactory {
	
	public abstract MessageObject parse(String message, InetAddress address, int port);
}
