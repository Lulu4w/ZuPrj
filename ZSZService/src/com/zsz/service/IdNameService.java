package com.zsz.service;

import com.zsz.dao.IdNameDAO;
import com.zsz.dto.IdNameDTO;

public class IdNameService {
	
	private IdNameDAO dao = new IdNameDAO();

	public long addIdName(String typeName, String name) {
		return dao.addIdName(typeName, name);
	}

	public IdNameDTO getById(long id) {
		return dao.getById(id);
	}

	// 获取类别下的IdName（比如所有的民族）
	public IdNameDTO[] getAll(String typeName) {
		return dao.getAll(typeName);
	}
}
