<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
       <%@ page import ="com.cloud.ezcloudstore.model.Files" %>
       <%@ page import ="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin Panel</title>
<link href="/resources/css/dashboard.css" rel="stylesheet" type="text/css"> 
<style>
  tbody tr:nth-of-type(even) {
      background-color:#f1f1f1;
    }
</style>
</head>
<body>
<h2>Welcome ${name}!</h2>
<p>${message}</p>
<div class="logout">
<form action="/logout" method="POST">
 <button type="submit" class="logoutbtn">Logout</button>
</form>
</div>
<div>
<%
	if(session.getAttribute("files") != null)
 {
	 System.out.println("not null");
	 ArrayList<Files> filesArray = (ArrayList<Files>)session.getAttribute("files");
%>
 <table class="dashboardTable">
					<thead>
					<tr>
					<th>User</th>
						<th>File name</th>
						<th>Description</th>
						<th>File size</th>
						<th>Created Time</th>
						<th>Updated Time</th>
						<th>Download</th>
						<th>Delete</th>
									</tr>
					</thead>
					<tbody>


<%
	for(Files file : filesArray) 
{
%>
<tr>
<td><%out.println(file.getEmailId()); %></td>
	<td><%out.println(file.getFileName()); %></td>
	<td><%out.println(file.getDescription()); %></td>
	<td><%out.println(file.getFileSize()); %>KB</td>
	<td><%out.println(file.getCreatedTime()); %></td>
	<td><%out.println(file.getUpdatedTime()); %></td>

<td><button><a href="/downloadFile?fileUrl=<%=file.getFileUrl() %>&emailid=<%=file.getEmailId() %>">Download</a></button></td>
	<form action="/admindelete" method="POST">
	<input type="hidden" name="emailid" value="<%=file.getEmailId() %>">
	<input type="hidden" name="fileId" value="<%=file.getFileID() %>">
<%-- 	<input type="hidden" name="name" value="<%=session.getAttribute("name") %>"> --%>
		<input type="hidden" name="filename" value="<%=file.getFileName() %>">
<td>	<button >Delete</button></td>
	</form></tr>


<% } %>										
	 
  </tbody>

 
</table>
 <% }
 else
 { %>
	<div class="nofiles">
	<%  out.println("No Files Found"); %>
	</div>
 <%   } %>

</div>
</body>
</html>