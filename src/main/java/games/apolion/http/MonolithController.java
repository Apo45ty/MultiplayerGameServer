package games.apolion.http;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import games.apolion.http.dtos.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import games.apolion.http.authentication.Session;
import games.apolion.http.authentication.Token;
import games.apolion.http.persistance.Users;
import games.apolion.http.persistance.UsersRepository;
import games.apolion.http.recaptcha.ICaptchaService;
import games.apolion.http.dtos.GameDescriptorDTO;
import games.apolion.http.updConfig.UPDServerConfig;
import games.apolion.udp.GameServerStates;
import games.apolion.udp.TempInGameGameStateLogic;
import games.apolion.udp.TempLobbyGameStateLogic;
import games.apolion.udp.TempMessageFactory;
import games.apolion.udp.ThreadGame;
import games.apolion.udp.UDPGameServer;  
@CrossOrigin(maxAge = 3600)
@RestController
public class MonolithController {
	
	public static int maxNumberOfServers = 2;
	List<ThreadGame> GameServersInfo = new LinkedList<ThreadGame>();
	
	
	@PostConstruct
    public void init(){
		resetServers();
	}

    @Autowired
    private UPDServerConfig udpProperties;
    
	public  void resetServers() {
		
		for (int i = 0; i < maxNumberOfServers; i++) {
			UDPGameServer server = null;
			try {
				server = new UDPGameServer(new TempMessageFactory(), "Pinocho" + i,udpProperties);
			} catch (SocketException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}

			if (server == null) {
				throw new IllegalStateException("Server cant be null");
			}
			Thread UDPServer = new Thread(server);
			UDPServer.start();
			ThreadGame tg = new ThreadGame();
			tg.server = server;
			tg.runningGameT = UDPServer;
			server.setState(GameServerStates.InGame);
			GameServersInfo.add(tg);
			server.addLogicForState(GameServerStates.Lobby, new TempLobbyGameStateLogic());
			server.addLogicForState(GameServerStates.InGame, new TempInGameGameStateLogic());

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

		}

	}

	
    @Autowired
    private ICaptchaService captchaService;
    
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
	private UsersRepository userRepository;

	List<Session> tokens = new LinkedList<Session>();
	
