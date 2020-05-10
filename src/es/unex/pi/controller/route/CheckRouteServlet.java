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

import es.unex.pi.dao.JDBCRouteDAOImpl;
import es.unex.pi.dao.JDBCRoutesCategoriesDAOImpl;
import es.unex.pi.dao.JDBCUserDAOImpl;
import es.unex.pi.dao.RouteDAO;
import es.unex.pi.dao.RoutesCategoriesDAO;
import es.unex.pi.dao.UserDAO;
import es.unex.pi.listener.ServletContextListener;
import es.unex.pi.model.Route;
import es.unex.pi.model.RoutesCategories;
import es.unex.pi.model.User;

/**
 * Servlet implementation class CheckRouteServlet
 */
@SuppressWarnings("unused")
@WebServlet("/CheckRouteServlet.do")
public class CheckRouteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckRouteServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("Handling GET");
		// To get the Route view it is need first to set the routeID by the session

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		RouteDAO routeDao = new JDBCRouteDAOImpl();
		routeDao.setConnection(conn);

		Route rt = new Route();
		if (request.getParameter("routeID") != null) {
			rt = routeDao.get(Long.parseLong(request.getParameter("routeID")));
		} else if(session.getAttribute("routeID") != null){
			rt = routeDao.get((Long) session.getAttribute("routeID"));
			session.removeAttribute("routeID");
		}else if(request.getAttribute("routeID") != null){
			Long rStr = (Long) request.getAttribute("routeID");
			rt = routeDao.get(rStr);
		}else {
			response.sendRedirect(request.getContextPath() + "/LoginServlet.do");
		}
		//logger.info("Route id user is " + rt.getIdu() + " and user session is" + user.getId());

		if (rt != null ) {
			if (user != null && rt.getIdu() == user.getId()) {
				request.setAttribute("CheckTypeRtUser", "Owner");
			} else {
				request.setAttribute("CheckTypeRtUser", "NoOwner");
				UserDAO userDao = new JDBCUserDAOImpl();
				userDao.setConnection(conn);

				User userRt = new User();
				userRt = userDao.get(rt.getIdu());
				request.setAttribute("userRt", userRt);
			}
			
			if(user == null) request.setAttribute("userType", "NoUser");
			request.setAttribute("route", rt);
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/RouteView.jsp");
			view.forward(request, response);
		} else {
			RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
			view.forward(request, response);
		}
	}

}
