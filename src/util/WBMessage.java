package util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WBMessage {
	
	public static final int LOGIN = 1;
	
	public static final int TALK = 2;
	
	public static final int LOGOUT = 3;
	
	public static final int PRIVATECHAT = 0;
	
	private Integer type;
	
	private String from;
	
	private String date;
	
	private String content;
	
	private String privateTo;
	
	public String getPrivateTo() {
		return privateTo;
	}

	public void setPrivateTo(String privateTo) {
		this.privateTo = privateTo;
	}

	private List<String> users;
	
	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public WBMessage() {
		this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

}
