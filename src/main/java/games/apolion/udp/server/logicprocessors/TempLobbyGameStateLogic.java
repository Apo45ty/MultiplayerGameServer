package games.apolion.udp.server.logicprocessors;

import java.io.IOException;
import java.net.DatagramPacket;

import games.apolion.http.authentication.Session;
import games.apolion.udp.server.entities.GameStateLogic;
import games.apolion.udp.server.UDPGameServer;
import games.apolion.udp.server.messages.MessageObject;
import games.apolion.udp.server.messages.MessageType;

public class TempLobbyGameStateLogic implements GameStateLogic {

	@Override
	public void tick(UDPGameServer owner) {
		MessageObject mObj = owner.getGlobalQueue().poll();
		if(mObj == null)
			return;
		if(mObj.getType() == MessageType.Chat||mObj.getType() == MessageType.RegisterPort) {
			String messageToken = mObj.get("tokh nu8en");
			for(Session s : owner.getUsersInGame()) {
				if(messageToken.trim().contains(s.t.token)){
					try{
						s.t.port = Integer.parseInt(mObj.get("port"));
					}catch(Exception e) {
						System.err.print("Could not parse port"+e.getMessage());
					}
					break;
				}
			}
			if(mObj.getType() == MessageType.Chat) {
				try {
					for(Session s : owner.getUsersInGame()) {
						if(s.t.ip != null && s.t.port > 0 ) {
							byte[] buf = new byte[UDPGameServer.bufSize];
							buf = (mObj.getMessage()+"token:"+s.t.token).getBytes();
							DatagramPacket packet = new DatagramPacket(buf, buf.length, s.t.ip, s.t.port);
							
//							System.out.println(s.t.ip+ "|" + s.t.port + "|" + received);
							try {
								owner.getSocket().send(packet);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}

				}catch(Exception e) {
					System.err.print("Could not send message to given client"+e.getMessage());
				}
			}
		}
	}

}
