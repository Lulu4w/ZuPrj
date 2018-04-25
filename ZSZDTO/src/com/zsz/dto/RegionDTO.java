package com.zsz.dto;

public class RegionDTO
{
	private long id;
	private String name;
	private boolean isDeleted;
	private long cityId;

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

	public boolean isDeleted()
	{
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

	public long getCityId()
	{
		return cityId;
	}

	public void setCityId(long cityId)
	{
		this.cityId = cityId;
	}

}
