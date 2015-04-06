package mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/app/*")
public class AppController {
	
	@RequestMapping(value = "login", method = RequestMethod.GET)  
	public String login() {
		
		return "app/login";
	}
}
