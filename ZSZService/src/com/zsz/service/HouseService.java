package com.zsz.service;

import com.zsz.dao.HouseDAO;
import com.zsz.dto.HouseDTO;
import com.zsz.dto.HousePicDTO;
import com.zsz.dto.HouseSearchOptions;
import com.zsz.dto.HouseSearchResult;

public class HouseService {

	private HouseDAO dao = new HouseDAO();

	public HouseDTO getById(long id) {
		return dao.getById(id);
	}

	public long getTotalCount(long cityId, long typeId) {
		return dao.getTotalCount(cityId, typeId);
	}

	public HouseDTO[] getAll() {
		return dao.getAll();
	}

	public HouseDTO[] getPagedData(long cityId, long typeId, int pageSize, long currentIndex) {
		return dao.getPagedData(cityId, typeId, pageSize, currentIndex);
	}

	public long addnew(HouseDTO house) {
		return dao.addnew(house);
	}

	public void update(HouseDTO house) {
		dao.update(house);
	}

	public void markDeleted(long id) {
		dao.markDeleted(id);
	}

	public HousePicDTO[] getPics(long houseId) {
		return dao.getPics(houseId);
	}

	public long addnewHousePic(HousePicDTO housePic) {
		return dao.addnewHousePic(housePic);
	}

	public void deleteHousePic(long housePicId) {
		dao.deleteHousePic(housePicId);
	}

	// 要把当前项目中的HouseSearchOptions删除，并且挪到DTO中
	public HouseDTO[] search(HouseSearchOptions options) {
		return dao.search(options);

	}

	public HouseSearchResult search2(HouseSearchOptions options) {
		return dao.search2(options);

	}

}
