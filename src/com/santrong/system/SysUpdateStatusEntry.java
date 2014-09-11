package com.santrong.system;

/**
 * @author weinianjie
 * @date 2014年9月10日
 * @time 下午4:17:11
 */
public class SysUpdateStatusEntry {
	
	public int updateSource;// 更新来源，本地升级包，1在线升级包
	public boolean uploading;// 上传或者下载升级文件中
	public boolean updating;// 升级中
	public int uploadPercent;// 下载或者升级中百分比
	public int updatePercent;// 升级中百分比
	public String uploadResult;// 上传或者下载升级文件结果
	public String updateResult;// 升级结果
	
	public int getUpdateSource() {
		return updateSource;
	}
	public void setUpdateSource(int updateSource) {
		this.updateSource = updateSource;
	}
	public boolean isUploading() {
		return uploading;
	}
	public void setUploading(boolean uploading) {
		this.uploading = uploading;
	}
	public boolean isUpdating() {
		return updating;
	}
	public void setUpdating(boolean updating) {
		this.updating = updating;
	}
	public int getUploadPercent() {
		return uploadPercent;
	}
	public void setUploadPercent(int uploadPercent) {
		this.uploadPercent = uploadPercent;
	}
	public int getUpdatePercent() {
		return updatePercent;
	}
	public void setUpdatePercent(int updatePercent) {
		this.updatePercent = updatePercent;
	}
	public String getUploadResult() {
		return uploadResult;
	}
	public void setUploadResult(String uploadResult) {
		this.uploadResult = uploadResult;
	}
	public String getUpdateResult() {
		return updateResult;
	}
	public void setUpdateResult(String updateResult) {
		this.updateResult = updateResult;
	}
}
