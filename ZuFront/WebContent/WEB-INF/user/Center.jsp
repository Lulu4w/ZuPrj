<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>个人中心</title>
	<%@include file="/WEB-INF/header.jsp" %>
</head>
<%@include file="/WEB-INF/loading.jsp" %>
	<body>
        <div class="headertwo clearfloat" id="header">
            <a href="javascript:history.go(-1)" class="fl box-s"><i class="iconfont icon-arrow-l fl"></i></a>
            <p class="fl">个人中心</p>
        </div>
		<div class="clearfloat pcenter" id="main">
			<div class="p-list p-listwo clearfloat box-s">
				<a href="collection.html" class="clearfloat">
					<i class="iconfont icon-favorite fl xing"></i>
					<span class="fl">我的收藏</span>
					<i class="iconfont icon-arrowright fr you"></i>
				</a>
			</div>

			<div class="p-list p-listhree clearfloat box-s">
				<a href="entrust.html" class="clearfloat">
					<i class="iconfont icon-weituoguanli fl weituoguanli"></i>
					<span class="fl">我的委托管理</span>
					<i class="iconfont icon-arrowright fr you"></i>
				</a>
			</div>
			
			<div class="p-list p-listwo clearfloat box-s">
				<a href="extension.html" class="clearfloat">
					<i class="iconfont icon-gonggao fl gonggao"></i>
					<span class="fl">我要推广</span>
					<i class="iconfont icon-arrowright fr you"></i>
				</a>
			</div>
			<div class="p-list p-listhree clearfloat box-s">
				<a href="about.html" class="clearfloat">
					<i class="iconfont icon-gerenzhongxin fl gerenzhongxin"></i>
					<span class="fl">关于我们</span>
					<i class="iconfont icon-arrowright fr you"></i>
				</a>
			</div>
			<div class="p-list p-listhree clearfloat box-s">
				<a href="modify.html" class="clearfloat">
					<i class="iconfont icon-lock fl lock"></i>
					<span class="fl">修改密码</span>
					<i class="iconfont icon-arrowright fr you"></i>
				</a>
			</div>
		</div>
		
		<footer class="page-footer fixed-footer" id="footer">
			<ul>
				<li>
					<a href="<%=ctxPath%>/Index?action=index">
						<i class="iconfont icon-shouyev1"></i>
						<p>首页</p>
					</a>
				</li>
				<li>
					<a href="lease.html">
						<i class="iconfont icon-chuzuwo"></i>
						<p>我要出租</p>
					</a>
				</li>
				<li>
					<a href="schedule.html">
						<i class="iconfont icon-richengbiao"></i>
						<p>看房日程</p>
					</a>
				</li>
				<li class="active">
					<a href="<%=ctxPath%>/User?action=center">
						<i class="iconfont icon-gerenzhongxin"></i>
						<p>个人中心</p>
					</a>
				</li>
			</ul>
		</footer>
	</body>
	<script type="text/javascript" src="<%=ctxPath %>/slick/slick.min.js" ></script>
	<script type="text/javascript" src="<%=ctxPath %>/js/jquery.leanModal.min.js"></script>
	<script type="text/javascript" src="<%=ctxPath %>/js/tchuang.js" ></script>
	<script type="text/javascript" src="<%=ctxPath %>/js/hmt.js" ></script>
</html>
