package games.apolion.http.controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import games.apolion.http.dtos.UsersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import games.apolion.http.authentication.Session;
import games.apolion.http.authentication.dtos.TokenDTO;
import games.apolion.http.persistance.Users;
import games.apolion.http.persistance.UsersRepository;
import games.apolion.http.recaptcha.ICaptchaService;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value="/users")
public class UserController {
	

    @Autowired
    private ICaptchaService captchaService;
    
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Autowired
	private UsersRepository userRepository;

	public static List<Session> sessions = new LinkedList<Session>();
	

	public static Session hasToken(String token) {
		for (Session t : sessions) {
			if (t.t.token.equals(token)) {
				return t;
			}
		}
		return null;
	}

	@PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void addUser(@RequestBody UsersDTO user, @RequestHeader("g-recaptcha-response") String captcha) {
		userRepository.save(convertToUsers(user));
	}

	@GetMapping(produces = "application/json")
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

	@GetMapping("/{username}")
	public UsersDTO game(@PathVariable String username, @RequestHeader("accesstoken") String token) {
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
	public TokenDTO getUserTokenForApi(@RequestBody UsersDTO user, HttpServletRequest request, @RequestHeader("g-recaptcha-response") String captcha) {
		Users userInfo = userRepository.findByUsernameAndPasswordhash(user.getUsername(), user.getPasswordhash());
		if (userInfo != null) {
			for (Session t : sessions) {
				if (t.u.getUsername().equals(user.getUsername())) {
					return t.t;
				}
			} // Else generate new token
			SecureRandom random = new SecureRandom();
			byte bytes[] = new byte[20];
			random.nextBytes(bytes);
			String token = bytes.toString();
			TokenDTO t = new TokenDTO(token);
			Session entry = new Session();
			entry.t = t;
			entry.u = userInfo;
			sessions.add(entry);
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
		return new TokenDTO("Invalid Credentials");
	}
	private Users convertToUsers(UsersDTO usersDTO){
		return new Users(usersDTO.getUsername(),usersDTO.getPasswordhash(),usersDTO.getEmail());
	}


	private UsersDTO convertToUsersDTO(Users users){
		return new UsersDTO(users.getUsername(),users.getEmail());
	}
}