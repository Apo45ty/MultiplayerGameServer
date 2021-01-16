package games.apolion.udp.server.messages;

import java.net.InetAddress;

public class TempMessageFactory extends MessageFactory {

    public static String SenderTokenDelimiter = "Stoken:";
    public static String recieverTokenDelimiter = "token:";

    @Override
    public MessageObject parse(String message, InetAddress address, int port) {
        if (message.indexOf(ServerCommands.chat.commandStr + ":") == 0)
            return new MessageObjectTempImp(message, address, port, ServerCommands.chat);
        else if (message.indexOf(ServerCommands.registerPort.commandStr + ":") == 0)
			return new MessageObjectTempImp(message, address, port, ServerCommands.registerPort);
        else if (message.contains(ServerCommands.setPosRot.commandStr + ":"))
            return new MessageObjectTempImp(message, address, port, ServerCommands.setPosRot);
        else if (message.contains(ServerCommands.inputCommand.commandStr + ":"))
			return new MessageObjectTempImp(message, address, port, ServerCommands.inputCommand);
        return null;
    }

}
