package es.unex.pi.controller.user;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.unex.pi.model.*;
import es.unex.pi.dao.*;

/**
 * Servlet implementation class DeletUserServlet
 */
@WebServlet("/DeletUserServlet.do")
public class DeletUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeletUserServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("Handling GET");

		HttpSession session = request.getSession();

		if (session.getAttribute("user") != null) {
			// Setting parameters for JSP
			request.setAttribute("CheckType", "Delete");
			request.setAttribute("errormsg", "Are you sure that you want to delete your account?");

			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/UserSettings.jsp");
			view.forward(request, response);
		} else {
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/Login.jsp");
			view.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);

		String username = request.getParameter("usern");
		logger.info("User requesting delete with  username (" + username + "). ");

		HttpSession session = request.getSession();
		User user = new User();
		user = (User) session.getAttribute("user");
		
		if (user != null) {
			logger.info("Checking user with username: " + user.getUsername() + " email: " + user.getEmail()
					+ " and password: " + user.getPassword());
			String passw = request.getParameter("passw");
			String passw2 = request.getParameter("passw2");
			
			request.setAttribute("CheckType", "Delete");
			// Password is stored only if both passwords matches
			if (passw != null && passw.equals(passw2)) {
				
				logger.info("Confirmed user to delete with session id: "+session.getId());
				
				if (user!=null) {
					logger.info("User with id "+ user.getId()+" is going to be deleted.");
					//Delete the user by using the DAO Object
					//Remove the user attribute from the session
					userDao.delete(user.getId());
								
					user=null;
					session.removeAttribute("user");
					
					response.sendRedirect(request.getContextPath()+"/LogoutServlet.do");
				}
			} else {
				logger.info("Fail deleting user with username: " + request.getParameter("usern")
						+ ". Passwords didn't match");
				request.setAttribute("errormsg", "Passwords didn't match. Try again.");
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/UserSettings.jsp");
				view.forward(request, response);
			}
		} else {
			request.setAttribute("errormsg", "Session Expired or User is not longer on the DB.");
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
			view.forward(request, response);
		}
	}

}
