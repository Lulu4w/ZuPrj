package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dto.CityDTO;
import com.zsz.dto.RegionDTO;
import com.zsz.dao.utils.JDBCUtils;

public class RegionDAO
{
	static RegionDTO toDTO(ResultSet rs) throws SQLException
	{
		RegionDTO dto = new RegionDTO();
		dto.setDeleted(rs.getBoolean("IsDeleted"));
		dto.setId(rs.getLong("Id"));
		dto.setName(rs.getString("Name"));
		dto.setCityId(rs.getLong("CityId"));
		return dto;
	}

	public RegionDTO getById(long id)
	{
		ResultSet rs = null;
		try
		{
			rs = JDBCUtils.executeQuery("select * from T_Regions where Id=? and IsDeleted=0", id);
			if (rs.next())
			{
				return toDTO(rs);
			} else
			{
				return null;
			}
		} catch (SQLException ex)
		{
			throw new RuntimeException(ex);
		} finally
		{
			JDBCUtils.closeAll(rs);
		}
	}

	public RegionDTO[] getAll(long cityId)
	{
		List<RegionDTO> list = new ArrayList<RegionDTO>();
		ResultSet rs = null;
		try
		{
			rs = JDBCUtils.executeQuery("select * from T_Regions where  IsDeleted=0 and CityId=?",cityId);
			while (rs.next())
			{
				list.add(toDTO(rs));
			}
			return list.toArray(new RegionDTO[list.size()]);
		} catch (SQLException ex)
		{
			throw new RuntimeException(ex);
		} finally
		{
			JDBCUtils.closeAll(rs);
		}
	}
}
