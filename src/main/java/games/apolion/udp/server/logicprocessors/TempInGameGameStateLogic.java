package games.apolion.udp.server.logicprocessors;

import java.io.IOException;
import java.net.DatagramPacket;

import games.apolion.http.authentication.Session;
import games.apolion.udp.server.entities.GameStateLogic;
import games.apolion.udp.server.UDPGameServer;
import games.apolion.udp.server.messages.MessageObject;
import games.apolion.udp.server.messages.ServerCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TempInGameGameStateLogic implements GameStateLogic {


	public static String destinationTokenDelimiter="Dtoken:";

	Logger LOG = LoggerFactory.getLogger(TempInGameGameStateLogic.class);
	@Override
	public void tick(UDPGameServer owner) {
		MessageObject mObj = owner.getGlobalQueue().poll();

		//LOG.info(mObj.getMessage());
		if (mObj == null)
			return;
		String strToken = mObj.get("token").trim();
		Session senderSession = null;
		for (Session s : owner.getUsersInGame())
		{
			if(s.t.token.equals(strToken))
				senderSession=s;
		}
		if(senderSession==null)
			return;
		if (mObj.getType() == ServerCommands.registerPort) {
			LOG.info("Sending player Port Registered ack");
			//Settup game port
			String messageToken = mObj.get("token");
			for (Session s : owner.getUsersInGame()) {
				if (messageToken.trim().contains(s.t.token)) {
					RegisterPortAndSendAck(owner,mObj, s);
					SendNewPlayerRegister(owner,mObj,senderSession);
					break;
				}
			}
		} else if (mObj.getType() == ServerCommands.chat) {
			//send message to all other chat clients
			try {
				for (Session s : owner.getUsersInGame()) {
					if (s.t.ip != null && s.t.getPort() > 0) {
						byte[] buf = new byte[UDPGameServer.bufSize];
						buf = (mObj.getMessage() + " "+destinationTokenDelimiter + s.t.token).getBytes();
						DatagramPacket packet = new DatagramPacket(buf, buf.length, s.t.ip, s.t.getPort());
							LOG.info(s.t.ip+ "|" + s.t.port + "|");
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

		} else if (mObj.getType() == ServerCommands.setPosRot) {
			try {
				for (Session s : owner.getUsersInGame()) {
					if(s.t.token.equals(senderSession.t.token))
						continue;
					if (s.t.ip != null && s.t.port > 0) {
						byte[] buf = new byte[UDPGameServer.bufSize];
						buf = (mObj.getMessage() + " user:"+senderSession.u.getUsername()+" "+destinationTokenDelimiter + s.t.token).getBytes();
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
		} else if (mObj.getType() == ServerCommands.inputCommand) {

		}
	}

	private void SendNewPlayerRegister(UDPGameServer owner, MessageObject mObj, Session senderSession) {
		for (Session s : owner.getUsersInGame()) {
			if (s.t.ip != null && s.t.port > 0) {
				if(s.t.token.equals(senderSession.t.token))
					continue;
				byte[] buf = new byte[UDPGameServer.bufSize];
				buf = ( ServerCommands.instantiateNewPlayer.commandStr+": "+ " user:"+senderSession.u.getUsername()+" "+destinationTokenDelimiter + s.t.token).getBytes();
				DatagramPacket packet = new DatagramPacket(buf, buf.length, s.t.ip, s.t.port);
//							System.out.println(s.t.ip+ "|" + s.t.port + "|" + received);
				try {
					owner.getSocket().send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void RegisterPortAndSendAck(UDPGameServer owner, MessageObject mObj, Session s) {
		try {
			s.t.port = Integer.parseInt(mObj.get("port"));
		} catch (Exception e) {
			System.err.print("Could not parse port" + e.getMessage());
			return;
		}
		//Respond to player that ports has been registed
		byte[] buf = new byte[UDPGameServer.bufSize];
		buf = (ServerCommands.portHasBeenRegister.commandStr+": "+destinationTokenDelimiter+mObj.get("token")).getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, s.t.ip, s.t.port);
		try {
			owner.getSocket().send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
