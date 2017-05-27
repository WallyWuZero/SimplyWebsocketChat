<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Room</title>

<script type="text/javascript">

var user='${sessionScope.user }';
var users=[];
var ws;//一个对象就是一个通信管道
var target="ws://localhost:8080/WebSocketTest/chat?currentUser="+user;

window.onload= init;

function init() {
    if ('WebSocket' in window) {
        ws = new WebSocket(target);
    } else if ('MozWebSocket' in window) {
        ws = new MozWebSocket(target);
    } else {
        alert('WebSocket is not supported by this browser.');
        return;
    }
    
    ws.onmessage=function(event){
	  		eval("var msg="+event.data+";");
		console.info(msg);
 
		if(msg.type==1) //login
		{
			showNewLogin(msg);
			appandUserList(msg);
		}
		else if(msg.type==2) //talk
		{
			showMsg(msg);
		}
		else if(msg.type==3) //logout
		{
			showLogout(msg);
			appandUserList(msg);
		}
		else if(msg.type==0)  //private
		{
			showMsg(msg);
			appandUserList4Private(msg);
		}
	}
		
}
//login
function showNewLogin(msg) {
	var   content= document.getElementById("content");
	content.innerHTML+=msg.content+" "+msg.date+"<br/>";
}

function showLogout(msg) {
	var   content= document.getElementById("content");
	content.innerHTML+=msg.content+" "+msg.date+"<br/>";
}

function showMsg(msg) {
	var   content= document.getElementById("content");
	content.innerHTML+=msg.from+" "+msg.date+"<br/>";
	content.innerHTML+=msg.content +"<br/>";
}

function appandUserList(msg) {
	var users = msg.users;
	if(users.length==0) return;
	var   userList= document.getElementById("userList");
	userList.innerHTML="";
	for(var i=0;i<users.length;i++){
		if(users[i]!=user) {
			userList.innerHTML+= '<a href=\"#\" onclick=\"jumpToPrivate(\''+users[i]+'\');\" >'+users[i]+'</a><br/>';
		} else {
			userList.innerHTML+= users[i]+"<br/>";
		}
			
	}
}

function appandUserList4Private(msg) {
	var users = msg.users;
	if(users.length==0) return;
	var   userList= document.getElementById("userList");
	userList.innerHTML="";
	for(var i=0;i<users.length;i++){
		if(users[i]==msg.from){
			userList.innerHTML+= '<a href=\"#\" onclick=\"jumpToPrivate(\''+users[i]+'\');\" >'+users[i]+'</a><br/>';
		}else if(users[i]!=user) {
			userList.innerHTML+= '<a href=\"#\" onclick=\"jumpToPrivate(\''+users[i]+'\');\" >'+users[i]+'</a><br/>';
		} else {
			userList.innerHTML+= users[i]+"<br/>";
		}
			
	}
}

function send(){
	var  write= document.getElementById("write");
	var  temp="{from:'"+user+"',content:'"+write.value+"'}";
	ws.send(temp);
	write.value = '';
}

function jumpToPrivate(privateTo) {
	var  temp="{from:'"+user+"',type:0,privateTo:'"+privateTo+"',content:'Lets private talk!'}";
	ws.send(temp);
	window.open("/WebSocketTest/privateChatServlet?username="+user+"&privatename="+privateTo);
}


</script>

</head>
<body>
	<div style="border:1px solid #000;width:700px;height:600px;">
		<div style="float:left;width:70%;height:100%;">
			<div style=" margin-left:10px;width:90%;height:5%;">
				<H3>${sessionScope.user }</H3>
			</div>
			<div id="content" style="margin:10px;border:1px solid #000;width:90%;height:50%;">
				
			
			</div>
			<div style="margin:10px;width:90%;height:28%;">
				<textarea id="write" style="width:99%;height:99%;resize:none;"></textarea>
			
			</div>
			<button onclick="send();" style="float:right;margin-right:40px;">Send</button>
		</div>
		<div style="float:left;width:28%;height:100%;">
			<div id="userList" style="margin:58px 10px 10px 0px;border:1px solid #000;width:100%;height:85%;">
				
			</div>
		</div>	
	
	
	
	
	
	</div>
</body>
</html>