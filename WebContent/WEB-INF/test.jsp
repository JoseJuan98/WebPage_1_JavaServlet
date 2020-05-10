<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div class="imgs_frame">
		
		<c:forEach var="category" items="${catRtList}"> 
		<c:choose>
				<c:when test="${catRt.idct == 0}"> <img alt="Mountain Bike" src="${pageContext.request.contextPath}/img/ciclying1.jpeg"></c:when>
				<c:when test="${catRt.idct == 1}"> <img alt="Cycling" src="${pageContext.request.contextPath}/img/cycling2.jpeg"></c:when>
				<c:when test="${catRt.idct == 2}"> <img alt="Running" src="${pageContext.request.contextPath}/img/running1.jpeg"></c:when>
				<c:when test="${catRt.idct == 3}"> <img alt="Walking" src="${pageContext.request.contextPath}/img/hike1.jpeg"></c:when>
				<c:when test="${catRt.idct == 4}"> <img alt="Walking" src="${pageContext.request.contextPath}/img/swim1.jpeg"></c:when>
			</c:choose>
		</c:forEach>
	</div>
</body>
</html>