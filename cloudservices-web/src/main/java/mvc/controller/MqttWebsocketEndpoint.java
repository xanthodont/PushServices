package mvc.controller;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;



@ServerEndpoint(value="cloudservices-web/ws/mqtt")
public class MqttWebsocketEndpoint {
	private Session session;  
    //private static final Logger sysLogger = Logger.getLogger("sysLog");
	private static void sysLoggerinfo(String msg) {
		System.out.println(msg);
	}
      
    @OnOpen  
    public void open(Session session,  @PathParam(value = "user")String user) {  
        this.session = session;  
          
        sysLoggerinfo("*** WebSocket opened from sessionId " + session.getId());  
    }  
      
    @OnMessage  
    public void inMessage(String message) {  
        sysLoggerinfo("*** WebSocket Received from sessionId " + this.session.getId() + ": " + message);  
    }  
      
    @OnClose  
    public void end() {  
        sysLoggerinfo("*** WebSocket closed from sessionId " + this.session.getId());  
    }  
}
