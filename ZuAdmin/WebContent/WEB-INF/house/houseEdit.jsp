<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="z" uri="http://www.zu.com/core" %>
<%@taglib prefix="zf" uri="http://www.zsz.com/functions" %>
<!DOCTYPE HTML>
<html>
<head>
<%@include file="/WEB-INF/header.jsp" %>
<script type="text/javascript">
$(function(){
	$("#regionId").change(function(){
		var regionId = $(this).val();	
		$.ajax({type:"post",url:"<%=ctxPath%>/House",
			data:{action:"loadCommunities",regionId:regionId},
			success:function(result){
				if(result.status!="ok")
				{
					layer.msg('加载小区错误'+result.msg,{icon:6,time:1000});
					return;
				}
				$("#communityId").empty();//清空以前生成的子元素
				for(var i=0;i<result.data.length;i++)
				{
					var item = result.data[i];
					var xiangdeng = (item.id==${house.communityId});//如果当前遍历的小区的id等于被编辑房源的小区id，则设定这个optoin为selected
					$("<option value='"+item.id+"'"+(xiangdeng?" selected":"")+">"+item.name+"</option>").appendTo($("#communityId"));
				}
			},
			error:function(){
				layer.msg("加载小区网络通讯错误",{icon:6,time:1000});
			}
		});
	});
	
	$("#regionId").change();//在html中如果直接设定某个option选中，则页面记载的时候change不会被触发。因此ajax就无法加载这个区下的小区。因此这里主动触发一下
});
</script>
<title>修改房源</title>
</head>
<body>
<div class="pd-20">
  <form action="" method="post" class="form form-horizontal" id="form-add">
  	<input type="hidden" id="" name="id" value="${house.id }"/>
  	<input type="hidden" name="action" value="editSubmit"/>
  	<input type="hidden" name="typeId" value="${house.typeId }"/>
    <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>区域：</label>
      <div class="formControls col-2">
       	  <z:select items="${regions }" selectedValue="${house.regionId }" name="regionId" textName="name" valueName="id" id="regionId"/>
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>小区：</label>
      <div class="formControls col-2">
        <select id="communityId" name="communityId"></select>
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>房型：</label>
      <div class="formControls col-2">
        <z:select items="${roomTypes }" selectedValue="${house.roomTypeId }" name="roomTypeId" textName="name" valueName="id" id="roomTypeId"/>
      </div>  
    </div>
    <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>地址：</label>
      <div class="formControls col-6">
        <input type="text" class="input-text" value="${house.address }" placeholder="这里输入几号楼，几单元，哪个房间" id="address" name="address" datatype="*" nullmsg="地址必填">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>月租：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.monthRent }" placeholder="" id="monthRent" name="monthRent" datatype="n" nullmsg="月租不能为空">
      </div>
    </div>    
    <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>状态：</label>
      <div class="formControls col-2">
        <z:select items="${statuses }" selectedValue="${house.statusId }" name="statusId" textName="name" valueName="id" id="statusId"/>
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>面积：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.area }" placeholder="" id="area" name="area" datatype="*" nullmsg="面积不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>装修：</label>
      <div class="formControls col-2">
        <z:select items="${decorateStatus }" selectedValue="${house.decorateStatusId }" name="decorateStatusId" textName="name" valueName="id" id="decorateStatusId"/>
      </div>  
    </div>    
    <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>楼层：</label>
      <div class="formControls col-1">
        <input type="text" class="input-text" value="${house.floorIndex }" placeholder="第几层" id="floorIndex" name="floorIndex" datatype="n" nullmsg="层数不能为空">
      </div>
      <div class="formControls col-1">
        <input type="text" class="input-text" value="${house.totalFloorCount }" placeholder="总层数" id="totalFloorCount" name="totalFloorCount" datatype="n" nullmsg="总层数不能为空">
      </div>      
      <label class="form-label col-2"><span class="c-red">*</span>朝向：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.direction }" placeholder="" id="direction" name="direction" datatype="*" nullmsg="朝向不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>可看房时间：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.lookableDateTime }" placeholder="" onfocus="WdatePicker({minDate: '${now}'})" id="lookableDateTime" name="lookableDateTime"  nullmsg="可看房时间不能为空">
      </div>  
    </div>   
    <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>入住时间：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.checkInDateTime }" onfocus="WdatePicker({minDate: '${now}'})" placeholder="" id="checkInDateTime" name="checkInDateTime" nullmsg="手机号不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>业主姓名：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.ownerName }" placeholder="" id="ownerName" name="ownerName" datatype="*" nullmsg="业主姓名不能为空">
      </div>
      <label class="form-label col-2"><span class="c-red">*</span>业主电话：</label>
      <div class="formControls col-2">
        <input type="text" class="input-text" value="${house.ownerPhoneNum }" placeholder="" id="ownerPhoneNum" name="ownerPhoneNum" datatype="m" nullmsg="业主电话不能为空">
      </div>  
    </div>  
    <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>房源描述：</label>
      <div class="formControls col-10">
        <textarea class="input-text" style="height: 100px" id="description" name="description"><c:out value="${house.description}"/></textarea>
      </div>
    </div>  
    <div class="row cl">
      <label class="form-label col-2"><span class="c-red">*</span>配套设施：</label>
      <div class="formControls col-10">
        <c:forEach items="${attachments }" var="att">
        	<z:checkbox checked="${zf:contains(houseAttachmentIds,att.id) }" label="${att.name }" name="attachmentId" value="${att.id }" id="attachmentId-${att.id }"/>
        </c:forEach>
      </div>
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
	var validForm = $("#form-add").Validform({tiptype:2});//初始化校验器
	$("#btnSave").click(function(){
		if(validForm.check(false)==false)//表单校验不通过
		{
			return;
		}
		
		var data = $("#form-add").serializeArray();//<input type="hidden" name="action" value="addSubmit"/>
		$.ajax({
			url:"<%=ctxPath%>/House",type:"post",
			data:data,
			success:function(result){
				if(result.status=="ok")
				{
					parent.location.reload();//刷新父窗口
				}
				else
				{
					alert("保存失败"+result.msg);
				}
			},
			error:function(){alert("保存网络请求失败");}
		});
	});
});
</script>
</body>
</html>