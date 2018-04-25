package com.zsz.dto;

import java.util.Date;

public class HousePicDTO
{
	private long id;
	private long houseId;
	private String url;
	private String thumbUrl;
	private int width;
	private int height;
	private Date createDateTime;
	private boolean isDeleted;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getHouseId()
	{
		return houseId;
	}

	public void setHouseId(long houseId)
	{
		this.houseId = houseId;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getThumbUrl()
	{
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl)
	{
		this.thumbUrl = thumbUrl;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public Date getCreateDateTime()
	{
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime)
	{
		this.createDateTime = createDateTime;
	}

	public boolean isDeleted()
	{
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted)
	{
		this.isDeleted = isDeleted;
	}

}
