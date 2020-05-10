<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html id="list">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/milligram.css" />
<title>List of routes</title>
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

	<h1 class="title_m">Welcome ${user.getUsername()} to BHike!</h1>
	<c:import url="/includes/filterSearch.jsp"></c:import>
	<h1>Different Lists:</h1>
	<h2>List of routes:</h2>
	<table class="list_routes_tab">
		<thead>
			<tr>
				<th>Title</th>
				<th>Description</th>
				<th>Distance (Km)</th>
				<th>Elevation (m)</th>
				<th>Duration (H:m)</th>
				<th>Kudos</th>
				<th>Difficulty</th>
				<th>Date</th>
				<th>Blocked</th>
				<th>User</th>
				<th>Category Ids</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="route" items="${routesList}">
				<tr>
					<td>${route.first.title}</td>
					<td>${route.first.description}</td>
					<td>${route.first.distance}</td>
					<td>${route.first.elevation}</td>
					<td><c:choose>
							<c:when test="${route.first.durationH=='0'}">${route.first.durationM}'</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${route.first.durationM=='0'}">${route.first.durationH}h</c:when>
									<c:otherwise>${route.first.durationH}h ${route.first.durationM}'</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose></td>
					<td>${route.first.kudos}</td>
					<td><c:choose>
							<c:when test="${route.first.getDifficulty()=='1'}">
		    					Easy
		    				</c:when>
							<c:when test="${route.first.getDifficulty()=='2'}">
		    					Medium
		    				</c:when>
							<c:when test="${route.first.getDifficulty()=='3'}">
		    					Difficult
		    				</c:when>
							<c:otherwise> - </c:otherwise>
						</c:choose></td>
					<td>${route.first.date}</td>
					<td><c:choose>
							<c:when test="${route.first.blocked=='1'}">
		    					Yes
		    				</c:when>
							<c:otherwise>
		    					No
		    				</c:otherwise>
						</c:choose></td>
					<td>${route.second.username}</td>
					<td><c:forEach var="category" items="${route.third}">
							${category.idct} - 		    	
							</c:forEach></td>
					<td><a class="logout_home"
						href="<c:url value='CheckRouteServlet.do?routeID=${route.first.id}'/>">See
					</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<h2>List of routes by users:</h2>
	<c:forEach var="routesByUser" items="${usersMap}">
		<h3>User: ${routesByUser.key.username}</h3>
		<h3>Email: ${routesByUser.key.email}</h3>
		<div class="user_container">
			<c:forEach var="route" items="${routesByUser.value}">
				<t:routeFrame route="${route}"></t:routeFrame>
			</c:forEach>
		</div>
		<c:if test="${BlockedRtDisplay ne 'Hide'}">
			<div class="blocked_container">
				<c:forEach var="route" items="${routesByUser.value}">
					<c:if test="${route.blocked == '1'}">
						<t:routeFrame route="${route}"></t:routeFrame>
					</c:if>
				</c:forEach>
			</div>
		</c:if>
	</c:forEach>
</body>
</html>
