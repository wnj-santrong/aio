package com.santrong.play.entry;

import java.util.Date;

/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 上午11:05:59
 */
public class TagItem {
	private String id;
	private String tagName;
	private int priority;
	private Date cts;
	private Date uts;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getCts() {
		return cts;
	}
	public void setCts(Date cts) {
		this.cts = cts;
	}
	public Date getUts() {
		return uts;
	}
	public void setUts(Date uts) {
		this.uts = uts;
	}
	
}
