<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Login</title>
  <link href="/resources/css/login.css" rel="stylesheet" type="text/css">  
  <link href="/resources/css/index.css" rel="stylesheet" type="text/css">  
</head>
<body>
<div class="container">
<p> ${status}</p>
  <div class="loginpage card">
    <div class="socialloginlinks">
	  <h1>Login</h1>
	  <div class="sociallinks">
	  <a href="/adminlogin">Admin Login!</a>
	  <div class="googleloginbtn" type="submit"><a href="/googleLogin">Login with Google!</a></div>
	   <a href="/register">Sign Up!</a></div>
	  </div>
    <form action="/loginUser" method="POST">
      <div class="loginform">
        <%--@declare id="emailid"--%><%--@declare id="password"--%><label for="emailid"><b>Email ID</b></label>
        <input type="text" name="emailid" placeholder="abc@example.com" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required/>
	    <label for="password"><b>Password</b></label>
	    <input type="password" name="password" placeholder="Password" required/>
	    <button type="submit" class="loginbtn">Login</button>
      </div>
    </form>
  </div>
  </div>
</body>
</html>