	@GetMapping("/reset")
	public boolean reset(@RequestHeader("secret") String secret) {
		if (!secret.equalsIgnoreCase("secret"))
			return false;
		for(ThreadGame game:GameServersInfo) {
			if(game.server.getHost()!=null)
				game.server.getHost().serverInstanceJoined = null;
			for(Session s:game.server.getUsersInGame()) {
				s.serverInstanceJoined = null;
			}
			game.server.running = false;
			game.server.getSocket().close();
		}
		GameServersInfo = new LinkedList<ThreadGame>();
		resetServers();
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
	public GameDescriptorDTO getGameDescriptorFromName(@RequestHeader("accesstoken") String token,
													   @RequestParam String gamename) {
		if (hasToken(token) == null)
			return null;
		for (ThreadGame tg : GameServersInfo) {
			if (tg.server.getName().equals(gamename)) {
				GameDescriptorDTO descriptor = new GameDescriptorDTO();
				descriptor.port = tg.server.getPort();
				descriptor.ip = tg.server.getIP();
				descriptor.serverName = tg.server.getName();
				List<String> usernames = new LinkedList<String>();
				for (Session s : tg.server.getUsersInGame()) {
					usernames.add(s.u.getUsername());
				}
				descriptor.users = usernames;
				if (tg.server.getHost() != null)
					descriptor.host = tg.server.getHost().u.getUsername();
				descriptor.state = tg.server.getState();
				return descriptor;
			}
		}
		return null;
	}

	@GetMapping(path = "/GetUsersInGame", consumes = "application/json", produces = "application/json")
	public Iterable<String> getUsersInGame(@RequestHeader("accesstoken") String token, @RequestParam String gamename) {
		if (hasToken(token) == null)
			return null;
		List<String> usernames = new LinkedList<String>();
		for (ThreadGame tg : GameServersInfo) {
			if (tg.server.getName().equals(gamename)) {
				for (Session s : tg.server.getUsersInGame()) {
					usernames.add(s.u.getUsername());
				}
				if (tg.server.getHost() != null)
					usernames.add(tg.server.getHost().u.getUsername());
			}
		}
		return usernames;
	}

	@PostMapping(path = "/HostGame", consumes = "application/json", produces = "application/json")
	public GameDescriptorDTO hostGame(@RequestHeader("accesstoken") String token) {
		Session session = hasToken(token);
		if (session == null)
			return null;
		if (session.serverInstanceJoined != null)
			return null;
		UDPGameServer server = null;
		for (ThreadGame tg : GameServersInfo) {
			if (tg.server.getHost() == null) {
				server = tg.server;
				break;
			}
		}
		if (server != null) {
			GameDescriptorDTO descriptor = new GameDescriptorDTO();
			descriptor.port = server.getPort();
			descriptor.ip = server.getIP();
			descriptor.serverName = server.getName();
			List<String> usernames = new LinkedList<String>();
			for (Session u : server.getUsersInGame()) {
				usernames.add(u.u.getUsername());
			}
			descriptor.users = usernames;
			descriptor.host = session.u.getUsername();
			session.serverInstanceJoined = server;
			server.setHost(session);
			return descriptor;
		}
		return null;
	}

	@PostMapping(path = "/JoinAnyGame", consumes = "application/json", produces = "application/json")
	public GameDescriptorDTO joinAnyGame(@RequestHeader("accesstoken") String token) {
		Session session = hasToken(token);
		if (session == null)
			return null;
		if (session.serverInstanceJoined != null)
			return null;
		// TODO select Game Room
		UDPGameServer server = GameServersInfo.get(0).server;
		GameDescriptorDTO descriptor = new GameDescriptorDTO();
		descriptor.port = server.getPort();
		descriptor.ip = server.getIP();
		descriptor.serverName = server.getName();
		if (server.getHost() != null)
			descriptor.host = server.getHost().u.getUsername();
		session.serverInstanceJoined = server;
		if (!server.addUser(session))
			System.err.print("error adding user to game");
		List<String> usernames = new LinkedList<String>();
		for (Session s : server.getUsersInGame()) {
			usernames.add(s.u.getUsername());
		}
		descriptor.users = usernames;
		descriptor.state = server.getState();
		return descriptor;
	}

	@PostMapping(path = "/LeaveGame", consumes = "application/json", produces = "application/json")
	public GameDescriptorDTO leaveGame(@RequestHeader("accesstoken") String token) {
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
	@PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
	public void addUser(@RequestBody UsersDTO user, @RequestHeader("g-recaptcha-response") String captcha) {
		userRepository.save(convertToUsers(user));
	}

	@RequestMapping(path = "/users", produces = "application/json")
	public Iterable<UsersDTO> openendedusers(@RequestHeader("accesstoken") String token,
											 @RequestParam Optional<Integer> pageNumber, @RequestParam Optional<Integer> pageSize) {
		if (hasToken(token) == null)
			return null;
		int pageS = 20;
		int pageN = 0;
		if(pageNumber.isPresent())
		{
			pageN = pageNumber.get();
		}
		if(pageSize.isPresent())
		{
			pageS = pageSize.get();
		}
		Iterable<Users> temp = userRepository.findAll(PageRequest.of(pageN,pageS));
		List<UsersDTO> result = new LinkedList<UsersDTO>();
		for(Users user : temp){
			result.add(convertToUsersDTO(user));
		}
		return  result;
	}

	@GetMapping("/user")
	public UsersDTO game(@RequestParam String username, @RequestHeader("accesstoken") String token) {
		if (hasToken(token) == null)
			return null;
		return convertToUsersDTO(userRepository.findByUsername(username));
	}
//	@PostMapping(path = "/contact", consumes = "application/json", produces = "application/json")
//	public void contact(@RequestBody ContactForm form, HttpServletRequest request,@RequestHeader("g-recaptcha-response") String captcha) {
//        captchaService.processResponse(captcha);
//		String address = (request.getRemoteAddr());
//        form.setFromip(address);
//		
//       // contactRepository.save(form);
//        SimpleMailMessage msg = new SimpleMailMessage();
//        msg.setTo("antonio@apolion.games");
//        
//        msg.setSubject("Contact Form:"+form.getSubject());
//        msg.setText(form.getMessage()+"\n el email del usuario es:  "+form.getEmail());
//        
//        javaMailSender.send(msg);
//	}
//	
	@PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
	public Token getUserTokenForApi(@RequestBody Users user, HttpServletRequest request,@RequestHeader("g-recaptcha-response") String captcha) {
		Users userInfo = userRepository.findByUsernameAndPasswordhash(user.getUsername(), user.getPasswordhash());
		if (userInfo != null) {
			for (Session t : tokens) {
				if (t.u.getUsername().equals(user.getUsername())) {
					return t.t;
				}
			} // Else generate new token
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[20];
			random.nextBytes(bytes);
			String token = bytes.toString();
			Token t = new Token(token);
			Session entry = new Session();
			entry.t = t;
			entry.u = userInfo;
			tokens.add(entry);
			try {
				String address = (request.getRemoteAddr());
				if (address.contains("0:0:0:0:0:0:0"))
					t.ip = InetAddress.getLocalHost();
				else
					t.ip = InetAddress.getByName(request.getRemoteAddr());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			return t;
		}
		return new Token("Invalid Credentials");
	}
	private Users convertToUsers(UsersDTO usersDTO){
		return new Users(usersDTO.getUsername(),usersDTO.getPasswordhash(),usersDTO.getEmail());
	}


	private UsersDTO convertToUsersDTO(Users users){
		return new UsersDTO(users.getUsername(),users.getEmail());
	}
}