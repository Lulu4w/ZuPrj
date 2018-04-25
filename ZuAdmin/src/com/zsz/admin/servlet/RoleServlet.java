package com.zsz.admin.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zsz.dao.PermissionDAO;
import com.zsz.dto.PermissionDTO;
import com.zsz.dto.RoleDTO;
import com.zsz.service.PermissionService;
import com.zsz.service.RoleService;
import com.zsz.tools.AjaxResult;
import com.zsz.tools.CommonUtils;

@WebServlet("/Role")
public class RoleServlet extends BaseServlet {

	@HasPermission("Role.Query")
	public void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		RoleService roleService = new RoleService();
		RoleDTO[] roles =  roleService.getAll();
		req.setAttribute("roles", roles);
		req.getRequestDispatcher("/WEB-INF/role/roleList.jsp").forward(req, resp);
	}
	
	@HasPermission("Role.Delete")
	public void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = Long.parseLong(req.getParameter("id"));
		RoleService service = new RoleService();
		service.markDeleted(id);
		this.writeJson(resp, new AjaxResult("ok"));
	}
	
	@HasPermission("Role.AddNew")
	public void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		PermissionDAO permDAO = new PermissionDAO();
		PermissionDTO[] perms = permDAO.getAll();
		req.setAttribute("perms", perms);
		req.getRequestDispatcher("/WEB-INF/role/roleAdd.jsp").forward(req, resp);
	}
	
	@HasPermission("Role.AddNew")
	public void addSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String rolename = req.getParameter("rolename");
		String[] permIds = req.getParameterValues("permId");
		
		RoleService service = new RoleService();
		long roleId = service.addnew(rolename);
		
		PermissionService permService = new PermissionService();
		permService.addPermIds(roleId, CommonUtils.toLongArray(permIds));
		writeJson(resp, new AjaxResult("ok"));
	}
	
	@HasPermission("Role.Edit")
	public void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id= Long.parseLong(req.getParameter("id"));
		RoleService service = new RoleService();
		RoleDTO role = service.getById(id);
		req.setAttribute("role", role);
		
		//获得所有的权限项
		PermissionService permService = new PermissionService();
		PermissionDTO[] perms = permService.getAll();
		req.setAttribute("perms", perms);
		
		//这个角色拥有的权限项的id
		PermissionDTO[] rolePerms = permService.getByRoleId(id);
		long[] rolePermIds = new long[rolePerms.length];
		for(int i=0;i<rolePerms.length;i++)
		{
			rolePermIds[i] = rolePerms[i].getId();
		}
		
		req.setAttribute("rolePermIds", rolePermIds);
		
		req.getRequestDispatcher("/WEB-INF/role/roleEdit.jsp").forward(req, resp);
	}
	
	@HasPermission("Role.Edit")
	public void editSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id= Long.parseLong(req.getParameter("id"));
		String rolename = req.getParameter("rolename");
		String[] permIds = req.getParameterValues("permId");
		
		RoleService service = new RoleService();
		service.update(id, rolename);

		PermissionService permService = new PermissionService();
		permService.updatePermIds(id, CommonUtils.toLongArray(permIds));
		
		writeJson(resp, new AjaxResult("ok"));
	}
}
