package com.zsz.dto;

import java.util.Date;

public class AdminLogDTO {
	private long id;
	private long adminUserId;
	private Date createDateTime;
	private String message;
	private String adminUserName;
	private String adminUserPhoneNum;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getAdminUserId() {
		return adminUserId;
	}
	public void setAdminUserId(long adminUserId) {
		this.adminUserId = adminUserId;
	}
	public Date getCreateDateTime() {
		return createDateTime;
	}
	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getAdminUserName() {
		return adminUserName;
	}
	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}
	public String getAdminUserPhoneNum() {
		return adminUserPhoneNum;
	}
	public void setAdminUserPhoneNum(String adminUserPhoneNum) {
		this.adminUserPhoneNum = adminUserPhoneNum;
	}
}
