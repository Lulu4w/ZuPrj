package com.zsz.dto;

import java.util.Date;

public class HouseAppointmentDTO
{
	private long id;
	private Long userId;
	private String name;
	private String phoneNum;
	private Date visitDate;
	private long houseId;
	private Date createDateTime;
	private String status;
	private Long followAdminUserId;
	private String followAdminUserName;
	private Date followDateTime;
	private String regionName;
	private String communityName;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPhoneNum()
	{
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum)
	{
		this.phoneNum = phoneNum;
	}

	public Date getVisitDate()
	{
		return visitDate;
	}

	public void setVisitDate(Date visitDate)
	{
		this.visitDate = visitDate;
	}

	public long getHouseId()
	{
		return houseId;
	}

	public void setHouseId(long houseId)
	{
		this.houseId = houseId;
	}

	public Date getCreateDateTime()
	{
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime)
	{
		this.createDateTime = createDateTime;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public Long getFollowAdminUserId()
	{
		return followAdminUserId;
	}

	public void setFollowAdminUserId(Long followAdminUserId)
	{
		this.followAdminUserId = followAdminUserId;
	}

	public String getFollowAdminUserName()
	{
		return followAdminUserName;
	}

	public void setFollowAdminUserName(String followAdminUserName)
	{
		this.followAdminUserName = followAdminUserName;
	}

	public Date getFollowDateTime()
	{
		return followDateTime;
	}

	public void setFollowDateTime(Date followDateTime)
	{
		this.followDateTime = followDateTime;
	}

	public String getRegionName()
	{
		return regionName;
	}

	public void setRegionName(String regionName)
	{
		this.regionName = regionName;
	}

	public String getCommunityName()
	{
		return communityName;
	}

	public void setCommunityName(String communityName)
	{
		this.communityName = communityName;
	}

}
