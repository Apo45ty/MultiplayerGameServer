package udpServer;

import java.net.InetAddress;

import hello.ServerCommands;

public class TempMessageFactory extends MessageFactory {

	@Override
	public MessageObject parse(String message, InetAddress address, int port) {
		if(message.indexOf(ServerCommands.chat.commandStr+":")==0)
			return new MessageObject() {
				@Override
				public MessageType getType() {
					// TODO Auto-generated method stub
					return MessageType.Chat;
				}
				
				@Override
				public String get(String s) {
					if(s.equalsIgnoreCase("body"))
						return message.substring("chat:".length());
					if(s.equalsIgnoreCase("token"))
						return message.split("token:")[1];
					if(s.equalsIgnoreCase("ip"))
						return address.toString();
					if(s.equalsIgnoreCase("port"))
						return ""+port;
					
					return null;
				}

				@Override
				public String getMessage() {
					return message.split("token:")[0];
				}

				
			};
			else if(message.indexOf(ServerCommands.registerPort.commandStr+":")==0)
			return new MessageObject() {
				@Override
				public MessageType getType() {
					// TODO Auto-generated method stub
					return MessageType.RegisterPort;
				}
				
				@Override
				public String get(String s) {
					if(s.equalsIgnoreCase("token"))
						return message.split("token:")[1];
					if(s.equalsIgnoreCase("ip"))
						return address.toString();
					if(s.equalsIgnoreCase("port"))
						return ""+port;
					return null;
				}

				@Override
				public String getMessage() {
					return message.split("token:")[0];
				}
			};
			else if(message.contains(ServerCommands.setPosRot.commandStr+":"))
				return new MessageObject() {
					@Override
					public MessageType getType() {
						// TODO Auto-generated method stub
						return MessageType.setPosRot;
					}
					
					@Override
					public String get(String s) {
						if(s.equalsIgnoreCase("token"))
							return message.split("token:")[1];
						if(s.equalsIgnoreCase("ip"))
							return address.toString();
						if(s.equalsIgnoreCase("port"))
							return ""+port;
						return null;
					}

					@Override
					public String getMessage() {
						return message.split("token:")[0];
					}
				};
				else if(message.contains(ServerCommands.inputCommand.commandStr+":"))
					return new MessageObject() {
						@Override
						public MessageType getType() {
							// TODO Auto-generated method stub
							return MessageType.InGameInput;
						}
						
						@Override
						public String get(String s) {
							if(s.equalsIgnoreCase("token"))
								return message.split("token:")[1];
							if(s.equalsIgnoreCase("ip"))
								return address.toString();
							if(s.equalsIgnoreCase("port"))
								return ""+port;
							return null;
						}

						@Override
						public String getMessage() {
							return message.split("token:")[0];
						}
					};
		return null;
	}

}
