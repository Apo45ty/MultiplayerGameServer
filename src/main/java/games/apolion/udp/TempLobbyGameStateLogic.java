package games.apolion.udp;

import java.io.IOException;
import java.net.DatagramPacket;

import games.apolion.http.authentication.Session;

public class TempLobbyGameStateLogic implements GameStateLogic {

	@Override
	public void tick(UDPGameServer owner) {
		MessageObject mObj = owner.getGlobalQueue().poll();
		if(mObj == null)
			return;
		if(mObj.getType() == MessageType.Chat||mObj.getType() == MessageType.RegisterPort) {
			String messageToken = mObj.get("token");
			if(messageToken.trim().contains(owner.getHost().t.token)){
				try{
					owner.getHost().t.port = Integer.parseInt(mObj.get("port"));
				}catch(Exception e) {
					System.err.print("Could not parse port"+e.getMessage());
				}
			}
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
					if(owner.getHost().t.ip != null && owner.getHost().t.port > 0 ) {
						byte[] buf = new byte[UDPGameServer.bufSize];
						buf = (mObj.getMessage()+"token:"+owner.getHost().t.token).getBytes();
						DatagramPacket packet = new DatagramPacket(buf, buf.length, owner.getHost().t.ip, owner.getHost().t.port);
						
//						System.out.println(s.t.ip+ "|" + s.t.port + "|" + received);
						try {
							owner.getSocket().send(packet);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}catch(Exception e) {
					System.err.print("Could not send message to given client"+e.getMessage());
				}
			}
		}
	}

}
