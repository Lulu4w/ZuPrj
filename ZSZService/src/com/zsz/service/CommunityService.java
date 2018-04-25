package com.zsz.service;

import com.zsz.dao.CommunityDAO;
import com.zsz.dto.CommunityDTO;

public class CommunityService
{
	private CommunityDAO dao = new CommunityDAO();
	public CommunityDTO[] getByRegionId(long regionId)
	{
		return dao.getByRegionId(regionId);
	}
}
