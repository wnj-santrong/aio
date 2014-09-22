package com.santrong.system;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

import com.santrong.log.Log;
import com.santrong.setting.SettingAction;
import com.santrong.system.status.StatusMgr;
import com.santrong.tcp.client.MainTcp31001;
import com.santrong.tcp.client.MainTcp39004;
import com.santrong.tcp.client.MainTcp39007;
import com.santrong.tcp.client.TcpClientService;
import com.santrong.util.XmlReader;

/**
 * @author weinianjie
 * @date 2014年8月18日
 * @time 下午3:41:50
 */
public class SystemUpdateService {
	
	public static int updateSource;// 更新来源，本地升级包，1在线升级包
	public static boolean uploading;// 上传或者下载升级文件中
	public static boolean updating;// 升级中
	public static int uploadPercent;// 下载或者升级中百分比
	public static int updatePercent;// 升级中百分比
	public static String uploadResult;// 上传或者下载升级文件结果
	public static String updateResult;// 升级结果
	
	public static String OnlineUpdateAddr = "http://www.santrong.com/update";	// 在线升级地址

	public static void resetStatus() {
		SystemUpdateService.updateSource = 0;
		SystemUpdateService.uploading = false;
		SystemUpdateService.uploadPercent = 0;
		SystemUpdateService.uploadResult = null;
		SystemUpdateService.updating = false;
		SystemUpdateService.updatePercent = 0;
		SystemUpdateService.updateResult = null;
	}
	
	public String update() {
		BufferedReader in = null;
		try{
			resetStatus();
			SystemUpdateService.uploading = true;
			SystemUpdateService.updateSource = 1;
			
			String xmlStr = "";
			String updateUrl = OnlineUpdateAddr;
			if(!updateUrl.endsWith("xml")) {
				if(!updateUrl.endsWith("/")) {
					updateUrl += "/";
				}
				updateUrl += "update.xml";
			}
			Log.info("get update.xml file from " + updateUrl);
			URL url = new URL(updateUrl);
			URLConnection conn = url.openConnection();
	        // 设置通用的请求属性
	        conn.setRequestProperty("accept", "*/*");
	        conn.setRequestProperty("connection", "Keep-Alive");
	        conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	        conn.connect();
	        
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
            	xmlStr += line;
            }
            
            Log.info("------Online Update:" + xmlStr);
            
            XmlReader xml = new XmlReader();
            xml.parse(xmlStr);
            
//            "<?xml version="1.0" encoding="UTF-8"?><Update><Version>主板本</Version><FileUrl>升级文件下载地址</FileUrl></Update>"
            String version = xml.find("/Version").getText();
            String FileUrl = xml.find("/FileUrl").getText();
            if(version == null) {
            	SystemUpdateService.uploadResult =  "fail";
            	return SystemUpdateService.uploadResult;
            }
            
            // 获取当前版本
            TcpClientService client = TcpClientService.getInstance();
            MainTcp39004 tcp = new MainTcp39004();
            client.request(tcp);
            if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
            	SystemUpdateService.uploadResult =  "fail";
            	return SystemUpdateService.uploadResult;
            }
            
            // 版本对比
            if(version.equals(tcp.getSystemVersion())) {
            	SystemUpdateService.uploadResult =  "notice_last_version";
            	return SystemUpdateService.uploadResult;
            }
            
            // 下载升级文件
            if(!getRemoteFile(FileUrl, "update.tar.gz")) {
            	SystemUpdateService.uploadResult =  "fail";
            	return SystemUpdateService.uploadResult;
            }
            
            SystemUpdateService.uploading = false;
			SystemUpdateService.uploadPercent = 100;
			SystemUpdateService.uploadResult = "success";
            
        	// 发送升级指令
        	if(!cmdUpdate()) {
        		return "fail";
        	}
        	
