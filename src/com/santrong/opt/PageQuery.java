package com.santrong.opt;

/**
 * @author weinianjie
 * @date 2014年7月18日
 * @time 下午3:04:18
 */
public class PageQuery {
	private int pageNum;
	private int count;
	private int pageSize = 14;
	
	private String orderBy = "cts";
	private String orderRule = "desc";	
	
	/*
	 * 获取分页的开始索引
	 */
	public int getBeginIndex() {
		return pageNum * pageSize;
	}
	
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderRule() {
		return orderRule;
	}

	public void setOrderRule(String orderRule) {
		this.orderRule = orderRule;
	}
}
