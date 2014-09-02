package com.santrong.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author weinianjie
 * @date 2014年7月18日
 * @time 下午5:29:22
 */
public class FileUtils {
	
	public static void main(String[] args) {
		System.out.println(File.separator);
	}
	
	/**
	 * 删除文件
	 * @param fullname 文件全路径
	 * @return
	 */
	public static boolean delFile(String fullname) {
		File file = new File(fullname);
		if(file != null && file.isFile() && file.exists()) {
			return file.delete(); 
		}
		return false;
	}
	
	/**
	 * 获取指定目录下的所有子目录很文件
	 * @param dirname
	 * @return
	 */
	public static List<File> getAllFileAndDirectory(String dirname) {
		List<File> fileList = new ArrayList<File>();
		File dir = new File(dirname);
		if (null != dir && dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (null != files && files.length > 0) {
				for (File file : files) {
					if (null != file) {
						fileList.add(file);
						if(file.isDirectory()) {
							fileList.addAll(getAllFileAndDirectory(file.getPath()));
						}
					}
				}
			}
		}
		return fileList;
	}
	
	/**
	 * 目录下文件查找，不含子目录
	 * @param dirname
	 * @param extName
	 * @return
	 */
	public static List<File> searchFile(String dirname, String extName) {
		List<File> fileNameList = new ArrayList<File>();
		List<File> fileList = new ArrayList<File>();
		File dir = new File(dirname);
		if (null != dir && dir.isDirectory()) {
			File[] files = dir.listFiles();
			if (null != files && files.length > 0) {
				for (File file : files) {
					if (null != file && file.isFile()) {
						if(extName != null && !extName.equals("")) {
							if (file.getName().toLowerCase().endsWith(extName.toLowerCase())) {
								fileNameList.add(file);
							}
						}else{
							fileNameList.add(file);
						}
					}
				}

				// 排序开始
				File[] targetFiles = new File[fileNameList.size()];
				for (int i = 0; i < fileNameList.size(); i++) {
					targetFiles[i] = fileNameList.get(i);
				}

				File fileTemp = null;
				for (int i = 0; i < targetFiles.length; i++) {
					for (int j = i; j < targetFiles.length; j++) {
						long diff = targetFiles[i].lastModified()
								- targetFiles[j].lastModified();
						if (diff < 0) {
							fileTemp = targetFiles[i];
							targetFiles[i] = targetFiles[j];
							targetFiles[j] = fileTemp;
						}
					}
				}

				fileList = new ArrayList<File>();
				for (int i = 0; i < targetFiles.length; i++) {
					fileList.add(targetFiles[i]);
				}

			}
		}
		return fileList;
	}
	
	public static List<File> searchFile(String dirname) {
		return searchFile(dirname, null);
	}
}
