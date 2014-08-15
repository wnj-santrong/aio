package com.santrong.system.network;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.santrong.log.Log;
import com.santrong.system.Global;

/**
 * @author weinianjie
 * @date 2014年7月18日
 * @time 上午9:47:38
 */
public class UnixNetworkHandle extends AbstractNetworkHandle {
	private static final String configFile = "/etc/network/interfaces";
	private List<String> pro = new ArrayList<String>();
	
	private static UnixNetworkHandle instance;
	private UnixNetworkHandle() {
		
	}
	
	public static UnixNetworkHandle getInstance() {
		if(instance == null) {
			instance = new UnixNetworkHandle();
		}
		return instance;
	}

	@Override
	public synchronized boolean setNetworkInfo(NetworkInfo vo) {
		try{
			
			NetworkInfo otherInfo = this.getNetworkInfo(vo.getIndex() == 0? 1 : 0);// 获取另一块网卡信息
			
			String deviceName = getDeviceName(vo.getIndex());
			if(deviceName == null) {
				return false;
			}
			
			// 全覆盖式写入
			pro = new ArrayList<String>();
			pro.add("# interfaces(5) file used by ifup(8) and ifdown(8)");
			pro.add("auto lo");
			pro.add("iface lo inet loopback");
			// 先后顺序不影响
			if(otherInfo != null && !otherInfo.getIp().equals("")) {
				pro.add("");	
				pro.add("auto " + otherInfo.getDeviceName());	
				pro.add("iface " + otherInfo.getDeviceName() + " inet static");	
				pro.add("address " + otherInfo.getIp());	
				pro.add("netmask " + otherInfo.getMask());	
				pro.add("gateway " + otherInfo.getGateway());
			}
			pro.add("");
			pro.add("auto " + deviceName);	
			pro.add("iface " + deviceName + " inet static");	
			pro.add("address " + vo.getIp());	
			pro.add("netmask " + vo.getMask());	
			pro.add("gateway " + vo.getGateway());
			writeFile();
		}catch(Exception e) {
			return false;
		}
		
		return true;
	}

	
//	范本
//	auto eth0
//	iface eth0 inet static
//	address 192.168.10.220
//	network 192.168.10.1
//	netmask 255.255.255.0
	@Override
	public synchronized NetworkInfo getNetworkInfo(int index) {
		NetworkInfo info =  new NetworkInfo();
		this.readFile();
		
		String deviceName = getDeviceName(index);
		if(deviceName == null) {
			return info;
		}		
		
		for(int i=0;i<pro.size();i++) {
			if(pro.get(i).indexOf(deviceName) != -1) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				for(int j = i+2; j<pro.size(); j++) {// 识别到网卡后的下两行开始读
					String line = pro.get(j);
					if(line == null || line.equals("")){// 读到空行就停止
						break;
					}else {
						if(line.indexOf(" ") != -1) {
							String[] arr = line.split("\\s+");
							if(map.get(arr[0]) == null) {// 确保是第一次读到的值，防止网卡配置粘不用空行隔开粘在一起的情况
								map.put(arr[0], arr[1]);
							}
						}
					}
				}
				
				info.setDeviceName(deviceName);
				info.setIndex(index);
				String tmp = map.get("address");
				info.setIp(tmp == null? "" : tmp);
				tmp = map.get("netmask");
				info.setMask(tmp == null? "" : tmp);
				tmp = map.get("gateway");
				info.setGateway(tmp == null? "" : tmp);
				break;
			}
		}
		
		return info;
	}
	
	private String getDeviceName(int index) {
		String deviceName = null;
		switch(index) {
		case 0 :
			deviceName = Global.LanDeviceName;
			break;
		case 1 :
			deviceName = Global.WanDeviceName;
			break;
		}
		return deviceName;
	}
	
	/*
	 * 读取文件
	 */
	private void readFile() {
		pro = new ArrayList<String>();
		FileInputStream fin = null;
		InputStreamReader in = null;
		BufferedReader buff = null;
		try{
			fin = new FileInputStream(configFile);
			in = new InputStreamReader(fin, "UTF-8");// UNIX默认编码
			buff = new BufferedReader(in);
			String line;
			while((line = buff.readLine()) != null) {
				line = line.trim();
				pro.add(line);
			}
		}catch(Exception e) {
			Log.printStackTrace(e);
		}finally{
			try {
				if(buff != null) {
					buff.close();
				}
				if(in != null) {
					in.close();
				}
				if(fin != null) {
					fin.close();
				}
			} catch (IOException e) {
				Log.printStackTrace(e);
			}
		}
	}
	
	public void writeFile() {
		if(this.pro != null) {
			FileOutputStream fos = null;
			try{
				fos = new FileOutputStream(configFile);
				for(String s : pro) {
					fos.write(s.getBytes("UTF-8"));
					fos.write("\r\n".getBytes("UTF-8"));
				}
			}catch(Exception e) {
				Log.printStackTrace(e);
			}finally{
				try {
					if(fos != null) {
						fos.close();
					}
				} catch (IOException e) {
					Log.printStackTrace(e);
				}
			}
		}
	}

	public static void main(String[] args) {
		UnixNetworkHandle handle = UnixNetworkHandle.getInstance();
		NetworkInfo info = handle.getNetworkInfo(0);
		System.out.println(info.getIndex());
		System.out.println(info.getIp());
		System.out.println(info.getMask());
		System.out.println(info.getGateway());
		System.out.println("----------------");
		info.setIp("192.168.10.222");
		info.setGateway("192.168.10.254");
		handle.setNetworkInfo(info);
		
	}
}
