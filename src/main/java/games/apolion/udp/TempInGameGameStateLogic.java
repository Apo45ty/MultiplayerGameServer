package games.apolion.udp;

import java.io.IOException;
import java.net.DatagramPacket;

import games.apolion.http.authentication.Session;

public class TempInGameGameStateLogic implements GameStateLogic {

	@Override
	public void tick(UDPGameServer owner) {
		MessageObject mObj = owner.getGlobalQueue().poll();
		if (mObj == null)
			return;
		if (mObj.getType() == MessageType.RegisterPort) {
			//Settup game port
			String messageToken = mObj.get("token");
			if (messageToken.trim().contains(owner.getHost().t.token)) {
				try {
					owner.getHost().t.port = Integer.parseInt(mObj.get("port"));
				} catch (Exception e) {
					System.err.print("Could not parse port" + e.getMessage());
				}
			} else
				for (Session s : owner.getUsersInGame()) {
					if (messageToken.trim().contains(s.t.token)) {
						try {
							s.t.port = Integer.parseInt(mObj.get("port"));
						} catch (Exception e) {
							System.err.print("Could not parse port" + e.getMessage());
						}
						break;
					}
				}
		} else if (mObj.getType() == MessageType.Chat) {
			///ADD CHAT PORT which is different from game port
			String messageToken = mObj.get("token");
			if (messageToken.trim().contains(owner.getHost().t.token)) {
				try {
					owner.getHost().t.setChatPort(Integer.parseInt(mObj.get("port")));
				} catch (Exception e) {
					System.err.print("Could not parse port" + e.getMessage());
				}
			} else
				for (Session s : owner.getUsersInGame()) {
					if (messageToken.trim().contains(s.t.token)) {
						try {
							s.t.setChatPort(Integer.parseInt(mObj.get("port")));
						} catch (Exception e) {
							System.err.print("Could not parse port" + e.getMessage());
						}
						break;
					}
				}
			try {
				for (Session s : owner.getUsersInGame()) {
					if (s.t.ip != null && s.t.getChatPort() > 0) {
						byte[] buf = new byte[UDPGameServer.bufSize];
						buf = (mObj.getMessage() + "token:" + s.t.token).getBytes();
						DatagramPacket packet = new DatagramPacket(buf, buf.length, s.t.ip, s.t.getChatPort());

//							System.out.println(s.t.ip+ "|" + s.t.port + "|" + received);
						try {
							owner.getSocket().send(packet);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				if (owner.getHost() != null && owner.getHost().t.ip != null && owner.getHost().t.getChatPort() > 0) {
					byte[] buf = new byte[UDPGameServer.bufSize];
					buf = (mObj.getMessage() + "token:" + owner.getHost().t.token).getBytes();
					DatagramPacket packet = new DatagramPacket(buf, buf.length, owner.getHost().t.ip,
							owner.getHost().t.getChatPort());

//						System.out.println(s.t.ip+ "|" + s.t.port + "|" + received);
					try {
						owner.getSocket().send(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				System.err.print("Could not send message to given client" + e.getMessage());
			}

		} else if (mObj.getType() == MessageType.setPosRot) {
			try {
				for (Session s : owner.getUsersInGame()) {
					if (s.t.ip != null && s.t.port > 0) {
						byte[] buf = new byte[UDPGameServer.bufSize];
						buf = (mObj.getMessage() + "token:" + s.t.token).getBytes();
						DatagramPacket packet = new DatagramPacket(buf, buf.length, s.t.ip, s.t.port);
//							System.out.println(s.t.ip+ "|" + s.t.port + "|" + received);
						try {
							owner.getSocket().send(packet);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				System.err.print("Could not send message to given client" + e.getMessage());
			}
		} else if (mObj.getType() == MessageType.InGameInput) {
			try {
				if (owner.getHost().t.ip != null && owner.getHost().t.port > 0) {
					
					byte[] buf = new byte[UDPGameServer.bufSize];
					buf = (mObj.getMessage() + "token:" + owner.getHost().t.token).getBytes();
					DatagramPacket packet = new DatagramPacket(buf, buf.length, owner.getHost().t.ip,
							owner.getHost().t.port);

//						System.out.println(s.t.ip+ "|" + s.t.port + "|" + received);
					try {
						owner.getSocket().send(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				System.err.print("Could not send message to given client" + e.getMessage());
			}
		}
	}

}
