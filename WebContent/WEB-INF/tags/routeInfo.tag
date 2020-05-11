<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@tag import="es.unex.pi.model.Route"%>
<%@attribute name="route" required="true" type="es.unex.pi.model.Route"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

	<p>${route.description}</p>
	<p>${route.distance}Km(D) ${route.elevation}m(E)
		${route.durationH}h${route.durationM} ${route.date}</p>
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