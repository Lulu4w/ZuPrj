<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="z" uri="http://www.zu.com/core" %>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>
<title>预约管理</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 用户中心 <span class="c-gray en">&gt;</span> 用户管理 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">

	<div class="cl pd-5 bg-1 bk-gray mt-20"> 
	<div class="mt-20">
	<table class="table table-border table-bordered table-hover table-bg table-sort">
		<thead>
			<tr class="text-c">
				<th width="25"><input type="checkbox" name="" value=""></th>
				<th width="80">ID</th>
				<th width="100">区域</th>
				<th width="100">小区名</th>
				<th width="100">姓名</th>
				<th width="100">手机</th>
				<th width="100">看房时间</th>
				<th width="100">创建时间</th>
				<th width="100">状态</th>
				<th width="100">跟进人</th>
				<th width="100">跟进时间</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${apps}" var="app">
			<tr class="text-c">
				<td><input type="checkbox" value="1" name=""></td>
				<th width="80">ID</th>
				<th width="100">${app.regionName }</th>
				<th width="100">${app.communityName }</th>
				<th width="100">${app.name }</th>
				<th width="100">${app.phoneNum }</th>
				<th width="100">${app.visitDate }</th>
				<th width="100">${app.createDateTime }</th>
				<th width="100">${app.status }</th>
				<th width="100">${app.followAdminUserName }</th>
				<th width="100">${app.followDateTime }</th>
				<td class="td-manage">
				<a title="抢单" href="javascript:;" onclick="house_follow(this,'${app.id}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	<z:pager urlFormat="${ctxPath }/House?action=appList&pageNum={pageNum}&status=${status }"
		 pageSize="10" totalCount="${totalCount}" currentPageNum="${pageNum}"/>
</div>
<script type="text/javascript">
$(function(){
	$('.table-sort tbody').on( 'click', 'tr', function () {
		if ( $(this).hasClass('selected') ) {
			$(this).removeClass('selected');
		}
		else {
			table.$('tr.selected').removeClass('selected');
			$(this).addClass('selected');
		}
	});
});



/*抢单*/
function house_follow(link,id){
	$.ajax({
		url:"<%=ctxPath%>/House",type:"post",
		data:{action:"follow",id:id},
		success:function(obj)
		{
			if(obj.status=="ok")
			{
				$(link).parents("tr").remove();
				layer.msg('抢单成功!',{icon:1,time:1000});
			}
			else
			{
				layer.msg(result.msg,{icon:6,time:1000});
			}
		},
		error:function(){alert("抢单处理失败");}
	})
}
</script> 
</body>
</html>