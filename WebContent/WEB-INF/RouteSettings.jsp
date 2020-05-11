<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/milligram.css" />
    <title>
    <c:choose>
		<c:when test="${CheckTypeRoute eq 'Edit'}">BHike Edit Route</c:when>
		<c:when test="${CheckTypeRoute eq 'Delete'}">BHike Delete Route</c:when>
		<c:when test="${CheckTypeRoute eq 'Create'}">BHike Create New Route</c:when>
	</c:choose></title>
</head>

<body>
    <div class="div_settings">
        <h1 class="sett_h1">BHike</h1>
        <p id="error_log" class="login_field">${errormsg}</p>
            <c:choose>
                <c:when test="${CheckTypeRoute eq 'Edit'}"><p class="login_h1"> Replace the information. </p></c:when>
                <c:when test="${CheckTypeRoute eq 'Delete'}"><p class="login_h1"> Confirm password to proceed. </p></c:when>
                <c:when test="${CheckTypeRoute eq 'Create'}"><p class="login_h1"> Create a new route. </p></c:when>
            </c:choose>
     
        <form method="post" action=
                <c:choose>
	                <c:when test="${CheckTypeRoute eq 'Edit'}"> "EditRouteServlet.do" </c:when>
	                <c:when test="${CheckTypeRoute eq 'Delete'}"> "DeleteRouteServlet.do" </c:when>
	                <c:when test="${CheckTypeRoute eq 'Create'}"> "CreateRouteServlet.do" </c:when>
           	 	</c:choose>>          	 	

            <fieldset class="login_field">
                <input type="text" name="title" class="inputs" placeholder="Route Title" required value="${route.title}"
                <c:if test="${not empty route.getTitle()}"> readonly </c:if>><br>
                <input type="text" name="desc" class="inputs" placeholder="Description" required value="${route.description}"
                <c:if test="${CheckTypeRoute eq 'Delete'}"> readonly </c:if>><br>
                <input type="number" name="dist" class="inputs" placeholder="Distance (Km)" min="1" step="0.1" value="1" required value="${route.distance}"
                <c:if test="${CheckTypeRoute eq 'Delete'}"> readonly </c:if>><br>
                
                <div class="dur_time">
                	<div>
		                <label for="date" class="route_lab">Duration (HH:MM) </label>
		                <div class="date_div" > 
			                <input type="number" name="durationH" id="durH" class="inputs" placeholder="Hours" required value="${route.durationH}"
			                <c:if test="${CheckTypeRoute eq 'Delete'}"> readonly </c:if>><br>
			                <input type="number" name="durationM" id="durM" class="inputs" placeholder="Min" step="15" max="45" required value="${route.durationM}"
			                <c:if test="${CheckTypeRoute eq 'Delete'}"> readonly </c:if>><br>
		                </div>
	                </div>
	                <div class="datetime">
		                <label for="date" class="route_lab">Date and time of start.</label>
		                <div class="date_div" > 
			                <input type="date" name="date" id="date_route" class="inputs" placeholder="Date" required value="${route.getDateSimple()}"
			                <c:if test="${CheckTypeRoute eq 'Delete'}"> readonly </c:if>><br>
			                <input type="time" name="time" class="inputs" id="time_route" placeholder="Time (HH:mm)" required value="${route.getTimeSimple()}"
			                <c:if test="${CheckTypeRoute eq 'Delete'}"> readonly </c:if>><br>
		                </div>
	                </div>
                </div>
                
                <input type="number" name="elevation" class="inputs" placeholder="Elevation (m)" min="0" step="10" required value="${route.elevation}"
                <c:if test="${CheckTypeRoute eq 'Delete'}"> readonly </c:if>><br>
          
          		<label for="category" class="route_lab">Category of the route: </label>
          		<c:if test="${CheckTypeRoute eq 'Edit' or CheckTypeRoute eq 'Delete'}">
          			<p> You have selected before 
          			<c:forEach var="catRt" items="${catRtList}">
          				<c:choose>
	          				<c:when test="${catRt.idct == 0}"> Mountain biking</c:when>
							<c:when test="${catRt.idct == 1}"> Cycling</c:when>
							<c:when test="${catRt.idct == 2}"> Running</c:when>
							<c:when test="${catRt.idct == 3}"> Walking</c:when>
							<c:when test="${catRt.idct == 4}"> Swimming</c:when>
						</c:choose>
					</c:forEach>
          			</p>
          		</c:if>
          		<c:if test="${CheckTypeRoute != 'Delete'}">
				<select name="category" required multiple>
					<option value="">Please select</option>
					<c:forEach var="cat" items="${catList}">
					<option value="${cat.id}"> ${fn:toUpperCase(cat.name)}</option>
					</c:forEach>	
				</select>
				</c:if>
				<label for="difficulty" class="route_lab">Difficulty of the route: </label>
				<select name="difficulty" required >
					<option value="${route.difficulty}"><c:choose>
							<c:when test="${route.difficulty eq 1}"> Easy selected</c:when>
							<c:when test="${route.difficulty eq 2}"> Medium selected</c:when>
							<c:when test="${route.difficulty eq 3}"> Difficult selected</c:when>
							<c:otherwise> Please Select</c:otherwise>
					</c:choose></option>
					<c:if test="${CheckTypeRoute != 'Delete'}">
						<option value="1">Easy selected</option>
						<option value="2">Medium selected</option>
						<option value="3">Difficult selected</option>
					</c:if>
				</select>
				
				<!--  <input type="number" class="routeIdU" name="idU"  value="${user.id}">-->
				<c:if test="${CheckTypeRoute eq 'Delete'}">
                	<input type="password" name="passw2" class="inputs" placeholder="Confirm Password to proceed" required><br>
                </c:if>

            </fieldset>
            <div class="create_us">
                <input name="sub_log" type="submit" class="butt_log" value=
                <c:choose>
	                <c:when test="${CheckTypeRoute eq 'Edit'}"> "Edit" </c:when>
	                <c:when test="${CheckTypeRoute eq 'Delete'}"> "Confirm Delete" </c:when>
	                <c:when test="${CheckTypeRoute eq 'Create'}"> "Create route" </c:when>
           	 	</c:choose>>
           	 	 <a class="cancel_c" href=<c:choose>
	                <c:when test="${CheckTypeRoute eq 'Edit'}"><c:url value='CheckRouteServlet.do?routeID=${route.id}' /> </c:when>
	                <c:when test="${CheckTypeRoute eq 'Delete'}"><c:url value='EditRouteServlet.do?routeID=${route.id}' /> </c:when>
	                <c:when test="${CheckTypeRoute eq 'Create'}"><c:url value='ListRoutesServlet.do' /> </c:when>
           	 	</c:choose>> Cancel </a>
            </div>
             
        </form>
        <c:if test="${CheckTypeRoute eq 'Edit'}"> <a class="delete_account" href=<c:url value='DeleteRouteServlet.do?routeID=${route.id}' />>
                Delete Route </a> </c:if>
    </div>
</body>

</html>
