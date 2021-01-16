package games.apolion.udp.server.messages;

import java.net.InetAddress;

public class MessageObjectTempImp implements MessageObject{
    String message; InetAddress address; int port; ServerCommands type;

    public MessageObjectTempImp(String message, InetAddress address, int port, ServerCommands type) {
        this.message = message;
        this.address = address;
        this.port = port;
        this.type = type;
    }

    @Override
    public ServerCommands getType() {
        return type;
    }

    @Override
    public String get(String s) {
        if(s.equalsIgnoreCase("body"))
            return message.substring("chat:".length());
        if(s.equalsIgnoreCase("token"))
            return message.split(TempMessageFactory.recieverTokenDelimiter)[1];
        if(s.equalsIgnoreCase("ip"))
            return address.toString();
        if(s.equalsIgnoreCase("port"))
            return ""+port;

        return null;
    }

    @Override
    public String getMessage() {
        return message.split(TempMessageFactory.recieverTokenDelimiter)[0];
    }


}
