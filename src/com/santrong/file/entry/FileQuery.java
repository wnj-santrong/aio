package com.santrong.file.entry;

import com.santrong.opt.PageQuery;

/**
 * @author weinianjie
 * @date 2014年7月18日
 * @time 下午3:17:03
 */
public class FileQuery extends PageQuery {
	private String keyword;
	private int level = -1;
	private boolean showRecording = false;
	
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isShowRecording() {
		return showRecording;
	}
	public void setShowRecording(boolean showRecording) {
		this.showRecording = showRecording;
	}
}
