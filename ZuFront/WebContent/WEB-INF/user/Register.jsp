<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>用户注册</title>
    <%@include file="/WEB-INF/header.jsp" %>
    <script type="text/javascript">
    $(function(){
    	
    	//刷新图形验证码
    	 $("#shuaxin").click();
    	 $("#shuaxin,#verifyCode").click(function(){
    		 $("#verifyCode").attr("src","<%=ctxPath%>/User?action=verifyCode&ts+"+Math.random());
    	 });
    	 
    	 //发送短信验证码
    	 $("#sendSmsCode").click(function(){
    		 var userCode = $("#userCode").val();
    		 var phoneNum = $("#phoneNum").val();
    		 $.ajax({type:"post",url:"<%=ctxPath%>/User",
    			data:{action:"regSendSmsCode",userCode:userCode,phoneNum:phoneNum},
    			success:function(result)
    			{
    				if(result.status=="ok")
   					{
   						alert("短信验证码已经发出，请查收");
   					}
    				else
   					{
   						alert(result.msg);
   						$("#shuaxin").click();
   					}
    			},
    			error:function()
    			{
    				alert("ajax请求网络失败");
    				$("#shuaxin").click();
    			}
    		 });
    		 
    	 });
    	 
    	 //处理注册请求
    	 $("#btnReg").click(function(){
    		 var phoneNum = $("#phoneNum").val();
    		 var smsCode = $("#smsCode").val();
    		 var password = $("#password").val();
    		 $.ajax({type:"post",url:"<%=ctxPath%>/User",
 				data:{action:"regSubmit",phoneNum:phoneNum,smsCode:smsCode,password:password},
 				success: function(result)
 				{
 					if(result.status=="ok")
					{
						alert("注册成功");
					}
 					else
					{
						alert(result.msg);
					}
 				},
 				error:function()
 				{
 					alert("注册网络请求失败");
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
			<p class="fl">用户注册</p>
		</div>
		
		<div class="register clearfloat" id="main">
			<ul>
				<li class="clearfloat">
					<p class="tit fl">手机号</p>
					<input type="text" id="phoneNum" value="" class="shuru fl" placeholder="请输入手机号码" />
				</li>
				<li class="clearfloat">
					<p class="tit fl">验证码</p>
					<input type="text" id="smsCode" value="" class="shuru shurutwo fl" placeholder="请输入短信验证码" />
					<a href="#loginmodalt" id="modaltrigger">
						<input type="button" id="" value="获取短信验证码" class="btn fr" />
					</a>
				</li>
				<li class="clearfloat">
					<p class="tit fl">密码</p>
					<input type="password" id="password" value="" class="shuru fl" placeholder="请设置密码" />
				</li>
				<li class="clearfloat">
					<p class="tit fl">确认密码</p>
					<input type="password" id="" value="" class="shuru fl" placeholder="请再次输入密码" />
				</li>
			</ul>
			<a href="javascript:void(0)" id="btnReg" class="pay-btn clearfloat">
				注册
			</a>
			<div class="bottom clearfloat">
				<p class="fl">
					已有账号？
					<a href="register.html">立即登录</a>
				</p>
			</div>
		</div>
		
		<!--弹窗内容 star-->
	    <div id="loginmodalt" class="box-s loginmodaltwo" style="display:none;">
	    	<div class="top clearfloat box-s">
	    		<p class="tit">请输入图片验证码</p>
	    		<div class="xia clearfloat">
	    			<input type="text" id="userCode" value="" class="yzm fl" placeholder="填写图片验证码" />
	    			<span class="fl"><img id="verifyCode" src="<%=ctxPath%>/User?action=verifyCode"/></span>
	    			<i id="shuaxin" class="iconfont icon-shuaxin fr"></i>
	    		</div>
	    	</div>
			<form id="loginform" name="loginform" method="post" action="">		
				<div class="center fl"><input type="button" name="loginbtn" id="loginbtn" class="hidemodal" value="取消" tabindex="3"></div>
				<div class="center fl"><input type="button" name="loginbtn" id="sendSmsCode" class="hidemodal" value="确定" tabindex="3"></div>
			</form>			
		</div>
	    <!--弹窗内容 end-->
	</body>
	<script type="text/javascript" src="<%=ctxPath %>/js/jquery.SuperSlide.2.1.js" ></script>
	<script type="text/javascript" src="<%=ctxPath %>/js/hmt.js" ></script>
	<script type="text/javascript" src="<%=ctxPath %>/js/jquery.leanModal.min.js"></script>
	<script type="text/javascript" src="<%=ctxPath %>/js/tchuang.js" ></script>
</html>
