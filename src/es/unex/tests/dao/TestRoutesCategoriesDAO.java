package es.unex.tests.dao;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.sql.Connection;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.unex.pi.dao.RoutesCategoriesDAO;
import es.unex.pi.dao.JDBCRoutesCategoriesDAOImpl;
import es.unex.pi.model.RoutesCategories;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestRoutesCategoriesDAO {

	static DBConn dbConn;
	static RoutesCategoriesDAO routesCategoriesDAO;
	static Connection conn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbConn = new DBConn();
		conn = dbConn.create();
	    routesCategoriesDAO = new JDBCRoutesCategoriesDAOImpl();
		routesCategoriesDAO.setConnection(conn);
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		dbConn.destroy(conn);
	}

	@Before
	public void setUpBeforeMethod() throws Exception {
	
	}

	@Test
	public void test1BaseData() {
		
		List<RoutesCategories> routesCategoriesList = routesCategoriesDAO.getAll();
		
		RoutesCategories routesCategories = routesCategoriesDAO.get(0,0);
		
		assertEquals(routesCategories.getIdr(),0);
		assertEquals(routesCategories.getIdct(),0);
		
		assertEquals(routesCategoriesList.get(0).getIdr(),routesCategories.getIdr());			
			
	}
	
	@Test
	public void test2BaseDataByCategory() {
		
		List<RoutesCategories> routesCategoriesList = routesCategoriesDAO.getAllByCategory(4);
		for(RoutesCategories routesCategories: routesCategoriesList)			
			assertEquals(routesCategories.getIdct(),4);			
	}
	
	@Test
	public void test3BaseDataByRoute() {
		
		List<RoutesCategories> routesCategoriesList = routesCategoriesDAO.getAllByRoute(0);
		for(RoutesCategories routesCategories: routesCategoriesList)			
			assertEquals(routesCategories.getIdr(),0);			
	}
	
	@Test
	public void test4Add(){
		RoutesCategories routesCategories01 = new RoutesCategories();
		routesCategories01.setIdr(3);
		routesCategories01.setIdct(0);
		routesCategoriesDAO.add(routesCategories01);
		
		RoutesCategories routesCategories02 = routesCategoriesDAO.get(3,0);
		
		assertEquals(3,routesCategories02.getIdr());
		assertEquals(0,routesCategories02.getIdct());
				
	}
	
	
	@Test
	public void test5Modify(){
		
		RoutesCategories routesCategories01 = routesCategoriesDAO.get(3,0);
		routesCategories01.setIdct(1);
		routesCategoriesDAO.save(routesCategories01);
		
		RoutesCategories routesCategories02 = routesCategoriesDAO.get(3,1);
		assertEquals(3,routesCategories02.getIdr());
		assertEquals(1,routesCategories02.getIdct());
	}
	
	@Test
	public void test6Delete(){
		 
		routesCategoriesDAO.delete(3,1);
		List<RoutesCategories> routesCategoriesList = routesCategoriesDAO.getAll();
		 
		 RoutesCategories routesCategories01 = new RoutesCategories();
		 routesCategories01.setIdr(3);
		 routesCategories01.setIdct(1);
		 		 
		for(RoutesCategories routesCategories: routesCategoriesList) {
				assertNotEquals(routesCategories,routesCategories01);
		}
		 
	}

}
