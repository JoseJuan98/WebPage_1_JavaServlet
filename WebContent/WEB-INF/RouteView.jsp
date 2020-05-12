<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/milligram.css" />
<title>Route ${route.title}</title>
</head>
<body>
	<c:choose>
		<c:when test="${userType eq  'NoUser'}">
			<c:import url="/includes/navbarNoUser.jsp"></c:import>
		</c:when>
		<c:otherwise>
			<c:import url="/includes/navbar.jsp"></c:import>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${CheckTypeRtUser eq 'NoOwner'}">
			<h3>User ${userRt.username}</h3>
			<h4>Email: ${userRt.email}</h4>
		</c:when>
		<c:when test="${CheckTypeRtUser eq 'Owner'}">
			<a class="logout_home"
				href="<c:url value='EditRouteServlet.do?routeID=${route.id}'/>">Edit</a>
			<c:choose>
				<c:when test="${route.blocked=='1'}">
					<a class="route_bl"
						href="<c:url value='UnblockRouteServlet.do?routeID=${route.id}'/>">
						Unblock Route </a>
				</c:when>
				<c:otherwise>
					<a class="route_bl"
						href="<c:url value='BlockRouteServlet.do?routeID=${route.id}'/>">Block
						Route</a>
				</c:otherwise>
			</c:choose>
		</c:when>
	</c:choose>
	<div class="rtView"
		<c:choose>
				<c:when test="${route.blocked=='1'}">
					id="block_div"
				 </c:when>
			</c:choose>>
		<div class="rtView_img_div">
			<t:routeHead route="${route}"></t:routeHead>
		</div>
		<div class="rtView_info_div">
			<t:routeInfo route="${route}"></t:routeInfo>
		</div>

		<c:choose>
			<c:when test="${userType ne 'NoUser'}">
				<t:kudo route="${route}"></t:kudo>
			</c:when>
		</c:choose>
		<p>${route.kudos}</p>

		<c:choose>
			<c:when test="${userType eq  'NoUser'}">
				<a class="logout_h" href="<c:url value='LoginServlet.do'/>">Login
					for kudos.</a>
			</c:when>
		</c:choose>
	</div>
</body>
</html>