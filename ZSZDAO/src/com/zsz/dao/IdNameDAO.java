package com.zsz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zsz.dao.utils.JDBCUtils;
import com.zsz.dto.IdNameDTO;

public class IdNameDAO {

	public long addIdName(String typeName, String name) {
		try {
			return JDBCUtils.executeInsert("insert into T_IdNames(TypeName,Name,IsDeleted) values(?,?,0)", typeName,
					name);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private static IdNameDTO toDTO(ResultSet rs) throws SQLException {
		IdNameDTO dto = new IdNameDTO();
		dto.setId(rs.getLong("id"));
		dto.setName(rs.getString("Name"));
		dto.setTypeName(rs.getString("TypeName"));
		return dto;
	}

	public IdNameDTO getById(long id) {
		ResultSet rs = null;
		try {
			rs = JDBCUtils.executeQuery("select * from T_IdNames where IsDeleted=0 and Id=?", id);
			if (rs.next()) {
				// DRY：Don't repeat yourself!
				/*
				 * IdNameDTO dto = new IdNameDTO(); dto.setId(rs.getLong("id"));
				 * dto.setName(rs.getString("Name"));
				 * dto.setTypeName(rs.getString("TypeName")); return dto;
				 */
				return toDTO(rs);
			} else {
				return null;
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			JDBCUtils.closeAll(rs);
		}
	}

	// 获取类别下的IdName（比如所有的民族）
	public IdNameDTO[] getAll(String typeName) {

		List<IdNameDTO> list = new ArrayList<IdNameDTO>();
		ResultSet rs = null;
		try {
			rs = JDBCUtils.executeQuery("select * from T_IdNames where IsDeleted=0 and TypeName=?",typeName);
			while (rs.next()) {
				/*
				 * IdNameDTO dto = new IdNameDTO(); dto.setId(rs.getLong("id"));
				 * dto.setName(rs.getString("Name"));
				 * dto.setTypeName(rs.getString("TypeName")); list.add(dto);
				 */
				IdNameDTO dto = toDTO(rs);
				list.add(dto);
			}
			return list.toArray(new IdNameDTO[list.size()]);
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			JDBCUtils.closeAll(rs);
		}
	}
}
