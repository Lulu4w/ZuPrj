package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dto.AttachmentDTO;
import com.zsz.dao.utils.JDBCUtils;

public class AttachmentDAO
{
	static AttachmentDTO toDTO(ResultSet rs) throws SQLException
	{
		AttachmentDTO dto = new AttachmentDTO();
		dto.setDeleted(rs.getBoolean("IsDeleted"));
		dto.setIconName(rs.getString("IconName"));
		dto.setId(rs.getLong("Id"));
		dto.setName(rs.getString("Name"));
		return dto;
	}

	public AttachmentDTO[] getAll()
	{
		List<AttachmentDTO> list = new ArrayList<AttachmentDTO>();
		ResultSet rs = null;
		try
		{
			rs = JDBCUtils.executeQuery("select * from T_Attachments where IsDeleted=0");
			while (rs.next())
			{
				list.add(toDTO(rs));
			}
			return list.toArray(new AttachmentDTO[list.size()]);
		} catch (SQLException ex)
		{
			throw new RuntimeException(ex);
		} finally
		{
			JDBCUtils.closeAll(rs);
		}
	}

	public AttachmentDTO[] getAttachments(long houseId)
	{
		ResultSet rs = null;
		try
		{
			rs = JDBCUtils.executeQuery(
					"select * from T_Attachments where Id in(select AttachmentId from t_houseattachments where HouseId=?)",
					houseId);
			List<AttachmentDTO> list = new ArrayList<AttachmentDTO>();
			while(rs.next())
			{
				list.add(toDTO(rs));
			}
			return list.toArray(new AttachmentDTO[list.size()]);
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		} finally
		{
			JDBCUtils.closeAll(rs);
		}
	}
}
