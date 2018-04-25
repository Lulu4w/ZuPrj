<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>短信验证</title>
    <%@include file="/WEB-INF/header.jsp" %>
    <script type="text/javascript">
    $(function(){
    	$("#next").click(function(){
    		var smsCode = $("#smsCode").val();
    		$.ajax({
    			type:"post",url:"<%=ctxPath%>/User",
    			data:{action:"findPasswordSubmit2",smsCode:smsCode},
    			success:function(result){
    				if(result.status=="ok")
   					{
   						location.href="<%=ctxPath%>/User?action=findPassword3";
   					}
    				else
   					{
   						alert(result.msg);
   					}
    			}
    			,error:function(){
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
			<p class="fl">短信验证</p>
		</div>
		
		<div class="modify clearfloat" id="main">
			<ul>
				<li class="clearfloat">
					<input type="text" name="" id="smsCode" value="" placeholder="请输入手机短信中的验证码" class="sname" />
				</li>				
			</ul>
			<a href="javascript:void(0)" id="next" class="pay-btn clearfloat">
				验证
			</a>
		</div>		
	</body>
	<script type="text/javascript" src="<%=ctxPath %>/js/jquery.SuperSlide.2.1.js" ></script>
	<script type="text/javascript" src="<%=ctxPath %>/slick/slick.min.js" ></script>
	<script type="text/javascript" src="<%=ctxPath %>/js/jquery.leanModal.min.js"></script>
	<script type="text/javascript" src="<%=ctxPath %>/js/tchuang.js" ></script>
	<script type="text/javascript" src="<%=ctxPath %>/js/hmt.js" ></script>
</html>
