package com.zsz.service;

import com.zsz.dao.RoleDAO;
import com.zsz.dto.RoleDTO;

public class RoleService
{
	private RoleDAO dao = new RoleDAO();
	
	public long addnew(String roleName)
	{
		return dao.addnew(roleName);
	}
	
	public void update(long roleId,String roleName)
	{
		dao.update(roleId, roleName);
	}
	
	public void markDeleted(long roleId)
	{
		dao.markDeleted(roleId);
	}
	public RoleDTO getById(long id)
	{
		return dao.getById(id);
	}

	public RoleDTO[] getAll()
	{
		return dao.getAll();
	}

	/**
	 * 给adminUserId用户增加权限roleIds
	 * 
	 * @param adminUserId
	 * @param roleIds
	 */
	public void addRoleIds(long adminUserId, long[] roleIds)
	{
		dao.addRoleIds(adminUserId, roleIds);
	}
	
	/**
	 * 更新adminUserId的权限为roleIds
	 * @param adminUserId
	 * @param roleIds
	 */
	public void updateRoleIds(long adminUserId, long[] roleIds)
	{
		dao.updateRoleIds(adminUserId, roleIds);
	}

	public RoleDTO[] getByAdminUserId(long adminUserId)
	{
		return dao.getByAdminUserId(adminUserId);
	}
}
