package udpServer;

public interface MessageObject {
	MessageType getType();
	String get(String s);
	String getMessage();
}
