<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="nav_div">
	<ul>
		<li><a href="<c:url value='ListRoutesServlet.do'/>">Home</a></li>
		<li class="search_li">
			<form class="form_nav" method="get" action="SearchServlet.do">
				<input type="search" name="search" class="search_input"
					placeholder="Search"> <input id="icon_search" type="submit"
					value="->">
			</form>
		</li>
		<li id="home_right_bar"><a class="logout_h"
			href="<c:url value='LoginServlet.do?mainPage=1'/>">Login</a></li>
	</ul>

</div>
<br>
<br>
<br>