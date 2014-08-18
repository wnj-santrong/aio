package com.santrong.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.santrong.log.Log;

public class UpdateConfig {
	private static Properties proplist;
	private final String conf_file = this.getClass().getResource("/").getPath() + "update.properties";

	private String autoUpdate;
	private String updateTime;

	public UpdateConfig() {
		autoUpdate 		= "0";
		updateTime 		= "00:00";
		read();
	}
	
	/**
	 * 读取配置文件
	 * 
	 * @throws IOException
	 */
	public synchronized void read() {
		try {
			if (new File(conf_file).exists()) {
				FileInputStream fin = new FileInputStream(conf_file);
				proplist = new Properties();
				proplist.load(fin);
				fin.close();

				this.autoUpdate = proplist.getProperty("autoUpdate");
				this.updateTime = proplist.getProperty("updateTime");
			}
		} catch (Exception e) {
			Log.printStackTrace(e);
		}
	}

	/**
	 * 更新配置文件
	 * 
	 * @throws IOException
	 * @throws TemplateException
	 */
	public synchronized boolean write(){
		try {
			FileOutputStream fos = new FileOutputStream(conf_file);
			if (proplist == null) {
				proplist = new Properties();
			}
			proplist.setProperty("autoUpdate", autoUpdate);
			proplist.setProperty("updateTime", updateTime);
			proplist.store(fos, "Copyright (c) Santrong");
			fos.flush();
			fos.close();
			return true;
		}catch(Exception e) {
			Log.printStackTrace(e);
		}
		return false;
	}

	public String getAutoUpdate() {
		return autoUpdate;
	}

	public void setAutoUpdate(String autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
}
