<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
<title>Member Register</title>
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
  <link rel="stylesheet" href="/resources/css/bootstrap.min.css">
	<script src="/resources/js/jquery-2.1.3.min.js"></script>
	<script src="/resources/js/bootstrap.min.js"></script>
	<script src="/resources/js/bootstrap-table.js"></script>
</head>

<body>
	<script type="text/javascript">
	$(document).ready(function(){
		
	});
	
	</script>

<div class="hero-unit">
	<h1>Member Register</h1>
</div>
<form class="form-horizontal" action="/rest/member/register" method="post">
  <div class="form-group">
    <label for="nickName" class="col-sm-2 control-label">Member UID</label>
    <div class="col-sm-10">
      <input type="text" class="form-control input-large" name="nickName" id="nickName" placeholder="NickName" required>
    </div>
  </div>
  <div class="form-group">
    <label for="email" class="col-sm-2 control-label">Email</label>
    <div class="col-sm-10">
      <input type="email" class="form-control input-large" name="email" id="email" placeholder="Email" required>
    </div>
  </div>
  <div class="form-group">
    <label for="passwd" class="col-sm-2 control-label">Password</label>
    <div class="col-sm-10">
      <input type="password" class="form-control input-large" name="passwd" id="passwd" placeholder="Password" data-minlength="6" required>
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-1 col-sm-08">
      <div class="checkbox">
        <label>
          <input type="checkbox"> Remember me
        </label>
      </div>
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-1 col-sm-10">
      <button type="Submit" class="btn btn-primary btn-large">Sign up</button>
      <a class="btn btn-primary btn-large" style="margin:10;" href="/member/members">Previous </a>
    </div>
  </div>
</form>
</body>
</html>