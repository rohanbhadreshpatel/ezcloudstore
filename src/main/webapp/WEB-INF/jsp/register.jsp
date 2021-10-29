<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Sign Up</title>
<link href="/resources/css/register.css" rel="stylesheet" type="text/css">  
<link href="/resources/css/index.css" rel="stylesheet" type="text/css">  
</head>
<body>
<div class="container">
<p> ${status}</p>
  <div class="registerpage card">
    <div class="loginlinks">
	  <h1>Sign Up</h1>
	  <a href="/login">Already have an account? Sign in</a>
	</div>
	<form action="/registerUser" method="POST">
	  <div class="registerform">
			<label for="firstname"><b>First Name</b></label>
			<input type="text" name="firstname" placeholder="First name" required/>
			<label for="lastname"><b>Last Name</b></label>
			<input type="text" name="lastname" placeholder="Last name" required/>
			<label for="emailid"><b>Email ID</b></label>
			<input type="text" name="emailid" placeholder="abc@example.com" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$" required/>
			<label for="password"><b>Password</b></label>
			<input type="password" name="password" placeholder="Password" required/>
			<button type="submit" class="registerbtn">Sign Up</button>
	  </div>
	</form>
  </div>
  </div>	
</body>
</html>