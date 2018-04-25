<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="zf" uri="http://www.zsz.com/functions" %>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>

<title>编辑管理员</title>
</head>
<body>
<div class="pd-20">
  <form action="" method="post" class="form form-horizontal" id="form-role-add">
  	<input type="hidden" name="action" value="editSubmit"/>
  	<input type="hidden" name="id" value="${adminUser.id }"/>
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>手机号：</label>
      <div class="formControls col-5">
      	<c:out value="${adminUser.phoneNum}"/>
      </div>
      <div class="col-4"> </div>
    </div>
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>姓名：</label>
      <div class="formControls col-5">
        <input type="text" class="input-text" value="${adminUser.name }" placeholder="" id="name" name="name" datatype="*" nullmsg="姓名不能为空">
      </div>
      <div class="col-4"> </div>
    </div>    
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>初始密码：</label>
      <div class="formControls col-5">
        <input type="password" class="input-text" value="" placeholder="" id="password" name="password" >
      </div>
      <div class="col-4"> </div>
    </div>    
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>确认密码：</label>
      <div class="formControls col-5">
        <input type="password" class="input-text" value="" placeholder="" recheck="password"  id="password2" name="password2">
      </div>
      <div class="col-4"> </div>
    </div>    
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>邮箱：</label>
      <div class="formControls col-5">
        <input type="text" class="input-text"  value="${adminUser.email }" placeholder="" id="email" name="email" datatype="e" nullmsg="邮箱不能为空">
      </div>
      <div class="col-4"> </div>
    </div>    
    <div class="row cl">
      <label class="form-label col-3"><span class="c-red">*</span>城市：</label>
      <div class="formControls col-5">
      	<select id="cityId" name="cityId" datatype="*">
      		<c:if test="${empty adminUser.cityId}">
      			<option value="-1" selected="selected">总部</option>
      		</c:if>
      		<c:if test="${not empty adminUser.cityId}">
      			<option value="-1">总部</option>
      		</c:if>
      		<c:forEach items="${cities }" var="city">
      			<c:if test="${city.id eq adminUser.cityId }">
      			   <option value="${city.id }"  selected="selected">${city.name }</option>
      			</c:if>
      			<c:if test="${city.id ne adminUser.cityId }">
      			   <option value="${city.id }">${city.name }</option>
      			</c:if>
      		</c:forEach>
      	</select>
      </div>
      <div class="col-4"> </div>
    </div>                    
    <div class="row cl">
      <label class="form-label col-3">角色</label>
    </div>
    <div class="row cl">
      <c:forEach items="${roles }" var="role">
      	<div class="col-3">
      	
      	<c:if test="${zf:contains(userRoleIds,role.id) }">
      		<input type="checkbox" name="roleId" value="${role.id }" id="roleId${role.id}" checked="checked" />
      	</c:if>
      	<c:if test="${not zf:contains(userRoleIds,role.id) }">
      		<input type="checkbox" name="roleId" value="${role.id }" id="roleId${role.id}"/>
      	</c:if>
      	<label for="roleId${role.id}"><c:out value="${role.name }"/></label>
      	</div>
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
			url:"<%=ctxPath%>/AdminUser",type:"post",
			data:data,
			success:function(result){
				if(result.status=="ok")
				{
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
});
</script>
</body>
</html>