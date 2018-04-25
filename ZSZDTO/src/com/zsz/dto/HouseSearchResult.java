package com.zsz.dto;

public class HouseSearchResult {

	private HouseDTO[] result;
	private long totalCount;
	public HouseDTO[] getResult() {
		return result;
	}
	public void setResult(HouseDTO[] result) {
		this.result = result;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
}
