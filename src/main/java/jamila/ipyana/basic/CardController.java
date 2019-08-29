package jamila.ipyana.basic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.LockModeType;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@CrossOrigin
@RestController
@RequestMapping("/cards")
public class CardController {
	
	@Autowired
	private CardJpaRepository cards;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserJpaRepository users;
	private UserController uController;
	
	private Card card;
	private User user;
	
	@PostConstruct
	@Transactional
	private void init() {
		
		User admin = users.findByUsername("admin");
		if(admin==null) {
			admin = new User();
			admin.setUsername("admin");
			admin.setPassword(passwordEncoder.encode("12345"));
			admin.setEmail("j7a0i8@gmail.com");
			admin.getRoles().addAll(Arrays.asList("USER", "ADMIN"));
			users.save(admin);
		}
		
	}
	
	//no path
	@RequestMapping(path="", method=RequestMethod.POST)
	@Transactional
	public void create(@RequestBody Card newCard) {
		user = users.findById(newCard.getCardToUserId());
		if(user!=null) {
			cards.save(newCard);
			addCardToUser(newCard.getId(),user.getId());
			cards.save(newCard);
		}
	}
	
	@RequestMapping(path="/admin/{cardId}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAuthority('ADMIN')")
	public void delete(@PathVariable int cardId) {
		cards.deleteById(cardId);
	}
	
//	@RequestMapping(path="/{cardId}", method=RequestMethod.GET)
//	public Card findCourse(@PathVariable Integer cardId) {
//		return cards.get(cardId);
//	}
	
	@RequestMapping(path="/all", method=RequestMethod.GET)
	public List<Card> retrieveAll() {
		return cards.findAll();
	}
	
	@RequestMapping(path="/{cardId}", method=RequestMethod.GET)
	public Card retrieveCard(
			@PathVariable int cardId) {
		return cards.findById(cardId).orElse(null);
	}
	
	@RequestMapping(path="/findDescription", method=RequestMethod.GET)
	public List<Card> findDescription(@RequestParam String description) {
		return cards.findByDescription(description);
	}
	
	@RequestMapping(path="/searchByUsername", method=RequestMethod.GET)
	public List<Card> searchByUsername(@RequestParam String username) {
		return cards.findByUsername(username);
	}
	
	
	@RequestMapping(path="/{cardId}", method=RequestMethod.PUT)
	@Transactional
	public void update(
			@PathVariable Integer cardId, 
			@RequestBody Card src) {
	
		Card card = cards.getOne(cardId);
		card.copy(src);
		cards.save(card);
	
	}
	@PreAuthorize("hasAuthority('ADMIN')")
	@Transactional
	@CrossOrigin
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@RequestMapping(path="/{cardId}", method=RequestMethod.PATCH)
	public void updatePartial(
			@PathVariable Integer cardId,
			@RequestBody Map<String, Object> updates) {
		
		Card card = retrieveCard(cardId);
		for(String key : updates.keySet()) {
			if(key.equals("name")) {
				card.setNameOfCard((String)updates.get(key));
			}
		}
		cards.save(card);
	}
	
	public void addCardToUser(
			 int cardId, 
			 int userId) {
		System.out.print("User Id: " + user.getId());
		Card card = cards.getOne(cardId);
		User user = users.getOne(userId);
		card.setUser(user);
		cards.save(card);
	}
	
	

}
