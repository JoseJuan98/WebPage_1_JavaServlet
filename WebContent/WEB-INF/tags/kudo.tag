<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@tag import="es.unex.pi.model.Route"%>
<%@attribute name="route" required="true" type="es.unex.pi.model.Route"%>

<div class="kudo_div">
	<div class="kudo_icon">
		<a href="<c:url value='GiveKudoServlet.do?routeID=${route.id}'/>"><img class="kud_img" alt="Like Kudo"
			src="${pageContext.request.contextPath}/img/heart.svg"> </a>
	</div>
	<div class="kudo_icon">
		<a href="<c:url value='DisKudoServlet.do?routeID=${route.id}'/>"><img class="kud_img" alt="Dislike Kudo"
			src="${pageContext.request.contextPath}/img/dislike.svg"> </a>
	</div>
</div>