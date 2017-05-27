package servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PrivateChatServlet extends HttpServlet   {
public void service(HttpServletRequest request, HttpServletResponse response) {
		
		String privatename= request.getParameter("privatename");
		String username= request.getParameter("username");
		
		HttpSession  session = request.getSession();
		session.setAttribute("user", username);
		session.setAttribute("privateTo", privatename);
		try {
			response.sendRedirect("PrivateRoom.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
