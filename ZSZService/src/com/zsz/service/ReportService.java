package com.zsz.service;

import java.util.Map;

import com.zsz.dao.ReportDAO;

public class ReportService {
	/**
	 * 获取24小时之内各个城市的新增房源数量
	 * 
	 * @return
	 */
	public Map<String, Long> queryYesterdayCityInfo() {
		return new ReportDAO().queryYesterdayCityInfo();
	}
}
