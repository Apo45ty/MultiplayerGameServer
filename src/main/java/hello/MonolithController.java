package hello;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;  
@CrossOrigin(maxAge = 3600)
@RestController
public class MonolithController {
//	public static int maxNumberOfServers = 2;
//	static List<ThreadGame> GameServersInfo = new LinkedList<ThreadGame>();
//	{
//		resetServers();
//	}
//
//	public static void resetServers() {
//		for (int i = 0; i < maxNumberOfServers; i++) {
//			UDPGameServer server = null;
//			try {
//				server = new UDPGameServer(new TempMessageFactory(), "Pinocho" + i);
//			} catch (SocketException e1) {
//				e1.printStackTrace();
//			} catch (UnknownHostException e1) {
//				e1.printStackTrace();
//			}
//
//			if (server == null) {
//				throw new IllegalStateException("Server cant be null");
//			}
//			Thread UDPServer = new Thread(server);
//			UDPServer.start();
//			ThreadGame tg = new ThreadGame();
//			tg.server = server;
//			tg.runningGameT = UDPServer;
//			server.setState(GameServerStates.InGame);
//			GameServersInfo.add(tg);
//			server.addLogicForState(GameServerStates.Lobby, new TempLobbyGameStateLogic());
//			server.addLogicForState(GameServerStates.InGame, new TempInGameGameStateLogic());
//
//			/** DEBUGGING MODE **/
////			Session s = new Session();
////			s.t = new Token("a1");
////			try {
////				s.t.ip = InetAddress.getLocalHost();
////			} catch (UnknownHostException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			s.u = new Users();
////			s.u.setUsername("Pokemon Trainer" + i);
////			server.addUser(s);
////			//
////			s = new Session();
////			s.t = new Token("a2");
////			try {
////				s.t.ip = InetAddress.getLocalHost();
////			} catch (UnknownHostException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			s.u = new Users();
////			s.u.setUsername("Fiddles" + i);
////			server.addUser(s);
//			//
////			if (i == 10) {
////				s = new Session();
////				s.t = new Token("a3");
////				try {
////					s.t.ip = InetAddress.getLocalHost();
////				} catch (UnknownHostException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////				s.u = new Users();
////				s.u.setUsername("Lola" + i);
////				server.setHost(s);
////			}
//
//		}
//
//	}

	
    @Autowired
    private ICaptchaService captchaService;
    
	@Autowired
    private JavaMailSender javaMailSender;
	
//	@Autowired
//	private UsersRepository userRepository;
//	
//	@Autowired
//	private ContactFormRepository contactRepository;

	List<Session> tokens = new LinkedList<Session>();
	
	@GetMapping("/reset")
	public boolean reset(@RequestHeader("secret") String secret) {
		if (!secret.equalsIgnoreCase("secret"))
			return false;
//		for(ThreadGame game:GameServersInfo) {
//			if(game.server.getHost()!=null)
//				game.server.getHost().serverInstanceJoined = null;
//			for(Session s:game.server.getUsersInGame()) {
//				s.serverInstanceJoined = null;
//			}
//			game.server.running = false;
//			game.server.getSocket().close();
//		}
//		GameServersInfo = new LinkedList<ThreadGame>();
//		resetServers();
		return true;
	}

	
	public Session hasToken(String token) {
		for (Session t : tokens) {
			if (t.t.token.equals(token)) {
				return t;
			}
		}
		return null;
	}
	
	@GetMapping(path = "/GetGameDescriptorFromName", consumes = "application/json", produces = "application/json")
	public GameDescriptor getGameDescriptorFromName(@RequestHeader("accesstoken") String token,
			@RequestParam String gamename) {
		if (hasToken(token) == null)
			return null;
//		for (ThreadGame tg : GameServersInfo) {
//			if (tg.server.getName().equals(gamename)) {
//				GameDescriptor descriptor = new GameDescriptor();
//				descriptor.port = tg.server.getPort();
//				descriptor.ip = tg.server.getIP();
//				descriptor.serverName = tg.server.getName();
//				List<String> usernames = new LinkedList<String>();
//				for (Session s : tg.server.getUsersInGame()) {
//					usernames.add(s.u.getUsername());
//				}
//				descriptor.users = usernames;
//				if (tg.server.getHost() != null)
//					descriptor.host = tg.server.getHost().u.getUsername();
//				descriptor.state = tg.server.getState();
//				return descriptor;
//			}
//		}
		return null;
	}

	@GetMapping(path = "/GetUsersInGame", consumes = "application/json", produces = "application/json")
	public Iterable<String> getUsersInGame(@RequestHeader("accesstoken") String token, @RequestParam String gamename) {
		if (hasToken(token) == null)
			return null;
//		List<String> usernames = new LinkedList<String>();
//		for (ThreadGame tg : GameServersInfo) {
//			if (tg.server.getName().equals(gamename)) {
//				for (Session s : tg.server.getUsersInGame()) {
//					usernames.add(s.u.getUsername());
//				}
//				if (tg.server.getHost() != null)
//					usernames.add(tg.server.getHost().u.getUsername());
//			}
//		}
//		return usernames;
		return null;
	}

