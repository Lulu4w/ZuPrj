package com.zsz.service;

import java.util.Date;

import com.zsz.dao.HouseAppointmentDAO;
import com.zsz.dto.HouseAppointmentDTO;

public class HouseAppointmentService {
	private HouseAppointmentDAO dao = new HouseAppointmentDAO();
	
	public HouseAppointmentDTO getById(long id) {
		return dao.getById(id);
	}
	
	/**
	 * 返回所有预约订单的数量
	 * @return
	 */
	public long getTotalCount() {
		return dao.getTotalCount();
	}

	public long getTotalCount(long cityId, String status) {
		return dao.getTotalCount(cityId, status);
	}

	// currentIndex从1开始
	public HouseAppointmentDTO[] getPagedData(long cityId, String status, int pageSize, long currentIndex) {
		return dao.getPagedData(cityId, status, pageSize, currentIndex);
	}

	// 新增一个预约：userId用户id（可以为null）；name姓名、phoneNum手机号、houseId房间id、visiteDate预约看房时间
	public long addnew(Long userId, String name, String phoneNum, long houseId, Date visitDate) {
		return dao.addnew(userId, name, phoneNum, houseId, visitDate);
	}

	/**
	 * 抢单(锁实现抢单)数据库锁会降低数据库的性能，只适用于较小并发量的抢单。
	 * 1：两阶段提交
	 * 2：排队。
	 * 
	 * @param adminUserId
	 * @param houseAppointmentId
	 * @return true为抢单失败，false为抢单成功
	 */
	public boolean follow(long adminUserId, long houseAppointmentId) {
		return dao.follow(adminUserId, houseAppointmentId);
	}
}
