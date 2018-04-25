package com.zsz.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.JDBC42UpdatableResultSet;
import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.HouseAppointmentDTO;

public class HouseAppointmentDAO {
	private HouseAppointmentDTO toDTO(ResultSet rs) throws SQLException {
		HouseAppointmentDTO dto = new HouseAppointmentDTO();
		dto.setCommunityName(rs.getString("communityName"));
		dto.setCreateDateTime(rs.getDate("CreateDateTime"));
		dto.setFollowAdminUserId((Long) rs.getObject("FollowAdminUserId"));
		dto.setFollowAdminUserName(rs.getString("followUserName"));
		dto.setFollowDateTime(rs.getDate("FollowDateTime"));
		dto.setHouseId(rs.getLong("HouseId"));
		dto.setId(rs.getLong("Id"));
		dto.setName(rs.getString("Name"));
		dto.setPhoneNum(rs.getString("PhoneNum"));
		dto.setRegionName(rs.getString("regionName"));
		dto.setStatus(rs.getString("Status"));
		dto.setUserId((Long) rs.getObject("UserId"));
		dto.setVisitDate(rs.getDate("VisitDate"));
		return dto;
	}

	public HouseAppointmentDTO getById(long id) {
		StringBuilder sb = new StringBuilder();
		sb.append(
				"select app.*,u.Name followUserName,reg.Name regionName,com.Name communityName from t_houseappointments app\n");
		sb.append("left join T_AdminUsers u on app.FollowAdminUserId=u.Id\n");
		sb.append("left join T_Houses h on app.HouseId=h.Id\n");
		sb.append("left join T_Regions reg on h.RegionId=reg.Id\n");
		sb.append("left join T_Communities com on h.CommunityId=com.Id\n");
		sb.append("where app.Id=?");

		ResultSet rs = null;
		try {
			rs = JDBCUtils.executeQuery(sb.toString(), id);
			if (rs.next()) {
				return toDTO(rs);
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JDBCUtils.closeAll(rs);
		}
	}
	
	/**
	 * 返回所有预约订单的数量
	 * @return
	 */
	public long getTotalCount() {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) from t_houseappointments app");

		Number number;
		try {
			number = (Number) JDBCUtils.querySingle(sb.toString());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return number.longValue();
	}

	public long getTotalCount(long cityId, String status) {
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) from t_houseappointments app\n");
		sb.append("left join T_houses h on app.HouseId=h.Id\n");
		sb.append("left join T_Regions reg on h.RegionId=reg.Id\n");
		sb.append("left join T_Cities city on reg.CityId=city.Id\n");
		sb.append("where CityId=? and Status=?");

		Number number;
		try {
			number = (Number) JDBCUtils.querySingle(sb.toString(), cityId, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return number.longValue();
	}

	// currentIndex从1开始
	public HouseAppointmentDTO[] getPagedData(long cityId, String status, int pageSize, long currentIndex) {

		StringBuilder sb = new StringBuilder();
		sb.append(
				"select app.*,u.Name followUserName,reg.Name regionName,com.Name communityName from t_houseappointments app\n");
		sb.append("left join T_AdminUsers u on app.FollowAdminUserId=u.Id\n");
		sb.append("left join T_Houses h on app.HouseId=h.Id\n");
		sb.append("left join T_Regions reg on h.RegionId=reg.Id\n");
		sb.append("left join T_Communities com on h.CommunityId=com.Id\n");
		sb.append("where reg.CityId=? and app.Status=?\n");
		sb.append("limit ?,?");
		// sb.append("limit (?-1)*?,?");//错误！

		List<HouseAppointmentDTO> list = new ArrayList<>();
		ResultSet rs = null;
		try {
			rs = JDBCUtils.executeQuery(sb.toString(), cityId, status, (currentIndex - 1) * pageSize, pageSize);
			while (rs.next()) {
				list.add(toDTO(rs));
			}
			return list.toArray(new HouseAppointmentDTO[list.size()]);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} // 0,10 10,10
	}

	// 新增一个预约：userId用户id（可以为null）；name姓名、phoneNum手机号、houseId房间id、visiteDate预约看房时间
	public long addnew(Long userId, String name, String phoneNum, long houseId, Date visitDate) {
		Number number;
		try {
			number = JDBCUtils.executeInsert(
					"insert into T_HouseAppointments(UserId,Name,PhoneNum,VisitDate,HouseId,CreateDateTime,Status) values(?,?,?,?,?,now(),'新建')",
					userId, name, phoneNum, visitDate, houseId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return number.longValue();
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

		/*
		//select for update
		//其他人就不能对这几条数据做处理，只有我处理完成之后其他人才能处理
		try {
			Number number = (Number) JDBCUtils.querySingle(
					"select count(*) from t_houseappointments where Id=? and Status='新建'", houseAppointmentId);
			if (number.intValue() <= 0)// 订单被抢走了！
			{
				return false;
			}
			
			//把订单更新成：在跟进，并且把跟进人设置为自己
			JDBCUtils.executeNonQuery("Update t_houseappointments set Status='在跟进',FollowAdminUserId=?",
					houseAppointmentId);
			return true;
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}	
		*/
		//数据库锁：Lock
		
		//select ... for update和update语句要在同一个事务中，一般的事务都要在同一个连接中
		
		Connection conn=null;
		try
		{
			conn = JDBCUtils.getConnection();
			conn.setAutoCommit(false);
			Number number = (Number) JDBCUtils.querySingle(
					conn,"select count(*) from t_houseappointments where Id=? and Status='新建' for  update", houseAppointmentId);
			if (number.intValue() <= 0)// 订单被抢走了！
			{
				conn.rollback();//一定不能忘，是在解锁for update
				return false;
			}
			JDBCUtils.executeNonQuery(conn,"Update t_houseappointments set Status='在跟进',FollowAdminUserId=? where Id=?",
					adminUserId,houseAppointmentId);			
			
			conn.commit();
			return true;
		} catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				
			}
			throw new RuntimeException(e);
		}
		finally
		{
			JDBCUtils.closeQuietly(conn);
		}
	}
}