            return "success";
	        
		}catch(Exception e) {
			SystemUpdateService.uploadResult =  "fail";
			Log.printStackTrace(e);
		}finally {
			SystemUpdateService.uploading = false;
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
            	Log.printStackTrace(e2);
            }
		}
		
		return "fail";
	}
	
	/*
	 * 发送升级指令
	 */
	public boolean cmdUpdate() {
		
		// 系统正在上传升级、下载升级或者升级中
		if(SystemUpdateService.uploading || SystemUpdateService.updating) {
			SystemUpdateService.updateResult = "fail";
			Log.logOpt("system-update", "systemUpdate end fail", "system", "127.0.0.1");
			return false;
		}
		
		// 判断是否在上课
		if(SettingAction.isClassOpen()) {
			SystemUpdateService.updateResult = "fail";
			Log.logOpt("system-update", "systemUpdate end fail", "system", "127.0.0.1");
			return false;
		}			
		
		// 获取文件失败（上传和在线获取）
		if(!"success".equals(SystemUpdateService.uploadResult)) {
			SystemUpdateService.updateResult = "fail";
			Log.logOpt("system-update", "systemUpdate end fail", "system", "127.0.0.1");
			return false;
		}
		
		try{
			// 给maintain发送登录，同时校验maintain可用性
			TcpClientService client = TcpClientService.getInstance();
			MainTcp31001 tcp31001 = new MainTcp31001();
			tcp31001.setAddr("http://" + InetAddress.getLocalHost().getHostAddress() + "/http/basic.action");
			tcp31001.setPort(80);
			tcp31001.setHeartbeat(Global.HeartInterval);
			client.request(tcp31001);
			if(tcp31001.getRespHeader().getReturnCode() == 1 || tcp31001.getResultCode() ==1) {
				SystemUpdateService.updateResult = "fail";
				Log.logOpt("system-update", "systemUpdate end fail", "system", "127.0.0.1");
				return false;
			}
			
			SystemUpdateService.updating = true;
			SystemUpdateService.updatePercent = 0;
			
			
			MainTcp39007 tcp = new MainTcp39007();
			client.request(tcp);
			
			if(tcp.getRespHeader().getReturnCode() == 0 && tcp.getResultCode() == 0) {
				Log.logOpt("system-update", "systemUpdate end success", "system", "127.0.0.1");
				return true;
			}else {
				SystemUpdateService.updateResult = "fail";
				Log.logOpt("system-update", "systemUpdate end fail", "system", "127.0.0.1");
				SystemUpdateService.updating = false;
				return false;
			}
		}catch(Exception e) {
			SystemUpdateService.updating = false;
			Log.logOpt("system-update", "systemUpdate end fail", "system", "127.0.0.1");
			Log.printStackTrace(e);
		}
		
		SystemUpdateService.updateResult = "fail";
		Log.logOpt("system-update", "systemUpdate end fail", "system", "127.0.0.1");
		return false;
	}
	
	
	/**
	 * 获取远程文件 
	 * @param strUrl 文件url
	 * @param fileName 存储到本地后的文件名
	 * @return
	 * @throws IOException
	 */
	private boolean getRemoteFile(String strUrl, String fileName) {
		DataInputStream input = null;
		DataOutputStream output = null;
		HttpURLConnection conn = null;
		try{
			URL url = new URL(strUrl); 
			conn = (HttpURLConnection) url.openConnection(); 
//			暂时HTTP方式，支持FTP方式的获取，只需要如下改动：
//		    URLConnection conn = url.openConnection();
			int  fileSize = 0;// 升级文件大小不可能溢出，用int
			try{
				fileSize = conn.getContentLength();
			}catch(Exception ee) {
				Log.printStackTrace(ee);
			}
			input = new DataInputStream(conn.getInputStream()); 
			output = new DataOutputStream(new FileOutputStream(DirDefine.updateFileDir + "/" + fileName));
			byte[] buffer = new byte[1024 * 8];// 每次1K
			int count = 0; 
			while ((count = input.read(buffer)) > 0) {
				output.write(buffer, 0, count);
				if(fileSize != 0) {
					SystemUpdateService.uploadPercent = output.size() * 100 / fileSize;
				}
			}
			SystemUpdateService.uploadPercent = 100;
		}catch(Exception e) {
			Log.printStackTrace(e);
		}finally{
			try{
				if(conn != null) {
					conn.disconnect();
				}
			}catch(Exception e1) {
				Log.printStackTrace(e1);
			}
			
			try{
				if(output != null) {
					output.close();
				}
			}catch(Exception e2) {
				Log.printStackTrace(e2);
			}
			
			try{
				if(input != null) {
					input.close();
				}
			}catch(Exception e3) {
				Log.printStackTrace(e3);
			}
		}
		
		return true; 
	}
	
}
