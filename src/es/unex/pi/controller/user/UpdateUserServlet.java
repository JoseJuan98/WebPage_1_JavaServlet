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

import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.User;

/**
 * Servlet implementation class UpdateUserServlet
 */
@WebServlet("/UpdateUserServlet.do")
public class UpdateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateUserServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("Handling GET UpdateServlet");
		HttpSession session = request.getSession();

		if (session.getAttribute("user") == null)
			response.sendRedirect("LoginServlet.do");
		else {
			request.setAttribute("user", session.getAttribute("user"));
			request.setAttribute("CheckType", "Update");
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/UserSettings.jsp");
			view.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		logger.info("Handling POST");

		// Set connection
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);

		User user = new User();
		HttpSession session = request.getSession();
		request.setAttribute("CheckType", "Update");
		
		String usern = request.getParameter("usern");
		user = userDao.get(usern);
		if (user != null && user.getUsername() != null) {
			logger.info("Updating user with username: " + user.getUsername() + " email: " + user.getEmail()
					+ " and password: " + user.getPassword());
			String passw = request.getParameter("passw");
			String passw2 = request.getParameter("passw2");

			// Password is stored only if both passwords matches
			if (passw != null && passw.equals(passw2)) {
				user = new User();
				logger.info("Creating user with username: " + request.getParameter("usern") + " email: "
						+ request.getParameter("email") + " and password: " + passw);
				// Obtain parameter of form
				user.setUsername(usern);
				user.setEmail(request.getParameter("email"));
				user.setPassword(passw);

				logger.info("Updating user with username: " + user.getUsername() + " email: " + user.getEmail()
						+ " and password: " + user.getPassword());

				// Update user in the DB
				userDao.save(user);
				// Changing the current user
				session.removeAttribute("user");
				session.setAttribute("user", user);
				
				response.sendRedirect("LoginServlet.do");
			} else {
				logger.info("Fail creating user with username: " + request.getParameter("usern")
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
