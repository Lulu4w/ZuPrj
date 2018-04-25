package com.zsz.dto;

public class CommunityDTO
{
	private long id;
	private String name;
	private long regionId;
	private boolean isDeleted;
	private String location;// �ض�
	private String traffic;// ��ͨ״��
	private Integer builtYear;// ������ݣ��ɿգ�

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public long getRegionId()
	{
		return regionId;
	}

	public void setRegionId(long regionId)
	{
		this.regionId = regionId;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public boolean isDeleted()
	{
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public String getTraffic()
	{
		return traffic;
	}

	public void setTraffic(String traffic)
	{
		this.traffic = traffic;
	}

	public Integer getBuiltYear()
	{
		return builtYear;
	}

	public void setBuiltYear(Integer builtYear)
	{
		this.builtYear = builtYear;
	}
}
