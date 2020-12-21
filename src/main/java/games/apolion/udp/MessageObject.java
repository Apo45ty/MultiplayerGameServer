package games.apolion.udp;

public interface MessageObject {
	MessageType getType();
	String get(String s);
	String getMessage();
}
