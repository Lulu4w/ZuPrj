package com.zsz.service;

import com.zsz.dao.RegionDAO;
import com.zsz.dto.RegionDTO;

public class RegionService
{
	private RegionDAO dao = new RegionDAO();

	public RegionDTO getById(long id)
	{
		return dao.getById(id);
	}

	public RegionDTO[] getAll(long cityId)
	{
		return dao.getAll(cityId);
	}
}
