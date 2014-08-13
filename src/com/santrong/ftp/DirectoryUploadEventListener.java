package com.santrong.ftp;

import java.util.HashMap;

import com.santrong.file.dao.FileDao;
import com.santrong.file.entry.FileItem;

/**
 * @author weinianjie
 * @date 2014年8月13日
 * @time 上午10:22:15
 */
public class DirectoryUploadEventListener implements EventListener {

	@Override
	public void afterFinish(HashMap<String, Object> mapper) {
		String fileId = (String)mapper.get("fileId");
		if(fileId != null) {
			FileDao dao = new FileDao();
			FileItem file = dao.selectById(fileId);
			if(file != null) {
				file.setStatus(FileItem.File_Status_Recorded);
				dao.update(file);
			}
		}
		
	}

}
