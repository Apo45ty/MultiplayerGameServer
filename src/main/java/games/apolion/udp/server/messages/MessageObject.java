package games.apolion.udp.server.messages;

public interface MessageObject {
	ServerCommands getType();
	String get(String s);
	String getMessage();
}
