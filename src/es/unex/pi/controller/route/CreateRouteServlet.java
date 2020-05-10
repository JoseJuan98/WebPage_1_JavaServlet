package es.unex.pi.controller.route;

import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Iterator;
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
import es.unex.pi.dao.RouteDAO;
import es.unex.pi.dao.RoutesCategoriesDAO;
import es.unex.pi.model.Category;
import es.unex.pi.model.Route;
import es.unex.pi.model.RoutesCategories;
import es.unex.pi.model.User;

/**
 * Servlet implementation class CreateRouteServlet
 */
@WebServlet("/CreateRouteServlet.do")
public class CreateRouteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateRouteServlet() {
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

			request.setAttribute("CheckTypeRoute", "Create");

			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			CategoryDAO catDao = new JDBCCategoryDAOImpl();
			catDao.setConnection(conn);

			List<Category> catList = catDao.getAll();
			request.setAttribute("catList", catList);

			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/RouteSettings.jsp");
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
	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		logger.info("Handling POST");
		// Set request parameters again to display the correct JSP code
		request.setAttribute("CheckTypeRoute", "Create");

		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		CategoryDAO catDao = new JDBCCategoryDAOImpl();
		catDao.setConnection(conn);

		List<Category> catList = catDao.getAll();
		request.setAttribute("catList", catList);
		// Set connection for the Routes
		RouteDAO routeDao = new JDBCRouteDAOImpl();
		routeDao.setConnection(conn);

		RoutesCategoriesDAO routCatDao = new JDBCRoutesCategoriesDAOImpl();
		routCatDao.setConnection(conn);

		HttpSession session = request.getSession();
		User user = new User();
		user = (User) session.getAttribute("user");

		request.setAttribute("CheckTypeRoute", "Create");

		// I want to check if this user created this route before
		List<Route> rtCheckList = routeDao.getAllByUser(user.getId());

		boolean found = false;

		String title = request.getParameter("title");

		if (!rtCheckList.isEmpty()) {
			Iterator<Route> itRt = rtCheckList.iterator();
			while (itRt.hasNext()) {
				Route aux = itRt.next();
				if (aux.getTitle().equals(title)) {
					found = true;
				}
			}
		}

		if (user != null) {
			Route route = new Route();

			route.setIdu(user.getId());
			route.setBlocked(0); // By default they are available
			route.setKudos(0);
			route.setTitle(title);
			route.setDescription(request.getParameter("desc"));
			route.setDistance(Float.parseFloat(request.getParameter("dist")));
			route.setDurationH(Integer.parseInt(request.getParameter("durationH")));
			route.setDurationM(Integer.parseInt(request.getParameter("durationM")));
			route.setElevation(Integer.parseInt(request.getParameter("elevation")));

			String date = request.getParameter("date");
			String time = request.getParameter("time");
			route.setDate(date.concat(" " + time));
			route.setDifficulty(Integer.parseInt(request.getParameter("difficulty")));

			String[] cat = request.getParameterValues("category");

			logger.info(
					"Hangling new route date and time: " + route.getDate() + " categories: " + Arrays.toString(cat));

			if (found == false && cat != null) {
				routeDao.add(route);

				// After inserting we take the new id that is given to the route by the DB.
				// (there's a constraint which only different users can use the same title)
				found = false;
				rtCheckList = routeDao.getAllByUser(user.getId());
				Iterator<Route> itRouts = rtCheckList.iterator();

				Long routeID = (long) 0;
				found = false;
				while (itRouts.hasNext() && found == false) {
					Route aux = itRouts.next();
					if (aux.getTitle().equals(title)) {
						found = true;
						routeID = aux.getId();
					}
				}

				if (found) {

					int i = 0;
					RoutesCategories rc = new RoutesCategories();
					while (i < cat.length) {
						rc.setIdct(Long.parseLong(cat[i]));
						rc.setIdr(routeID);

						routCatDao.add(rc);
						i++;
					}
					route = null;
					route = routeDao.get(routeID);

					session.setAttribute("routeID", routeID);
					response.sendRedirect(request.getContextPath() + "/CheckRouteServlet.do");

				} else {
					routeDao.delete(routeID);
					request.setAttribute("errormsg",
							"DB couldn't assign an id to the route. Try to logout and login again.");
					logger.info("DB couldn't assign an id to the route with title " + route.getTitle());
				}
			} else {
				// Session store route to edit the title and make another route and delete route
				// with error
				if (cat == null) {
					request.setAttribute("errormsg", "Please select a category.");
				} else {
					request.setAttribute("errormsg",
							"You created already this route. Try to update the information instead of creating a new one.");
				}
				request.setAttribute("route", route);

				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/RouteSettings.jsp");
				view.forward(request, response);
			}
		} else {
			response.sendRedirect(request.getContextPath() + "/LoginServlet.do");
		}
	}
}
