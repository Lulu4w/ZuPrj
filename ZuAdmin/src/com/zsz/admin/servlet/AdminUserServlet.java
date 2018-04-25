package com.zsz.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.zsz.dto.AdminUserDTO;
import com.zsz.dto.CityDTO;
import com.zsz.dto.RoleDTO;
import com.zsz.service.AdminUserService;
import com.zsz.service.CityService;
import com.zsz.service.RoleService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.CommonUtils;

@WebServlet("/AdminUser")
public class AdminUserServlet extends BaseServlet {

	@HasPermission("AdminUser.Query")
	public void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		AdminUserService service = new AdminUserService();
		AdminUserDTO[] adminUsers =  service.getAll();
		req.setAttribute("adminUsers", adminUsers);
		req.getRequestDispatcher("/WEB-INF/adminUser/adminUserList.jsp").forward(req, resp);
		
	}
	
	@HasPermission("AdminUser.Delete")
	public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		AdminUserService service = new AdminUserService();
		service.markDeleted(id);
		writeJson(resp, new AjaxResult("ok"));
	}
	
	@HasPermission("AdminUser.AddNew")
	public void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		CityService cityService = new CityService();
		CityDTO[] cities = cityService.getAll();
		req.setAttribute("cities", cities);
		
		RoleService roleService = new RoleService();
		RoleDTO[] roles = roleService.getAll();
		req.setAttribute("roles", roles);
		
		req.getRequestDispatcher("/WEB-INF/adminUser/adminUserAdd.jsp").forward(req, resp);
	}
	
	@HasPermission("AdminUser.AddNew")
	public void addSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String phoneNum = req.getParameter("phoneNum");
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String strCityId = req.getParameter("cityId");
		String[] roleIds = req.getParameterValues("roleId");
		Long cityId=null;
		if(!strCityId.equals("-1"))//-1代表总部
		{
			cityId=Long.parseLong(strCityId);
		}
		
		AdminUserService adminUserService = new AdminUserService();
		AdminUserDTO user = adminUserService.getByPhoneNum(phoneNum);//第一道防线：更好的展示给用户
		if(user!=null)
		{
			writeJson(resp, new AjaxResult("error","手机号已经存在",""));
			return;
		}
		
		//addAdminUser内部检查手机号重复则是第二道防线，防止调用addAdminUser忘了检查手机号的重复性
		long adminUserId = adminUserService.addAdminUser(name, phoneNum, password, email, cityId);
		RoleService roleService = new RoleService();
		roleService.addRoleIds(adminUserId, CommonUtils.toLongArray(roleIds));
		
		writeJson(resp, new AjaxResult("ok"));
	}
	
	@HasPermission("AdminUser.Edit")
	public void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		AdminUserService adminUserService = new AdminUserService();
		AdminUserDTO adminUser =  adminUserService.getById(id);
		req.setAttribute("adminUser", adminUser);
		
		CityService cityService = new CityService();
		CityDTO[] cities = cityService.getAll();
		req.setAttribute("cities", cities);
		
		RoleService roleService = new RoleService();
		RoleDTO[] roles = roleService.getAll();
		req.setAttribute("roles", roles);
		
		RoleDTO[] userRoles = roleService.getByAdminUserId(id);//用户拥有的角色
		long[] userRoleIds = new long[userRoles.length];//用户拥有的角色的id
		for(int i=0;i<userRoles.length;i++)
		{
			userRoleIds[i] = userRoles[i].getId();
		}
		req.setAttribute("userRoleIds", userRoleIds);
		
		req.getRequestDispatcher("/WEB-INF/adminUser/adminUserEdit.jsp").forward(req, resp);
	}
	
	@HasPermission("AdminUser.Edit")
	public void editSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		
		String name = req.getParameter("name");
		String password = req.getParameter("password");
		String email = req.getParameter("email");
		String strCityId = req.getParameter("cityId");
		String[] roleIds = req.getParameterValues("roleId");
		Long cityId=null;
		if(!strCityId.equals("-1"))//-1为总部
		{
			cityId=Long.parseLong(strCityId);
		}
		
		AdminUserService adminUserService = new AdminUserService();
		adminUserService.updateAdminUser(id, name, password, email, cityId);
		
		RoleService roleService = new RoleService();
		roleService.updateRoleIds(id, CommonUtils.toLongArray(roleIds));
		
		writeJson(resp, new AjaxResult("ok"));		
	}
	
}
