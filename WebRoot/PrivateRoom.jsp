<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Private Room</title>

<script type="text/javascript">

var user='${sessionScope.user }';
var privateTo = '${sessionScope.privateTo }';
var target="ws://localhost:8080/WebSocketTest/privatechat?currentUser="+user+"&privateTo="+privateTo;

window.onload= init;
var ws;
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
			
		}
		if(msg.type==2) //talk
		{
			showMsg(msg);
		}
		if(msg.type==3) //logout
		{
			showLogout(msg);
		
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



function send(){
	var  write= document.getElementById("write");
	var  temp="{from:'"+user+"',content:'"+write.value+"',privateTo:'"+ privateTo +"'}";
	ws.send(temp);
	write.value = '';
}

function jumpToPrivate(privateTo) {
	location.href=""
}


</script>

</head>
<body>
	<div style="border:1px solid #000;width:500px;height:600px;">
		<div style="float:left;width:100%;height:100%;">
			<div style=" margin-left:10px;width:90%;height:5%;">
				<H3>${sessionScope.user } PRIVATE TALK TO ${sessionScope.privateTo } </H3>
			</div>
			<div id="content" style="margin:10px;border:1px solid #000;width:90%;height:50%;">
				
			
			</div>
			<div style="margin:10px;width:90%;height:28%;">
				<textarea id="write" style="width:99%;height:99%;resize:none;"></textarea>
			
			</div>
			<button onclick="send();" style="float:right;margin-right:40px;">Send</button>
		</div>
	
	
	
	</div>
</body>
</html>