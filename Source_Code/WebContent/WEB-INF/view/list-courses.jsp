<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>List of courses</title>

<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/style.css" />
</head>
<body>
	<div id="wrapper">
		<div id="header">
			<h2>Student Hub</h2>
		</div>
	</div>
	<div id="container">
		<div id="content">
		<!-- put new button add customer -->
			<input type="button" value="Add Course" 
				   onclick="window.location.href='showFormForAdd';return false;"
				   class="add-button"/>
		
		</div>
		
		<div id="container">
		<div id="content">
		<h3>Enrolled Courses</h3>
		<c:forEach var="course" items="${courses }">
			<ul>
			  <li>${course}</li>
			</ul>
		</c:forEach>
		
		</div>
		
		
	</div>
	
</body>
</html>