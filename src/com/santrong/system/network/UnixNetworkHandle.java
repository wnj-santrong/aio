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

/**
 * @author weinianjie
 * @date 2014年7月18日
 * @time 上午9:47:38
 */
public class UnixNetworkHandle extends AbstractNetworkHandle {
//	private static final String configFile = "/etc/network/interfaces";
	private static final String configFile = "E:\\workspace\\data\\other\\network.txt";
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
			readFile();
			
			for(int i=0;i<pro.size();i++) {
				if(pro.get(i).indexOf("eth" + vo.getIndex()) != -1) {// 必须是eth0这类的
					
					pro.set(i + 2, "address " + vo.getIp());
					pro.set(i + 3, "network " + vo.getGateway());
					pro.set(i + 4, "netmask " + vo.getMask());
					
					break;
				}
			}
			
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
		NetworkInfo info = null;
		this.readFile();
		for(int i=0;i<pro.size();i++) {
			if(pro.get(i).indexOf("eth" + index) != -1) {// 必须是eth0这类的
				HashMap<String, String> map = new HashMap<String, String>();
				
				for(int j = 2; j<5; j++) {
					String line = pro.get(i + j);
					if(line != null && line != "" && line.indexOf(" ") != -1) {
						String[] arr = line.split("\\s+");
						map.put(arr[0], arr[1]);
					}
				}
				
				info = new NetworkInfo();
				info.setIndex(index);
				info.setIp(map.get("address"));
				info.setMask(map.get("netmask"));
				info.setGateway(map.get("network"));
				break;
			}
		}
		
		return info;
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
