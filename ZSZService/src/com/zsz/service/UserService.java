package com.zsz.service;

import com.zsz.dao.UserDAO;
import com.zsz.dto.UserDTO;

public class UserService {

	private UserDAO dao = new UserDAO();

	public long addnew(String phoneNum, String password) {
		if(dao.getByPhoneNum(phoneNum)!=null)
		{
			throw new IllegalArgumentException("手机号已经存在！"+phoneNum);
		}
		
		return dao.addnew(phoneNum, password);
	}

	public UserDTO getById(long id) {
		return dao.getById(id);
	}

	public UserDTO getByPhoneNum(String phoneNum) {
		return dao.getByPhoneNum(phoneNum);
	}

	public boolean checkLogin(String phoneNum, String password) {
		return dao.checkLogin(phoneNum, password);
	}

	public void updatePwd(long userId, String newPassword) {
		dao.updatePwd(userId, newPassword);
		;
	}

	public void setUserCityId(long userId, long cityId) {
		dao.setUserCityId(userId, cityId);
	}
}
