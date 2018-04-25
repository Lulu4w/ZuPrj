package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.zsz.dao.utils.JDBCUtils;

public class ReportDAO {
	/**
	 * 获取24小时之内各个城市的新增房源数量
	 * 
	 * @return
	 */
	public Map<String, Long> queryYesterdayCityInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("select city.Name cityName,count(*) c from T_houses h\n");
		sb.append("left join T_Regions reg on h.RegionId=reg.Id\n");
		sb.append("left join T_Cities city on reg.CityId=city.Id\n");
		sb.append("where TIMESTAMPDIFF(HOUR, h.CreateDateTime, now())<=24\n");
		sb.append("group by city.Name\n");

		ResultSet rs = null;
		try {
			rs = JDBCUtils.executeQuery(sb.toString());
			HashMap<String, Long> map = new HashMap<>();
			while(rs.next())
			{
				String cityName = rs.getString("cityName");
				Long count = rs.getLong("c");
				map.put(cityName, count);
			}
			return map;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JDBCUtils.closeQuietly(rs);
		}
	}
}
