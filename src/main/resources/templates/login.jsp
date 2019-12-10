<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>Home</title>
    
</head>

<body>
	<script type="text/javascript">
	$(document).ready(function(){
		<c:if test="${!empty SESSION_INFO}">
		for(var i = 0; i<200 ; i ++){
			setTimeout(function(){getData();},1000+(i*10));
		}
		</c:if>
		
	});
	var cnt = 0;
	function getData(){
		$("#result").prepend(cnt+'<iframe style="float:left;height:30px;width:200px;" src="/member/getData.json" />');
		setTimeout(function(){getData();}, 100);
		cnt ++;
		if(cnt == 10000){
			document.location.href="/member/login";
		}
	}
	</script>
	
		<div class="hero-unit">
			<h1>Home</h1>
		
			<p>
				<spring:message code="message.welcome"/>
			</p>
			<p><a class="btn btn-primary btn-large" href="https://github.com/unlogicaldev/spring4_servlet3_infinispan"><spring:message code="message.home.learnMore"/></a></p>
			
		</div>
		
		<c:if test="${empty SESSION_INFO}">
			<div class="row-fluid">
				<div class="span8">
				
					<form name="fm" method="post">
						ID : <input type="text" name="id" /><br/>
						PASS : <input type="password" name="pass" /><br/>
						<input type="submit" value="Login" />					
					</form>
	 			</div>
			</div>
		</c:if>
		${SESSION_INFO}
		<div id="result" style="width:100%; height:400px; overflow-y:scroll;"></div>
</body>
</html>