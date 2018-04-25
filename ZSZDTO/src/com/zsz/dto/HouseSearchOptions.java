package com.zsz.dto;

public class HouseSearchOptions
{
	private long cityId;//城市id
	private Long typeId;//房源类型，可空
	private Long regionId;//区域，可空
	private Integer startMonthRent;//起始月租，可空
	private Integer endMonthRent;//结束月租，可空
	private OrderByType orderByType = OrderByType.MonthRent;//排序方式
	private String keywords;//搜索关键字，可空
	private int pageSize;//每页数据条数
	private long currentIndex;//当前页码

	public long getCityId()
	{
		return cityId;
	}

	public void setCityId(long cityId)
	{
		this.cityId = cityId;
	}

	public Long getTypeId()
	{
		return typeId;
	}

	public void setTypeId(Long typeId)
	{
		this.typeId = typeId;
	}

	public Long getRegionId()
	{
		return regionId;
	}

	public void setRegionId(Long regionId)
	{
		this.regionId = regionId;
	}

	public Integer getStartMonthRent()
	{
		return startMonthRent;
	}

	public void setStartMonthRent(Integer startMonthRent)
	{
		this.startMonthRent = startMonthRent;
	}

	public Integer getEndMonthRent()
	{
		return endMonthRent;
	}

	public void setEndMonthRent(Integer endMonthRent)
	{
		this.endMonthRent = endMonthRent;
	}

	public OrderByType getOrderByType()
	{
		return orderByType;
	}

	public void setOrderByType(OrderByType orderByType)
	{
		this.orderByType = orderByType;
	}

	public String getKeywords()
	{
		return keywords;
	}

	public void setKeywords(String keywords)
	{
		this.keywords = keywords;
	}

	public int getPageSize()
	{
		return pageSize;
	}

	public void setPageSize(int pageSize)
	{
		this.pageSize = pageSize;
	}

	public long getCurrentIndex()
	{
		return currentIndex;
	}

	public void setCurrentIndex(long currentIndex)
	{
		this.currentIndex = currentIndex;
	}

	public enum OrderByType {
		MonthRent, Area
	};
}
