package com.zsz.service;

import com.zsz.dao.AdminLogDAO;

public class AdminLogService {
	
	private AdminLogDAO dao = new AdminLogDAO();
	
	// 插入一条日志：adminUserId为操作用户id，message为消息
	public void addnew(long adminUserId, String message) {
		dao.addnew(adminUserId, message);
	}
}
