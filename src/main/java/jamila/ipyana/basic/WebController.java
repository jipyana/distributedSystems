package jamila.ipyana.basic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller

public class WebController {

	
	@RequestMapping(path="/", method=RequestMethod.GET)
	public String registration() {
		return "login";
	}
	@RequestMapping(path="/home", method=RequestMethod.GET)
	public String home() {
		return "bleep";
	}
}
 