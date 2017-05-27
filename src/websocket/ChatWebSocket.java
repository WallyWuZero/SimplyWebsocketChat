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

@ServerEndpoint("/chat")
public class ChatWebSocket {
	
	private static  Set<ChatWebSocket>  ss= new HashSet<ChatWebSocket>();
	
	private String user;
	
	private Map<String,String> userMap = new HashMap<String,String>();
	
	private List<String> users = new ArrayList<String>();
	
	private Session  session ;
	
	private Gson gson=new Gson();
	
	public ChatWebSocket () {
		System.out.println("ChatWebSocket");
	}
	
	@OnOpen
	public void open(Session session) {
		System.out.println("Chat login: " + session.getId());
		
		String queryString= session.getQueryString();
		this.user = queryString.substring(queryString.indexOf("=")+1);
		
		ss.add(this);
		userMap.put(session.getId(), this.user );
		users.add(user);
		this.session = session;

		WBMessage msg = new WBMessage();
		msg.setType(WBMessage.LOGIN);
		msg.setFrom("Admin");
		msg.setContent(user+" come into Room");
		
		/*String msgjson = gson.toJson(msg);*/
		broadcast(ss,msg);
		
	}
	
	
	@OnMessage
	public void message(Session session, String msg) {
		System.out.println(msg);
		WBMessage  temp= gson.fromJson(msg, WBMessage.class);
		if(temp.getType()!=null && temp.getType().equals(0)) {
			broadcastprivate(ss, temp);
		} else {
			temp.setType(WBMessage.TALK);
			broadcast(ss, temp);
		}
		
	}
	
	@OnClose
	public void close(Session session) {
		System.out.println("Chat logout: " + session.getId());
		
		ss.remove(this);
		
		WBMessage msg = new WBMessage();
		msg.setType(WBMessage.LOGOUT);
		msg.setFrom("Admin");
		msg.setContent(user+" leave Room");
		
		broadcast(ss,msg);
	}
	
	private void broadcast(Set<ChatWebSocket>  ss, WBMessage msg) {
		
		List<String> userList = new ArrayList<String>();
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			ChatWebSocket chatwebsocket = (ChatWebSocket) iterator.next();
			userList.add(chatwebsocket.user);
		}
		msg.setUsers(userList);
		String msgjson = gson.toJson(msg);
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			ChatWebSocket chatwebsocket = (ChatWebSocket) iterator.next();
			
			try {
				chatwebsocket.session.getBasicRemote().sendText(msgjson);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
private void broadcastprivate(Set<ChatWebSocket>  ss, WBMessage msg) {
		
		List<String> userList = new ArrayList<String>();
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			ChatWebSocket chatwebsocket = (ChatWebSocket) iterator.next();
			userList.add(chatwebsocket.user);
		}
		msg.setUsers(userList);
		String msgjson = gson.toJson(msg);
		for (Iterator iterator = ss.iterator(); iterator.hasNext();) {
			ChatWebSocket chatwebsocket = (ChatWebSocket) iterator.next();
			
			try {
				if(chatwebsocket.user.equals(msg.getPrivateTo())){
					chatwebsocket.session.getBasicRemote().sendText(msgjson);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
