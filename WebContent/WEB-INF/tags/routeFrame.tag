<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@tag import="es.unex.pi.model.Route"%>
<%@attribute name="route" required="true" type="es.unex.pi.model.Route"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

<div class="user_routes" <c:choose>
			<c:when test="${route.blocked=='1'}">
				id="block_div"
			 </c:when>
		</c:choose>>
		
	<t:routeHead route="${route}"></t:routeHead>
	
	<t:routeInfo route="${route}"></t:routeInfo>

	
		<c:choose>
		<c:when test="${userType ne 'NoUser'}">
			<t:kudo route="${route}"></t:kudo>
		</c:when>
	</c:choose> <p> ${route.kudos} </p>
	<c:if test="${CheckTypeFrame eq 'MainPage'}">
		<p class="see">
			<a class="logout_home"
				href="<c:url value='CheckRouteServlet.do?routeID=${route.id}'/>">See
			</a>
		</p>
	</c:if>
</div>

