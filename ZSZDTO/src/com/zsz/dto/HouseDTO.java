package com.zsz.dto;

import java.util.Date;

public class HouseDTO
{
	private long id;
	private long cityId;// ����id
	private String cityName;
	private long regionId;// ����id
	private String regionName;// ��������
	private long communityId;// С��id
	private String communityName;// С������
	private String communityLocation;// С��λ��
	private String communityTraffic;// С����ͨ
	private Integer communityBuiltYear;// С���������

	private long roomTypeId;// ���ݻ���id������һ����
	private String roomTypeName;// ���ݻ�������
	private String address;// ��ַ
	private int monthRent;// �����
	private long statusId;// ״̬�������У��Ѿ������
	private String statusName;// ״̬����
	private double area;// ���
	private long decorateStatusId;// װ��״̬����װ����װ
	private String decorateStatusName;// װ��״̬����
	private int totalFloorCount;// ��¥��
	private int floorIndex;// ��������¥��
	private long typeId;// �������ͣ� ���� д��¥ ���� ����
	private String typeName;// ������������
	private String direction;// ����
	private Date lookableDateTime;// ���Կ�����ʱ��
	private Date checkInDateTime;// ������ס��ʱ��

	private String ownerName;// ��������
	private String ownerPhoneNum;// �����绰
	private String description;// ��Դ����
	private Date createDateTime;// ��¼ʱ��
	private boolean isDeleted;// �Ƿ���ɾ��
	private long[] attachmentIds;// ������ʩid
	private String firstThumbUrl;// ��һ������ͼurl

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getCityId()
	{
		return cityId;
	}

	public void setCityId(long cityId)
	{
		this.cityId = cityId;
	}

	public String getCityName()
	{
		return cityName;
	}

	public void setCityName(String cityName)
	{
		this.cityName = cityName;
	}

	public long getRegionId()
	{
		return regionId;
	}

	public void setRegionId(long regionId)
	{
		this.regionId = regionId;
	}

	public String getRegionName()
	{
		return regionName;
	}

	public void setRegionName(String regionName)
	{
		this.regionName = regionName;
	}

	public long getCommunityId()
	{
		return communityId;
	}

	public void setCommunityId(long communityId)
	{
		this.communityId = communityId;
	}

	public String getCommunityName()
	{
		return communityName;
	}

	public void setCommunityName(String communityName)
	{
		this.communityName = communityName;
	}

	public String getCommunityLocation()
	{
		return communityLocation;
	}

	public void setCommunityLocation(String communityLocation)
	{
		this.communityLocation = communityLocation;
	}

	public String getCommunityTraffic()
	{
		return communityTraffic;
	}

	public void setCommunityTraffic(String communityTraffic)
	{
		this.communityTraffic = communityTraffic;
	}

	public Integer getCommunityBuiltYear()
	{
		return communityBuiltYear;
	}

	public void setCommunityBuiltYear(Integer communityBuiltYear)
	{
		this.communityBuiltYear = communityBuiltYear;
	}

	public long getRoomTypeId()
	{
		return roomTypeId;
	}

	public void setRoomTypeId(long roomTypeId)
	{
		this.roomTypeId = roomTypeId;
	}

	public String getRoomTypeName()
	{
		return roomTypeName;
	}

	public void setRoomTypeName(String roomTypeName)
	{
		this.roomTypeName = roomTypeName;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public int getMonthRent()
	{
		return monthRent;
	}

	public void setMonthRent(int monthRent)
	{
		this.monthRent = monthRent;
	}

	public long getStatusId()
	{
		return statusId;
	}

	public void setStatusId(long statusId)
	{
		this.statusId = statusId;
	}

	public String getStatusName()
	{
		return statusName;
	}

	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}

	public double getArea()
	{
		return area;
	}

	public void setArea(double area)
	{
		this.area = area;
	}

	public long getDecorateStatusId()
	{
		return decorateStatusId;
	}

	public void setDecorateStatusId(long decorateStatusId)
	{
		this.decorateStatusId = decorateStatusId;
	}

	public String getDecorateStatusName()
	{
		return decorateStatusName;
	}

	public void setDecorateStatusName(String decorateStatusName)
	{
		this.decorateStatusName = decorateStatusName;
	}

	public int getTotalFloorCount()
	{
		return totalFloorCount;
	}

	public void setTotalFloorCount(int totalFloorCount)
	{
		this.totalFloorCount = totalFloorCount;
	}

	public int getFloorIndex()
	{
		return floorIndex;
	}

	public void setFloorIndex(int floorIndex)
	{
		this.floorIndex = floorIndex;
	}

	public long getTypeId()
	{
		return typeId;
	}

	public void setTypeId(long typeId)
	{
		this.typeId = typeId;
	}

	public String getTypeName()
	{
		return typeName;
	}

	public void setTypeName(String typeName)
	{
		this.typeName = typeName;
	}

	public String getDirection()
	{
		return direction;
	}

	public void setDirection(String direction)
	{
		this.direction = direction;
	}

	public Date getLookableDateTime()
	{
		return lookableDateTime;
	}

	public void setLookableDateTime(Date lookableDateTime)
	{
		this.lookableDateTime = lookableDateTime;
	}

	public Date getCheckInDateTime()
	{
		return checkInDateTime;
	}

	public void setCheckInDateTime(Date checkInDateTime)
	{
		this.checkInDateTime = checkInDateTime;
	}

	public String getOwnerName()
	{
		return ownerName;
	}

	public void setOwnerName(String ownerName)
	{
		this.ownerName = ownerName;
	}

	public String getOwnerPhoneNum()
	{
		return ownerPhoneNum;
	}

	public void setOwnerPhoneNum(String ownerPhoneNum)
	{
		this.ownerPhoneNum = ownerPhoneNum;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
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

	public long[] getAttachmentIds()
	{
		return attachmentIds;
	}

	public void setAttachmentIds(long[] attachmentIds)
	{
		this.attachmentIds = attachmentIds;
	}

	public String getFirstThumbUrl()
	{
		return firstThumbUrl;
	}

	public void setFirstThumbUrl(String firstThumbUrl)
	{
		this.firstThumbUrl = firstThumbUrl;
	}

}
