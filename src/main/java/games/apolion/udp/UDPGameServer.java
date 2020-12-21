package games.apolion.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import games.apolion.http.authentication.Session;
import games.apolion.http.updConfig.UPDServerConfig;

public class UDPGameServer implements Runnable {
	Logger LOG = LoggerFactory.getLogger(UDPGameServer.class);
	private int basePort;
	private List<Session> usersInGame = new LinkedList<Session>();
	private Session host = null;
	private String name;
	private DatagramSocket socket;
	public boolean running;
	private byte[] buf;
	public static int bufSize;
	private GameServerStates state = GameServerStates.Lobby;
	private Queue<MessageObject> globalQueue = new ConcurrentLinkedQueue<MessageObject>();
	private MessageFactory messageFactory;
	private Thread logicThread;
	private LogicProcessor processor;
	public UDPGameServer(MessageFactory messageFactory,String name,UPDServerConfig config) throws SocketException, UnknownHostException {
		int count = 0;
		this.basePort=config.getUDPbaseport();
		this.bufSize = config.getUDPmessagebuffersize();
        boolean foundAPort = true;
        do
        {
            try
            {
            	//LOG.info("base port "+basePort);
            	socket = new DatagramSocket(basePort+ count);
                foundAPort = true;
            }
            catch (Exception e)
            {
                count++;
                foundAPort = false;
            }
        } while (!foundAPort);
        //Settup message factory
        this.messageFactory=messageFactory;
        //Setup logic processor
        processor = new LogicProcessor(this);
        logicThread = new Thread(processor);
        logicThread.start();
        this.name = name;
	}
	
	public void addLogicForState(GameServerStates state,GameStateLogic stateLogic) {
		processor.addLogicForState(state,stateLogic);
	}
	
	public GameServerStates getState() {
		return state;
	}

	public void setState(GameServerStates state) {
		this.state = state;
	}

	public Session getHost() {
		return host;
	}

	public void setHost(Session host) {
		this.host = host;
	}

	public boolean addUser(Session u) {
		return usersInGame.add(u);
	}
	public boolean removeUser(Session u) {
		return usersInGame.remove(u);
	}
	public String getIP() {
		return socket.getLocalAddress().toString();
	}
	public int getPort() {
		return socket.getLocalPort();
	}
	
	
	public void run() {
		running = true;

		LOG.info("Waiting for udp packets " + socket.getLocalPort());
		while (running) {
//			System.out.println("Users in game " + usersInGame.toString());
			buf = new byte[bufSize];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			packet = new DatagramPacket(buf, buf.length, address, port);
			String received = new String(packet.getData(), 0, packet.getLength());
			
			MessageObject mObj = messageFactory.parse(received,address,port);
			if( mObj != null )
				globalQueue.add(mObj);
			System.out.println(address.getHostAddress() + "|" + port + "|" + received);
//			try {
//				socket.send(packet);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
		socket.close();
		processor.done = true;
		try {
			logicThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Session> getUsersInGame() {
		return usersInGame;
	}
	public String getName() {
		return name;
	}
	public DatagramSocket getSocket() {
		return socket;
	}
	public Queue<MessageObject> getGlobalQueue() {
		return globalQueue;
	}
}
