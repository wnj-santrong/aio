package com.santrong.http.client.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

import com.santrong.log.Log;
import com.santrong.util.SantrongUtils;

/**
 * @author weinianjie
 * @date 2014年7月18日
 * @time 下午5:29:22
 */
public class HttpClientHandler{
	
	private static final Logger logger = Logger.getLogger(HttpClientHandler.class);
	
    private static final int TIME_OUT = 10000;		// 连接超时时间
    
    /**
     * 发送post
     * */
	public String sendPost(String url, String xmlMsg) {
		StringBuilder msgRsp = new StringBuilder();
		PrintWriter out = null;
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setConnectTimeout(TIME_OUT);
			out = new PrintWriter(conn.getOutputStream());
			
			String uuid = SantrongUtils.getGUID();
			
			logger.info("sendXmlMsg [HTTP_BEGIN (" + uuid + ")] : " + xmlMsg);
			
			out.print(xmlMsg);
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				msgRsp.append(line);
			}
			
			logger.info("getXmlMsg  [HTTP_END   (" + uuid + ")] : " + msgRsp.toString());
			
		} catch (Exception e) {
			Log.printStackTrace(e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return msgRsp.toString();
	}
}
