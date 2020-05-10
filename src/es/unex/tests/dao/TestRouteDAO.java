package es.unex.tests.dao;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import es.unex.pi.dao.RouteDAO;
import es.unex.pi.dao.JDBCRouteDAOImpl;
import es.unex.pi.model.Route;

import org.junit.Test;



@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestRouteDAO {

	static DBConn dbConn;
	static RouteDAO routeDAO;
	static Connection conn;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		dbConn = new DBConn();
		conn = dbConn.create();
	    routeDAO = new JDBCRouteDAOImpl();
		routeDAO.setConnection(conn);
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
		
		Route route01 = routeDAO.get(0);
		assertEquals(route01.getId(),0);
		assertEquals(route01.getTitle(),"Pericuto");

		Route route02 = routeDAO.get(1);
		assertEquals(route02.getId(),1);
		assertEquals(route02.getTitle(),"Chorrito de Ovejuela");
		
		Route route03 = routeDAO.get(2);
		assertEquals(route03.getId(),2);
		assertEquals(route03.getTitle(),"Circuito natación Triatlón Cáceres");
		
		Route route04 = routeDAO.get(3);
		assertEquals(route04.getId(),3);
		assertEquals(route04.getTitle(),"Trabuquete");
		
		Route route05 = routeDAO.get(4);
		assertEquals(route05.getId(),4);
		assertEquals(route05.getTitle(),"Risco");
		
	}
	
	
	@Test
	public void test2Add(){
		Route route01 = new Route();
		route01.setTitle("newRoute");
		route01.setDescription("new description");
		Date date = new Date();
		route01.setDate(date.toString());
		route01.setDistance(40);
		route01.setElevation(200);
		route01.setIdu(0);
		route01.setKudos(10);
		route01.setBlocked(0);
		
		routeDAO.add(route01);
		
		Route route02 = routeDAO.getAllBySearchTitle("newRoute").iterator().next();
		assertEquals(route01.getDescription(),route02.getDescription());
	}
	
	@Test
	public void test3Modify(){
		Route route01 = routeDAO.getAllBySearchTitle("newRoute").iterator().next();
		route01.setTitle("newRouteUpdated");
		route01.setDescription("new description updated");
		routeDAO.save(route01);
		
		Route route02 = routeDAO.getAllBySearchTitle("newRouteUpdated").iterator().next();
		assertEquals(route01.getDescription(),route02.getDescription());
		
		routeDAO.getAll();
	}
	
	@Test
	public void test4Delete(){
		
		 Route route01 = routeDAO.getAllBySearchTitle("newRouteUpdated").iterator().next();
		 long idRoute= route01.getId();
		 routeDAO.delete(idRoute);
		 
		 Route route02 = routeDAO.get(idRoute);
		 assertEquals(null,route02);
		 
		routeDAO.getAll();
	}

}
