<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@tag import="es.unex.pi.model.Route"%>
<%@attribute name="route" required="true" type="es.unex.pi.model.Route"%>


<div class="user_routes">
	<h4 class="title_us_route">${route.title}
		<c:if test="${route.blocked=='1'}"> ( BLOCKED )</c:if>
	</h4>
	<c:choose>
		<c:when test="${route.difficulty=='1'}">
			<img alt="Hike" class="img_frame"
				src="${pageContext.request.contextPath}/img/hike1.jpeg">
		</c:when>
		<c:when test="${route.difficulty=='2'}">
			<img alt="Swimming" class="img_frame"
				src="${pageContext.request.contextPath}/img/swim1.jpeg">
		</c:when>
		<c:when test="${route.difficulty=='3'}">
			<img alt="Mountain Bike" class="img_frame"
				src="${pageContext.request.contextPath}/img/ciclying1.jpeg">
		</c:when>
	</c:choose>
	<p>${route.description}</p>
	<p>${route.distance}Km
	${route.elevation}m
	${route.durationH}h${route.durationM}
	${route.date}</p>
	<p>
		<c:choose>
			<c:when test="${route.difficulty=='1'}">
			    					Easy
			    				</c:when>
			<c:when test="${route.difficulty=='2'}">
			    					Medium
			    				</c:when>
			<c:when test="${route.difficulty=='3'}">
			    					Difficult
			    				</c:when>
		</c:choose>
	</p>
	<p>
		<c:choose>
			<c:when test="${route.blocked=='1'}">
			    					Blocked
			    				</c:when>
			<c:otherwise>
			    					Available
			    				</c:otherwise>
		</c:choose>
	</p>
	<p>${route.kudos}</p>
	<c:if test="${CheckTypeFrame eq 'MainPage'}">
		<p class="see">
			<a class="logout_home"
				href="<c:url value='CheckRouteServlet.do?routeID=${route.id}'/>">See
			</a>
		</p>
	</c:if>
</div>

