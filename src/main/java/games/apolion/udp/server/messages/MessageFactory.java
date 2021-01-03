package games.apolion.udp.server.messages;

import games.apolion.udp.server.messages.MessageObject;

import java.net.InetAddress;

public abstract class MessageFactory {
	
	public abstract MessageObject parse(String message, InetAddress address, int port);
}
