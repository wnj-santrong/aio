package com.santrong;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.logicalcobwebs.proxool.configuration.PropertyConfigurator;

import com.santrong.system.network.SystemUtils;




/**
 * @author weinianjie
 * @date 2014年7月24日
 * @time 下午5:28:37
 */
public class Test {
	public static void main(String[] args) {
		Properties dbProps = new Properties();
		try {
			dbProps.load(new FileInputStream(new File("E:\\workspace\\plt\\WebRoot\\WEB-INF\\classes\\datasource.properties")));
			if(SystemUtils.getOsType() == SystemUtils.WINDOWS) {// 开发坏境不进行链接测试
//				dbProps.setProperty("jdbc-0.proxool.house-keeping-test-sql", null);
//				dbProps.remove("jdbc-0.proxool.house-keeping-test-sql");
			}				
			PropertyConfigurator.configure(dbProps);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
