package servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet  {
	public void service(HttpServletRequest request, HttpServletResponse response) {
		
		String username= request.getParameter("username");
		
		HttpSession  session = request.getSession();
		session.setAttribute("user", username);
		
		try {
			response.sendRedirect("ChatRoom.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