	@PostMapping(path = "/HostGame", consumes = "application/json", produces = "application/json")
	public GameDescriptor hostGame(@RequestHeader("accesstoken") String token) {
		Session session = hasToken(token);
		if (session == null)
			return null;
		if (session.serverInstanceJoined != null)
			return null;
		//UDPGameServer server = null;
//		for (ThreadGame tg : GameServersInfo) {
//			if (tg.server.getHost() == null) {
//				server = tg.server;
//				break;
//			}
//		}
//		if (server != null) {
//			GameDescriptor descriptor = new GameDescriptor();
//			descriptor.port = server.getPort();
//			descriptor.ip = server.getIP();
//			descriptor.serverName = server.getName();
//			List<String> usernames = new LinkedList<String>();
//			for (Session u : server.getUsersInGame()) {
//				usernames.add(u.u.getUsername());
//			}
//			descriptor.users = usernames;
//			descriptor.host = session.u.getUsername();
//			session.serverInstanceJoined = server;
//			server.setHost(session);
//			return descriptor;
//		}
		return null;
	}

	@PostMapping(path = "/JoinAnyGame", consumes = "application/json", produces = "application/json")
	public GameDescriptor joinAnyGame(@RequestHeader("accesstoken") String token) {
		Session session = hasToken(token);
		if (session == null)
			return null;
		if (session.serverInstanceJoined != null)
			return null;
		// TODO select Game Room
//		UDPGameServer server = GameServersInfo.get(0).server;
//		GameDescriptor descriptor = new GameDescriptor();
//		descriptor.port = server.getPort();
//		descriptor.ip = server.getIP();
//		descriptor.serverName = server.getName();
//		if (server.getHost() != null)
//			descriptor.host = server.getHost().u.getUsername();
//		session.serverInstanceJoined = server;
//		if (!server.addUser(session))
//			System.err.print("error adding user to game");
//		List<String> usernames = new LinkedList<String>();
//		for (Session s : server.getUsersInGame()) {
//			usernames.add(s.u.getUsername());
//		}
//		descriptor.users = usernames;
//		descriptor.state = server.getState();
//		return descriptor;
		return null;
	}

	@PostMapping(path = "/LeaveGame", consumes = "application/json", produces = "application/json")
	public GameDescriptor leaveGame(@RequestHeader("accesstoken") String token) {
		Session session = hasToken(token);
		if (session == null)
			return null;
		if (session.serverInstanceJoined == null)
			return null;
		if (session.serverInstanceJoined.getHost() == session) {
			session.serverInstanceJoined.setHost(null);
		} else {
			if (!session.serverInstanceJoined.removeUser(session)) {
				System.err.print("Error removing user from game");
			}
		}
		session.serverInstanceJoined = null;
		return null;
	}
	@CrossOrigin(origins = "https://apolion.games")
	@PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
	public void addUser(@RequestBody Users user,@RequestHeader("g-recaptcha-response") String captcha) {
		captchaService.processResponse(captcha);
		//userRepository.save(user);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("antonio@apolion.games");

        msg.setSubject("User Registered");
        msg.setText("Hi User with the email of : "+user.getEmail()+" has just registered");

        javaMailSender.send(msg);
	}

	@RequestMapping(path = "/users", produces = "application/json")
	public Iterable<Users> openendedusers(@RequestHeader("accesstoken") String token) {
		if (hasToken(token) == null)
			return null;
		//return userRepository.findAll();
		return null;
	}

	@GetMapping("/user")
	public Users game(@RequestParam String username, @RequestHeader("accesstoken") String token) {
		if (hasToken(token) == null)
			return null;
		//return userRepository.findByUsername(username);
		return null;
	}
	@CrossOrigin(origins = "https://apolion.games")
	@PostMapping(path = "/contact", consumes = "application/json", produces = "application/json")
	public void contact(@RequestBody ContactForm form, HttpServletRequest request,@RequestHeader("g-recaptcha-response") String captcha) {
        captchaService.processResponse(captcha);
		String address = (request.getRemoteAddr());
        form.setFromip(address);
		
       // contactRepository.save(form);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("antonio@apolion.games");
        
        msg.setSubject("Contact Form:"+form.getSubject());
        msg.setText(form.getMessage()+"\n el email del usuario es:  "+form.getEmail());
        
        javaMailSender.send(msg);
	}
	
	@PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
	public Token getUserTockenForApi(@RequestBody Users user, HttpServletRequest request) {
		//Users userInfo = userRepository.findByUsernameAndPasswordhash(user.getUsername(), user.getPasswordhash());
//		if (userInfo != null) {
//			for (Session t : tokens) {
//				if (t.u.getUsername().equals(user.getUsername())) {
//					return t.t;
//				}
//			} // Else generate new token
//			SecureRandom random = new SecureRandom();
//			byte bytes[] = new byte[20];
//			random.nextBytes(bytes);
//			String token = bytes.toString();
//			Token t = new Token(token);
//			Session entry = new Session();
//			entry.t = t;
//			entry.u = userInfo;
//			tokens.add(entry);
//			try {
//				String address = (request.getRemoteAddr());
//				if (address.contains("0:0:0:0:0:0:0"))
//					t.ip = InetAddress.getLocalHost();
//				else
//					t.ip = InetAddress.getByName(request.getRemoteAddr());
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			}
//			return t;
//		}
//		return new Token("Invalid Credentials");
		return null;
	}

}