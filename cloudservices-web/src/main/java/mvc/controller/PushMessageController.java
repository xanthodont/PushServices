package mvc.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mvc.dao.PageList;
import mvc.model.PushMessage;
import mvc.service.IPushMessageService;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/push/message/*")
public class PushMessageController {
	private static Logger logger = Logger.getLogger(PushMessageController.class);
	@Resource
	private IPushMessageService pushMessageService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)  
	public ModelAndView userListView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("push/messagelist");
		mv.addObject("title", "Spring MVC And Freemarker");
		mv.addObject("content", " Hello world ， test my first spring mvc ! ");
		return mv;
	}
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public PageList<PushMessage> userListPost(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("page") int page,
			@RequestParam("rows") int size) {
		PageList<PushMessage> list = pushMessageService.getMessageList(page, size);
		return list;
	}
	
	public IPushMessageService getPushMessageService() {
		return pushMessageService;
	}
	public void setPushMessageService(IPushMessageService pushMessageService) {
		this.pushMessageService = pushMessageService;
	}
}
