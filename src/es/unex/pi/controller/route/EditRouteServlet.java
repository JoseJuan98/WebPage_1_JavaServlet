package es.unex.pi.controller.route;

import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
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

/**
 * Servlet implementation class EditRouteServlet
 */
@WebServlet("/EditRouteServlet.do")
public class EditRouteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditRouteServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("Handling GET EditRouteServlet");
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
				
				session.setAttribute("routeA", rt);
				
				request.setAttribute("CheckTypeRoute", "Edit");
				RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/RouteSettings.jsp");
				view.forward(request, response);
			} else {
				request.setAttribute("CheckTypeRoute", "Create");
				request.setAttribute("errormsg", "There is an error with editing this route, try to login again.");
				RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/RouteSettings.jsp");
				view.forward(request, response);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("Handling GET EditRouteServlet");
		HttpSession session = request.getSession();

		if (session.getAttribute("user") == null) {
			response.sendRedirect("LoginServlet.do");
		} else {
			request.setAttribute("CheckTypeRoute", "Edit");

			Connection conn = (Connection) getServletContext().getAttribute("dbConn");
			RouteDAO routeDao = new JDBCRouteDAOImpl();
			routeDao.setConnection(conn);

			RoutesCategoriesDAO routCatDao = new JDBCRoutesCategoriesDAOImpl();
			routCatDao.setConnection(conn);

			// To get all the values that you can not in the form, like kudos or blocked
			Route route = (Route) session.getAttribute("routeA");
			if (route != null) {
				session.removeAttribute("routeA");

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

				logger.info("Hangling new route date and time: " + route.getDate() + " categories: "
						+ Arrays.toString(cat));

				if (cat != null) {
					routeDao.save(route);
					// Delete all the previous categories to save the new ones
					routCatDao.deleteByRoute(route.getId());

					int i = 0;
					RoutesCategories rc = new RoutesCategories();
					while (i < cat.length) {
						rc.setIdct(Long.parseLong(cat[i]));
						rc.setIdr(route.getId());

						routCatDao.add(rc);
						i++;
					}

					session.setAttribute("routeID", route.getId());
					
					
					
					response.sendRedirect(request.getContextPath() + "/CheckRouteServlet.do");

				} else {
					CategoryDAO catDao = new JDBCCategoryDAOImpl();
					catDao.setConnection(conn);
					List<Category> catList = catDao.getAll();
					request.setAttribute("catList", catList);
					request.setAttribute("errormsg", "Please select a category.");
					request.setAttribute("route", route);
					RequestDispatcher view = request.getRequestDispatcher("WEB-INF/RouteSettings.jsp");
					view.forward(request, response);
				}
			} else {
				RequestDispatcher view = request.getRequestDispatcher("WEB-INF/Login.jsp");
				view.forward(request, response);
			}
		}
	}
}
