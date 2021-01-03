package games.apolion.udp.server.messages;

public interface MessageObject {
	MessageType getType();
	String get(String s);
	String getMessage();
}
