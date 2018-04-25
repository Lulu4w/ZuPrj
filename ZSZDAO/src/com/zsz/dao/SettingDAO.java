package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dto.SettingDTO;
import com.zsz.dao.utils.JDBCUtils;

public class SettingDAO
{
	public static final String Key_RuPengSMS_UserName="RuPengSMS.UserName";
	public static final String Key_RuPengSMS_AppKey="RuPengSMS.AppKey";
	public static final String Key_RuPengSMS_TemplateId="RuPengSMS.TemplateId";
	public static final String Key_FileServer_RootUrl="FileServer.RootUrl";
	public static final String Key_HouseStaticPages_Dir = "HouseStaticPagesDir";
	public static final String Key_FrontRootUrl = "FrontRootUrl";
	
	public void setValue(String name, String value)
	{
		String oldValue = getValue(name, null);
		try
		{
			if (oldValue == null)
			{
				JDBCUtils.executeNonQuery("insert into T_Settings(Name,Value) values(?,?)", name, value);
			} else
			{
				JDBCUtils.executeNonQuery("update T_Settings set Value=? where Name=?", name, value);
			}
		} catch (SQLException ex)
		{
			throw new RuntimeException(ex);
		}
	}
	
	public String getValue(String name)
	{
		return getValue(name,null);
	}

	public String getValue(String name, String defaultValue)
	{
		try
		{
			String value = (String) JDBCUtils.querySingle("select Value from T_Settings where Name=?", name);
			if (value == null)
			{
				return defaultValue;
			} else
			{
				return value;
			}
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		}
	}

	private static SettingDTO toDTO(ResultSet rs) throws SQLException
	{
		SettingDTO dto = new SettingDTO();
		dto.setId(rs.getLong("Id"));
		dto.setName(rs.getString("Name"));
		dto.setValue(rs.getString("Value"));
		return dto;
	}

	public SettingDTO[] getAll()
	{
		List<SettingDTO> list = new ArrayList<SettingDTO>();
		ResultSet rs = null;
		try
		{
			rs = JDBCUtils.executeQuery("select * from T_Settings");
			while (rs.next())
			{
				list.add(toDTO(rs));
			}
			return list.toArray(new SettingDTO[list.size()]);
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		} finally
		{
			JDBCUtils.closeAll(rs);
		}

	}
}
