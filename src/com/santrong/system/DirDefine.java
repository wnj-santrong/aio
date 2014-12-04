package com.santrong.system;

import com.santrong.system.network.SystemUtils;

/**
 * @author weinianjie
 * @date 2014年8月8日
 * @time 下午4:22:21
 */
public class DirDefine {
	public static final String DbBackupDir;								// 数据库备份路径
	public static final String ShellDir;										// 所有shell脚本目录
	public static final String SysConfigDir;								// 公共配置文件目录
	public static final String UpdateFileDir;							// 升级文件放置目录
	public static final String VedioDir;										// 录制的视频目录
	
	static {
		
		// 开发环境
		if(SystemUtils.getOsType() == SystemUtils.WINDOWS) {
			DbBackupDir = "E:/workspace/aio/linuxDir/opt/AIO/Service/webservice/dbbackup";
			ShellDir = "E:/workspace/aio/linuxDir/opt/AIO/Service/webservice/webapp/shell";
			SysConfigDir = "E:\\workspace\\aio\\linuxDir\\opt\\AIO\\Service";
			UpdateFileDir = "E:/workspace/aio/linuxDir/opt/AIO/Service/update";
			VedioDir = "E:/workspace/aio/linuxDir/RecData";
			
		// 部署环境
		}else{
			DbBackupDir = "/opt/AIO/Service/webservice/dbbackup";
			ShellDir = "/opt/AIO/Service/webservice/webapp/shell";
			SysConfigDir = "/opt/AIO/Service";
			UpdateFileDir = "/opt/AIO/Service/update";
			VedioDir = "/RecData";
		}
		
	}	
	
}
