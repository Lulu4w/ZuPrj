<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>修改成功</title>
    <%@include file="/WEB-INF/header.jsp" %>
</head>
<%@include file="/WEB-INF/loading.jsp" %>
	<body>
		<div class="headertwo clearfloat" id="header">
			<a href="javascript:history.go(-1)" class="fl box-s"><i class="iconfont icon-arrow-l fl"></i></a>
			<p class="fl">修改成功</p>
		</div>
		
		<div class="success clearfloat" id="main">			
			<div class="top clearfloat">
				<i class="iconfont icon-chenggong chenggong"></i>
				<p class="tit">恭喜您，成功修改密码！</p>
				<p class="fu-tit">您的密码已经修改成功，请牢记此密码！</p>
			</div>
		</div>
	</body>
	<script src="<%=ctxPath %>/js/fastclick.js"></script>
	<script src="<%=ctxPath %>/js/mui.min.js"></script>
	<script type="text/javascript" src="<%=ctxPath %>/js/hmt.js" ></script>
</html>
