package com.santrong.info.entry;

import java.util.ArrayList;
import java.util.List;

import com.santrong.tcp.client.MainTcp39004.ModuleStatus;
import com.santrong.util.SantrongUtils;

/**
 * @author weinianjie
 * @date 2014年7月14日
 * @time 下午4:34:09
 */
public class SystemInfoView {
	
	private String model;
	private String serialNo;
	private int uniVodMax;
	private int uniCur;
	private int vodCur;
	private int freePcent;
	private long freeSize;
	private String systemVersion;
	private String webVersion;
	private List<ModuleStatus> moduleList = new ArrayList<ModuleStatus>();
	
	/*
	 * 根据百分比和空闲算出磁盘总容量
	 */
	public String getTotalSizeString() {
		if(this.freeSize != 0 && this.freePcent != 0){
			return SantrongUtils.formatDiskSize(this.freeSize * 1024 * 1024 * 100 / this.freePcent);// 先获取总字节然后格式化
		}
		return "unknown";
	}
	
	/*
	 * 获取剩余空间
	 */
	public String getFreeSizeString() {
		if(this.freeSize != 0) {
			return SantrongUtils.formatDiskSize(this.freeSize * 1024 * 1024);// M先转成字节
		}
		return "unknown";
	}
	
	
	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public String getWebVersion() {
		return webVersion;
	}


	public void setWebVersion(String webVersion) {
		this.webVersion = webVersion;
	}



	public String getModel() {
		return model;
	}


	public void setModel(String model) {
		this.model = model;
	}


	public String getSerialNo() {
		return serialNo;
	}


	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}


	public int getUniVodMax() {
		return uniVodMax;
	}
	public void setUniVodMax(int uniVodMax) {
		this.uniVodMax = uniVodMax;
	}
	public int getUniCur() {
		return uniCur;
	}
	public void setUniCur(int uniCur) {
		this.uniCur = uniCur;
	}
	public int getVodCur() {
		return vodCur;
	}
	public void setVodCur(int vodCur) {
		this.vodCur = vodCur;
	}
	public int getFreePcent() {
		return freePcent;
	}
	public void setFreePcent(int freePcent) {
		this.freePcent = freePcent;
	}
	public long getFreeSize() {
		return freeSize;
	}
	public void setFreeSize(long freeSize) {
		this.freeSize = freeSize;
	}
	public List<ModuleStatus> getModuleList() {
		return moduleList;
	}
	public void setModuleList(List<ModuleStatus> moduleList) {
		this.moduleList = moduleList;
	}
}
