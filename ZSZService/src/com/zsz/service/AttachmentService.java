package com.zsz.service;

import com.zsz.dao.AttachmentDAO;
import com.zsz.dto.AttachmentDTO;

public class AttachmentService
{
	private AttachmentDAO dao = new AttachmentDAO();

	public AttachmentDTO[] getAll()
	{
		return dao.getAll();
	}

	public AttachmentDTO[] getAttachments(long houseId)
	{
		return dao.getAttachments(houseId);
	}
}
