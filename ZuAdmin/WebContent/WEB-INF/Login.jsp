<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String ctxPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8"/>
<title>后台登录</title>
<meta name="author" content="" />
<link rel="stylesheet" type="text/css" href="css/styleLogin.css" />


<style type="text/css">
body{
	height:100%;background:#16a085;overflow:hidden;
}
canvas{
	z-index:-1;position:absolute;
}
img{
	z-index:1;position:absolute;
}
</style>


<script type="text/javascript" src="lib/jquery/1.9.1/jquery.js"></script>
<script type="text/javascript" src="lib/Particleground.js"></script> 

<script type="text/javascript">
	$(function(){
		$("#imgVerifyCode").click();
		$("#imgVerifyCode,#kanbuq").click(function(){
			$("#imgVerifyCode").attr("src","<%=ctxPath%>/Index?action=verifyCode&ts="+Math.random());
		});
		
		$("#btnLogin").click(function(){
			var data = $("#formLogin").serializeArray();
			$.ajax({
				type:"post",
				url:"<%=ctxPath%>/Index",
				data:data,
				success:function(result){
					if(result.status == "ok"){
						location.href="<%=ctxPath%>/Index?action=index";
					} else{
						
						alert("登录出错" + result.msg);
						$("#imgVerifyCode").click();
					}
				},
				error:function(){
					alert("登录请求失败");
				}
			});
		});
	});
	
	
</script>

<script>
$(document).ready(function() {
  //粒子背景特效
  $('body').particleground({
    dotColor: '#5cbdaa',
    lineColor: '#5cbdaa'
    //
    
  });
 
});
</script>


</head>
<body >
<form action="index.html" id="formLogin" method="post">
<input type="hidden" name="action" value="loginSubmit"/>

<dl class="admin_login">
 <dt>
  <strong>站点后台管理系统</strong>
  <em>Management System</em>
 </dt>
 <dd class="user_icon">
  <input type="text" name="phoneNum" placeholder="手机号" class="login_txtbx"/>
 </dd>
 <dd class="pwd_icon">
  <input type="password" name="password" placeholder="密码" class="login_txtbx"/>
 </dd>
 <dd class="val_icon">
  <div class="checkcode">
    <input type="text" name="verifyCode" id="J_codetext" placeholder="验证码" maxlength="4" class="login_txtbx">
    <img src="<%=ctxPath%>/Index?action=verifyCode" id="imgVerifyCode">

  </div>
  <input type="button" value="更换二维码" class="ver_btn" id="kanbuq" onClick="validate();">
 </dd>
 <dd>
  <input type="button" value="立即登陆" id="btnLogin" class="submit_btn" />
 </dd>
 <dd>
 
  <p>© 2018-2020 Lu_1u 版权所有</p>
  <p>黔B2-20180314-1</p>
 </dd>
</dl>
</form>
</body>
</html>
