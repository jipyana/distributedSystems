package jamila.ipyana.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.internet.InternetAddress;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	@Autowired
	private UserJpaRepository users;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
	@Async
	public void sendEmail(SimpleMailMessage email) {
		mailSender.send(email);
	}
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//no path
	@RequestMapping(path="", method=RequestMethod.POST)
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@Transactional
	public void create(@RequestBody User newUser) {
//		System.out.println(newUser.getUsername());
		System.out.println(newUser.getPassword());
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.getRoles().add("USER");
		users.save(newUser);
	}
	
//	@PreAuthorize("hasAuthority('ADMIN')")
	@Transactional
	@RequestMapping(path="/admin/{userId}", method=RequestMethod.DELETE)
	public void delete(@PathVariable int userId) {
		users.deleteById(userId);
	}

//	@RequestMapping(path="/searchByName", method=RequestMethod.GET)
//	public List<User> findUsersByName(
//			@RequestParam(value="name") final String name) {
//		
//		return users.findAll().stream()
//			.filter(i -> i.getUsername().equals(name))
//			.collect(Collectors.toList());
//	}
	
	@RequestMapping(path="/{userId}", method=RequestMethod.GET)
	public User retrieve(
			@PathVariable int userId) {
		return users.getOne(userId);
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(path="/getAll", method=RequestMethod.GET)
	public List<User> retrieveAll() {
		return users.findAll();
	}
	
	@RequestMapping(path="/searchByUsername", method=RequestMethod.GET)
	public User searchByUsername(@RequestParam String username) {
		return users.findByUsername(username);
	}
	
	@RequestMapping(path="/userPartial", method=RequestMethod.GET)
	public List<User> findByNameLike(@RequestParam String username) {
		return users.findByUsernameLike(username);
	}
	
	@RequestMapping(path="/{userId}", method=RequestMethod.PUT)
	@Transactional
	public void update(@PathVariable int userId, @RequestBody User updated) {
		User usr = retrieve(userId);
		if(usr == null) {
			throw new IllegalArgumentException("user id " + userId + "dne");
			
		}
		usr.copy(updated);
		usr.setPassword(passwordEncoder.encode(updated.getPassword()));
		users.save(usr);
	}
	
	@RequestMapping(path="/forgot/{username}", method=RequestMethod.POST)
	public void forgotPassword(@PathVariable String username)  {
		User foundUser = searchByUsername(username);
		if(foundUser == null) {
			throw new IllegalArgumentException("user " + username + " does not exist");
		}
		
		String newPassword = randomAlphaNumeric(5);
		foundUser.setPassword(newPassword);
		update(foundUser.getId(), foundUser);
	
		 
		System.out.println();
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(foundUser.getEmail());
		message.setFrom("j7a0i8@gmail.com");
		message.setSubject("Forgot Password");
		message.setText(foundUser.getUsername() + " here is your new password " + newPassword );
		sendEmail(message);
	}
	
	@RequestMapping(path="/admin/{userId}", method=RequestMethod.POST)
	@Transactional
	@PreAuthorize("hasAuthority('ADMIN')")
	public void makeAdmin(@PathVariable Integer userId, @PathVariable boolean admin) {
		User user = retrieve(userId);
		if(admin) {
			if(!user.getRoles().contains("ADMIN")) {
				user.getRoles().add("ADMIN");
			}
		}
		else {
			if(user.getRoles().contains("ADMIN")) {
				user.getRoles().remove("ADMIN");
			}
		}
	}
	
	public static String randomAlphaNumeric(int len) {
		StringBuilder builder = new StringBuilder();
		while (len-- != 0) {
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
	return builder.toString();
	}
}

	

