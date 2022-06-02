<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
	<title>Save Course</title>
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/style.css" />
	<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/add-customer-style.css" />
</head>
<body>
	<div id="wrapper">
		<div id="header">
			<h2>Student Hub</h2>
		</div>
	</div>
	<div id="container">
		<h3>Save Course</h3>
		
			<form action="saveCourse" method="get">
		<input type="text" name="course" placeholder="What's your course name" />
		
		<input type="submit" />
	
	</form>
		<div style="clear; both;"></div>
		<p>
			<a href="${pageContext.request.contextPath }/student/list"> Back to List</a>
		</p>
	</div>
</body>
</html>