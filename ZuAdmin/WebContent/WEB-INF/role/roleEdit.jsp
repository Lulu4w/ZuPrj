<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="zf" uri="http://www.zsz.com/functions" %>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>

<title>编辑角色</title>
</head>
<body>
<div class="pd-20">
  <form action="" method="post" class="form form-horizontal" id="form-role-add">
  	<input type="hidden" name="action" value="editSubmit"/>
  	<input type="hidden" name="id" value="${role.id }"/>
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>角色名：</label>
      <div class="formControls col-5">
        <input type="text" class="input-text" value="${role.name }" placeholder="" id="member-name" name="rolename" datatype="*2-16" nullmsg="角色名不能为空">
      </div>
      <div class="col-4"> </div>
    </div>
    <div class="row cl">
      <label class="form-label col-3">权限</label>
    </div>
    <div class="row cl">
      <c:forEach items="${perms }" var="perm">
      	<div class="col-2"><input type="checkbox" name="permId" value="${perm.id }" id="permId${perm.id}"
      		<c:if test="${zf:contains(rolePermIds,perm.id) }">checked</c:if>
      	 /><label for="permId${perm.id}"><c:out value="${perm.description }"/></label></div>
      </c:forEach>
    </div>

    <div class="row cl">
      <div class="col-9 col-offset-3">
        <input class="btn btn-primary radius" type="button" id="btnSave" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
      </div>
    </div>
  </form>
</div>

<script type="text/javascript">
$(function(){
	
	//必须放到页面初始化的时候，不能放到按钮点击里面
	var validForm = $("#form-role-add").Validform({tiptype:2});//初始化校验器
	$("#btnSave").click(function(){
		if(validForm.check(false)==false)//表单校验不通过
		{
			return;
		}
		
		var data = $("#form-role-add").serializeArray();//<input type="hidden" name="action" value="addSubmit"/>
		$.ajax({
			url:"<%=ctxPath%>/Role",type:"post",
			data:data,
			success:function(result){
				if(result.status=="ok")
				{
					//alert("保存成功");
					
					//把当前的弹窗关掉
					//var index = parent.layer.getFrameIndex(window.name);
					//parent.layer.close(index);
					parent.location.reload();//刷新父窗口
				}
				else
				{
					alert("保存失败");
				}
			},
			error:function(){alert("保存网络请求失败");}
		});
	});
	
	/*
	$("#form-role-add").Validform({
		tiptype:2,
		callback:function(form){
			form[0].submit();
			var index = parent.layer.getFrameIndex(window.name);
			parent.$('.btn-refresh').click();
			parent.layer.close(index);
		}
	});*/
});
</script>
</body>
</html>