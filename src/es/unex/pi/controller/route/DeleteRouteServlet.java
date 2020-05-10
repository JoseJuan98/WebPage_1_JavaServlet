package es.unex.pi.controller.route;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.unex.pi.dao.CategoryDAO;
import es.unex.pi.dao.JDBCCategoryDAOImpl;
import es.unex.pi.dao.JDBCRouteDAOImpl;
import es.unex.pi.dao.JDBCRoutesCategoriesDAOImpl;
import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.RouteDAO;
import es.unex.pi.dao.RoutesCategoriesDAO;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.model.Category;
import es.unex.pi.model.Route;
import es.unex.pi.model.RoutesCategories;
import es.unex.pi.model.User;

/**
 * Servlet implementation class DeleteRouteServlet
 */
@WebServlet("/DeleteRouteServlet.do")
public class DeleteRouteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteRouteServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("Handling GET DeleteRouteServlet");
		HttpSession session = request.getSession();

		if (session.getAttribute("user") == null)
			response.sendRedirect("LoginServlet.do");
		else {
			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			RouteDAO rtDao = new JDBCRouteDAOImpl();
			rtDao.setConnection(conn);

			Route rt;
			rt = rtDao.get(Long.parseLong(request.getParameter("routeID")));

			CategoryDAO catDao = new JDBCCategoryDAOImpl();
			catDao.setConnection(conn);

			List<Category> catList = catDao.getAll();
			request.setAttribute("catList", catList);

			if (rt != null) {
				RoutesCategoriesDAO rtCtDao = new JDBCRoutesCategoriesDAOImpl();
				rtCtDao.setConnection(conn);

				List<RoutesCategories> catRtList = rtCtDao.getAllByRoute(rt.getId());

				request.setAttribute("catRtList", catRtList);
				request.setAttribute("route", rt);
				
				session.setAttribute("routeD", rt);
				
				request.setAttribute("CheckTypeRoute", "Delete");
				RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/RouteSettings.jsp");
				view.forward(request, response);
			} else {
				request.setAttribute("CheckTypeRoute", "Create");
				request.setAttribute("errormsg", "There is an error editing this route, try to login again.");
				RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/RouteSettings.jsp");
				view.forward(request, response);
			}
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
		
		RouteDAO rtDao = new JDBCRouteDAOImpl();
		rtDao.setConnection(conn);
		
		RoutesCategoriesDAO routCatDao = new JDBCRoutesCategoriesDAOImpl();
		routCatDao.setConnection(conn);

		String username = request.getParameter("usern");
		logger.info("User requesting delete with  username (" + username + "). ");

		HttpSession session = request.getSession();
		User user = new User();
		user = (User) session.getAttribute("user");

		Route route = new Route();
		route = (Route) session.getAttribute("routeD");

		if (user != null) {
			String passw2 = request.getParameter("passw2");
			String title = request.getParameter("title");
			logger.info("Checking route with title : " + route.getTitle() + " description: " + route.getDescription()
					+ " and user password: " + passw2);
			if (route != null && route.getTitle().equals(title)) {
				String passw = user.getPassword();
				request.setAttribute("CheckType", "Delete");
				// Password is stored only if both passwords matches
				if (passw != null && passw.equals(passw2)) {

					logger.info("Confirmed user to delete with session id: " + session.getId());

					if (user != null) {

						// Delete all the Routecategories belonging to this route
						routCatDao.deleteByRoute(route.getId());
						logger.info("Route with id " + route.getId() + " is going to be deleted.");
						// Delete the route by using the DAO Object
						// Remove the route attribute from the session
						rtDao.delete(route.getId());

						route = null;
						session.removeAttribute("routeD");

						response.sendRedirect(request.getContextPath() + "/ListRoutesServlet.do");
					}
				} else {
					logger.info("Fail deleting user with username: " + request.getParameter("title")
							+ ". Passwords didn't match");
					
					request.setAttribute("errormsg", "Passwords didn't match. Try again.");
					
					RoutesCategoriesDAO rtCtDao = new JDBCRoutesCategoriesDAOImpl();
					rtCtDao.setConnection(conn);

					List<RoutesCategories> catRtList = rtCtDao.getAllByRoute(route.getId());

					request.setAttribute("catRtList", catRtList);
					request.setAttribute("route", route);
					
					session.setAttribute("routeD", route);
					
					request.setAttribute("CheckTypeRoute", "Delete");
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/RouteSettings.jsp");
					view.forward(request, response);
				}
			} else {
				logger.info("Fail deleting route with title: " + route.getTitle()
				+ ". Route is not in the session");
				
				request.setAttribute("errormsg", "Session error. Try again.");
				request.setAttribute("CheckType", "Edit");
				
				CategoryDAO catDao = new JDBCCategoryDAOImpl();
				catDao.setConnection(conn);
				List<Category> catList = catDao.getAll();
				request.setAttribute("catList", catList);
				
				request.setAttribute("route", route);
				
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/RouteSettings.jsp");
				view.forward(request, response);
			}
		} else {
			request.setAttribute("errormsg", "Session Expired or User is not longer on the DB.");
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
			view.forward(request, response);
		}
	}

}
