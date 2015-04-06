package mvc.controller;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping(value="/push/*")
public class PushController {
	private static Logger logger = Logger.getLogger(PushController.class);

	@RequestMapping(value = "userlist.do", method = RequestMethod.GET)  
	public ModelAndView userList(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("push/userlist");
		mv.addObject("title", "Spring MVC And Freemarker");
		mv.addObject("content", " Hello world ， test my first spring mvc ! ");
		return mv;
	}
	
	@RequestMapping(value="hello.do", produces = {"text/html; charset=UTF-8"})
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("hello");
		mv.addObject("title", "Spring MVC And Freemarker");
		mv.addObject("content", " Hello world ， test my first spring mvc ! ");
		return mv;
	}

	
}
