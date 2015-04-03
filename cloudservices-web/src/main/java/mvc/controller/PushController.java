package mvc.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value="/push/*")
public class PushController {
	private static Logger logger = Logger.getLogger(PushController.class);

	@RequestMapping(value="userlist.do", produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String userList() {
		return "userlist";
	}
}
