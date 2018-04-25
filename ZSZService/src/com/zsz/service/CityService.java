package com.zsz.service;

import com.zsz.dao.CityDAO;
import com.zsz.dto.CityDTO;

public class CityService {
	private CityDAO dao = new CityDAO();

	// 根据id获取城市DTO
	public CityDTO getById(long id) {
		return dao.getById(id);
	}

	// 获取所有城市
	public CityDTO[] getAll() {
		return dao.getAll();
	}
}
