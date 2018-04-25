<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>
<title>管理员管理</title>
</head>
<body>
<nav class="breadcrumb"><i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 用户中心 <span class="c-gray en">&gt;</span> 用户管理 <a class="btn btn-success radius r mr-20" style="line-height:1.6em;margin-top:3px" href="javascript:location.replace(location.href);" title="刷新" ><i class="Hui-iconfont">&#xe68f;</i></a></nav>
<div class="pd-20">

	<div class="cl pd-5 bg-1 bk-gray mt-20"> 
	<span class="l"><a href="javascript:;" onclick="layer_show('添加管理员','<%=ctxPath %>/AdminUser?action=add','500','510')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i> 添加管理员</a></span> <span class="r">共有数据：<strong>88</strong> 条</span> </div>
	<div class="mt-20">
	<table class="table table-border table-bordered table-hover table-bg table-sort">
		<thead>
			<tr class="text-c">
				<th width="25"><input type="checkbox" name="" value=""></th>
				<th width="80">ID</th>
				<th width="100">城市</th>
				<th width="100">姓名</th>
				<th width="100">手机号</th>
				<th width="100">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${adminUsers}" var="adminUser">
			<tr class="text-c">
				<td><input type="checkbox" value="1" name=""></td>
				<td><c:out value="${adminUser.id }"/> </td>
				<td><c:out value="${adminUser.cityName }"/></td>
				<td><c:out value="${adminUser.name }"/></td>
				<td><c:out value="${adminUser.phoneNum }"/></td>
				<td class="td-manage">
				<a title="编辑" href="javascript:;" onclick="layer_show('编辑','<%=ctxPath %>/AdminUser?action=edit&id=${adminUser.id }','800','510')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a>
				<a title="删除" href="javascript:;" onclick="adminUser_del(this,'${adminUser.id}')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>
				</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
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



/*用户-删除*/
function adminUser_del(link,id){
	layer.confirm('确认要删除吗？',function(index){
		$.ajax({
			url:"<%=ctxPath%>/AdminUser",type:"post",
			data:{action:"delete",id:id},
			success:function(obj)
			{
				if(obj.status=="ok")
				{
					$(link).parents("tr").remove();
					layer.msg('已删除!',{icon:1,time:1000});
				}
				else
				{
					alert("删除失败");
				}
			},
			error:function(){alert("删除处理失败");}
		})
		
		
	});
}
</script> 
</body>
</html>