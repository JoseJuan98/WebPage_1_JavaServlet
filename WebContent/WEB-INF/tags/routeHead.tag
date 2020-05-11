<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@tag import="es.unex.pi.model.Route"%>
<%@attribute name="route" required="true" type="es.unex.pi.model.Route"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>

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