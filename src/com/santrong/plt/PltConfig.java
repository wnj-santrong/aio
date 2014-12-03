package com.santrong.plt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PltConfig {
	private static final Logger logger = Logger.getLogger(PltConfig.class);

	private static Properties proplist;
	private final String conf_file = this.getClass().getResource("/").getPath() + "plt.properties";

	private String username;
	private String password; 

	public PltConfig() {
		username		= "";
		password 		= "";
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

				this.username = proplist.getProperty("username");
				this.password = proplist.getProperty("password");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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
			proplist.setProperty("username", username);
			proplist.setProperty("password", password);
			proplist.store(fos, "Copyright (c) Santrong");
			fos.flush();
			fos.close();
			return true;
		}catch(Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
