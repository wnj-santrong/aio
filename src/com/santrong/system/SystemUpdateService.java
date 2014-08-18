package com.santrong.system;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.santrong.log.Log;
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

	public String update() {
		BufferedReader in = null;
		try{
			String xmlStr = "";
			URL url = new URL(Global.OnlineUpdateAddr);
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
            String FileUrl = xml.find("/FileName").getText();
            if(version == null) {
            	return "fail";
            }
            
            // 获取当前版本
            TcpClientService client = TcpClientService.getInstance();
            MainTcp39004 tcp = new MainTcp39004();
            client.request(tcp);
            if(tcp.getRespHeader().getReturnCode() == 1 || tcp.getResultCode() == 1) {
            	return "fail";
            }
            
            // 版本对比
            if(version.equals(tcp.getSystemVersion())) {
            	return "notice_last_version";
            }
            
            // 下载升级文件
            if(!getRemoteFile(FileUrl, "update.tar.gz")) {
            	return "fail";
            }
            
        	// 发送升级指令
        	if(!cmdUpdate()) {
        		return "fail";
        	}
            
        	
            return "notice_update_success";
	        
		}catch(Exception e) {
			Log.printStackTrace(e);
		}finally {
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
		try{
			TcpClientService client = TcpClientService.getInstance();
			MainTcp39007 tcp = new MainTcp39007();
			client.request(tcp);
			
			if(tcp.getRespHeader().getReturnCode() == 0 && tcp.getResultCode() == 0) {
				return true;
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
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
		try{
			URL url = new URL(strUrl); 
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
//			暂时HTTP方式，支持FTP方式的获取，只需要如下改动：
//		    URLConnection conn = url.openConnection();
			input = new DataInputStream(conn.getInputStream()); 
			output = new DataOutputStream(new FileOutputStream(DirDefine.updateFileDir + "/" + fileName));
			byte[] buffer = new byte[1024 * 8];
			int count = 0; 
			while ((count = input.read(buffer)) > 0) { 
			  output.write(buffer, 0, count); 
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
		}finally{
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
