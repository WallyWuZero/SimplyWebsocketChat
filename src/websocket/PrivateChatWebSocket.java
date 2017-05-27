package websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import util.WBMessage;

@ServerEndpoint("/privatechat")
public class PrivateChatWebSocket {
	
	private static  Set<PrivateChatWebSocket>  ss= new HashSet<PrivateChatWebSocket>();
	
	private String user;
	
	private String privateTo;
	
	private Map<String,String> userMap = new HashMap<String,String>();
	
	private List<String> users = new ArrayList<String>();
	
	private Session  session ;
	
	private String mapper;
	
	private Gson gson=new Gson();
	
	public PrivateChatWebSocket () {
		System.out.println("PrivateChatWebSocket");
	}
	
	@OnOpen
	public void open(Session session) {
		System.out.println("Private Chat login: " + session.getId());
		
		String queryString= session.getQueryString();
		//privatechat?currentUser=wally&privateTo=zero
		this.user = queryString.substring(queryString.indexOf("=")+1,queryString.indexOf("&"));
		this.privateTo = queryString.substring(queryString.lastIndexOf("=")+1);
		System.out.println("mapping:"+ user +"-"+ privateTo);
		ss.add(this);
		userMap.put(session.getId(), this.user );
		users.add(user);
		this.session = session;
		mapper =user+ "-MPM-"+ privateTo;
		WBMessage msg = new WBMessage();
		msg.setType(WBMessage.LOGIN);
		msg.setFrom("Admin");
		msg.setPrivateTo(this.privateTo);
		msg.setContent(user+" come into Private Room");
		
		/*String msgjson = gson.toJson(msg);*/
		broadcast(ss,msg);
		
	}
	
	
	@OnMessage
	public void message(Session session, String msg) {
		System.out.println(msg);
		WBMessage  temp= gson.fromJson(msg, WBMessage.class);
		temp.setType(WBMessage.TALK);
		broadcast(ss, temp);
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("Chat logout: " + session.getId());
		
		ss.remove(this);
		
		WBMessage msg = new WBMessage();
		msg.setType(WBMessage.LOGOUT);
		msg.setFrom("Admin");
		msg.setContent(user+" leave Private Room");
		
		broadcast(ss,msg);
	}
	
	private void broadcast(Set<PrivateChatWebSocket>  ss, WBMessage msg) {
		
		String msgjson = gson.toJson(msg);
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			PrivateChatWebSocket chatwebsocket = (PrivateChatWebSocket) iterator.next();
			try {
				String mp1 = msg.getPrivateTo() + "-MPM-" + this.user;
				String mp2 = this.user + "-MPM-" + msg.getPrivateTo();
				
				if(chatwebsocket.user.equals(msg.getPrivateTo())||chatwebsocket.user.equals(this.user)) {
					
					if(chatwebsocket.mapper.equals(mp1) || chatwebsocket.mapper.equals(mp2))
						{	
							chatwebsocket.session.getBasicRemote().sendText(msgjson);
						}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
