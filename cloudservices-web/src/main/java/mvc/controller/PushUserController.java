package mvc.controller;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JRadioButton;

import mvc.dao.IPushUserDao;
import mvc.dao.PageList;
import mvc.model.JResponse;
import mvc.model.PushUser;
import mvc.service.IPushUserService;

import org.apache.log4j.Logger;
import org.dna.mqtt.moquette.server.Server;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cloudservices.client.ClientService;
import cloudservices.client.http.async.support.ParamsWrapper;
import cloudservices.client.packets.AckPacket;
import cloudservices.client.packets.FilePacket;
import cloudservices.client.packets.HttpPacket;
import cloudservices.client.packets.Packet;
import cloudservices.client.packets.PacketCollector;
import cloudservices.client.packets.TextPacket;
import cloudservices.client.packets.filters.PacketAckFilter;


@Controller
@RequestMapping(value="/push/user/*")
public class PushUserController {
	private static Logger logger = Logger.getLogger(PushUserController.class);
	@Resource
	private IPushUserService pushuserService;
	

	@RequestMapping(value = "list", method = RequestMethod.GET)  
	public ModelAndView userListView(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("push/userlist");
		mv.addObject("title", "Spring MVC And Freemarker");
		mv.addObject("content", " Hello world ， test my first spring mvc ! ");
		return mv;
	}
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public PageList userListPost(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("page") int page,
			@RequestParam("rows") int size) {
		PageList<PushUser> list = pushuserService.getUserList(page, size);
		
		//Map map = new HashMap<String, Object>();
		//map.put("total", list.size());
		//map.put("rows", list);
		return list;
	}
	
	@RequestMapping(value = "online")
	@ResponseBody
	public JResponse online(HttpServletRequest request, HttpServletResponse response) {
		int count = pushuserService.getTotalOnline();
		return JResponse.success(String.valueOf(count));
	}
	
	@RequestMapping(value = "send")
	@ResponseBody
	public JResponse send(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("topic") String topic,
			@RequestParam("isAck") boolean isAck,
			@RequestParam("text") String text) {
		TextPacket packet = new TextPacket();
		packet.setText(text);
		packet.setAck(isAck);
		if (Server.getInstance().getMessaging().isOffline(topic)) { // topic已离线
			ClientService.getInstance().sendPacket(packet, topic);
			return JResponse.success("用户离线，消息已存储");
		}
		if (isAck) {
			PacketCollector collector = ClientService.getInstance().createPacketCollector(new PacketAckFilter(packet.getMessageId()));
			ClientService.getInstance().sendPacket(packet, topic);
			Packet r = collector.nextResult(60000); // 等待超时时间设置为60秒
			collector.cancel();
			if (r != null) {
				return JResponse.success("收到回执");
			} else {
				return JResponse.success("等待消息回执超时");
			}
		} else {
			ClientService.getInstance().sendPacket(packet, topic);
			return JResponse.success("消息已下发");
		}
	}
	
	@RequestMapping(value = "sendfile")
	@ResponseBody
	public JResponse sendFile(@RequestParam("topic") String topic,
			@RequestParam("isAck") boolean isAck,
			@RequestParam("text") String text) {
		FilePacket packet = new FilePacket();
		if (Server.getInstance().getMessaging().isOffline(topic)) { // topic已离线
			ClientService.getInstance().sendPacket(packet, topic);
			return JResponse.success("用户离线，消息已存储");
		}
		if (isAck) {
			PacketCollector collector = ClientService.getInstance().createPacketCollector(new PacketAckFilter(packet.getMessageId()));
			ClientService.getInstance().sendPacket(packet, topic);
			Packet r = collector.nextResult(60000); // 等待超时时间设置为60秒
			collector.cancel();
			if (r != null) {
				return JResponse.success("收到回执");
			} else {
				return JResponse.success("等待消息回执超时");
			}
		} else {
			ClientService.getInstance().sendPacket(packet, topic);
			return JResponse.success("消息已下发");
		}
	}
	
	@RequestMapping(value = "sendhttp")
	@ResponseBody
	public JResponse sendHttp(@RequestParam("topic") String topic,
			@RequestParam("isAck") boolean isAck,
			@RequestParam("url") String url) {
		HttpPacket packet = new HttpPacket();
		packet.setAck(isAck);
		packet.setUrl(url);
		packet.setParams(new ParamsWrapper());
		if (Server.getInstance().getMessaging().isOffline(topic)) { // topic已离线
			ClientService.getInstance().sendPacket(packet, topic);
			return JResponse.success("用户离线，消息已存储");
		}
		if (isAck) {
			PacketCollector collector = ClientService.getInstance().createPacketCollector(new PacketAckFilter(packet.getMessageId()));
			ClientService.getInstance().sendPacket(packet, topic);
			AckPacket r = (AckPacket) collector.nextResult(60000); // 等待超时时间设置为60秒
			collector.cancel();
			if (r != null) {
				return JResponse.success("收到回执:"+r.getText());
			} else {
				return JResponse.success("等待消息回执超时");
			}
		} else {
			ClientService.getInstance().sendPacket(packet, topic);
			return JResponse.success("消息已下发");
		}
	}
	
	@RequestMapping(value="hello.do")
	@ResponseBody
	public PushUser handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PushUser user = new PushUser();
		user.setId(1);
		user.setUsername("中文");
		user.setPassword("12345");
		user.setResource("beidou");
		
		return user;
	}
	
}
