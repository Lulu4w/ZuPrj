<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%String ctxPath = request.getContextPath(); %>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">    
    <meta name="keywords" content="">
    <meta name="description" content="">
    <script src="<%=ctxPath %>/js/rem.js"></script> 
    <script src="<%=ctxPath %>/js/jquery.min.js" type="text/javascript"></script>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath %>/css/base.css"/>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath %>/css/page.css"/>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath %>/css/all.css"/>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath %>/css/mui.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath %>/css/loaders.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath %>/css/loading.css"/>
    <link rel="stylesheet" type="text/css" href="<%=ctxPath %>/slick/slick.css"/>
	<script type="text/javascript">
		$(window).load(function(){
			$(".loading").addClass("loader-chanage")
			$(".loading").fadeOut(300)
		})
	</script>