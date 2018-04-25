<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>输入新密码</title>
     <%@include file="/WEB-INF/header.jsp" %>
     <script type="text/javascript">
     $(function(){

		$("#next").click(function(){
	    	 var password = $("#password").val();
	    	 var password2 = $("#password2").val();
	    	 if(password!=password2)
	   		 {
	   		 	alert("两次输入的密码不一致");
	   		 	return;
	   		 };
			$.ajax({
	    		 type:"post",url:"<%=ctxPath%>/User",
	    		 data:{action:"findPasswordSubmit3",password:password},
	    		 success:function(result){
	    			 if(result.status=="ok")
	   				 {
	   				 	location.href="<%=ctxPath%>/User?action=findPasswordComplete";//充值密码成功
	   				 }
	    			 else
	   				 {
	   				 	alert(result.msg);
	   				 }
	    		 },
	    		 error:function(){
	    			 alert("网络通讯错误");
	    		 }
	    	 });
			
		});
     });
     </script>
</head>
<%@include file="/WEB-INF/loading.jsp" %>

	<body>
		<div class="headertwo clearfloat" id="header">
			<a href="javascript:history.go(-1)" class="fl box-s"><i class="iconfont icon-arrow-l fl"></i></a>
			<p class="fl">输入新密码</p>
		</div>
		
		<div class="modify clearfloat" id="main">
			<ul>
				<li class="clearfloat">
					<input type="password" name="" id="password" value="" placeholder="请输入新密码" class="sname snametwo" />
				</li>
				<li class="clearfloat">
					<input type="password" name="" id="password2" value="" placeholder="请再次输入新密码" class="sname snametwo" />
				</li>	
			</ul>
			<a href="javascript:void(0)" id="next" class="pay-btn clearfloat">
				确认修改
			</a>
		</div>
	</body>
	<script type="text/javascript" src="js/jquery-1.8.3.min.js" ></script>
	<script type="text/javascript" src="js/jquery.SuperSlide.2.1.js" ></script>
	<script type="text/javascript" src="slick/slick.min.js" ></script>
	<script type="text/javascript" src="js/jquery.leanModal.min.js"></script>
	<script type="text/javascript" src="js/tchuang.js" ></script>
	<script type="text/javascript" src="js/hmt.js" ></script>
</html>
