package com.santrong.ftp;

import java.io.File;  
import java.io.FileInputStream;  
import org.apache.commons.net.ftp.FTPClient;  
import org.apache.commons.net.ftp.FTPReply;

/**
 * @author weinianjie
 * @date 2014年8月11日
 * @time 下午3:50:27
 */
public class FtpUploadService {

	private  FTPClient ftp;
    /** 
     *  
     * @param path 上传到ftp服务器哪个路径下    
     * @param addr 地址 
     * @param port 端口号 
     * @param username 用户名 
     * @param password 密码 
     * @return 
     * @throws Exception 
     */  
    private  boolean connect(String path,String addr,int port,String username,String password) throws Exception {
        boolean result = false;
        ftp = new FTPClient();
        int reply;
        ftp.connect(addr,port);
        ftp.login(username,password);
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {    
            ftp.disconnect();
            return result;
        }    
        ftp.changeWorkingDirectory(path);
        result = true;
        return result;
    }    
    /** 
     *  
     * @param file 上传的文件或文件夹 
     * @throws Exception 
     */
    private void upload(File file) throws Exception{
        if(file.isDirectory()){
            ftp.makeDirectory(file.getName());
            ftp.changeWorkingDirectory(file.getName());
            String[] files = file.list();
            for (int i = 0; i < files.length; i++) {
                File file1 = new File(file.getPath()+"\\"+files[i] );
                if(file1.isDirectory()){
                    upload(file1);
                    ftp.changeToParentDirectory();
                }else{
                    File file2 = new File(file.getPath()+"\\"+files[i]);
                    FileInputStream input = new FileInputStream(file2);
                    ftp.storeFile(file2.getName(), input);
                    input.close();
                }               
            }
        }else{
            File file2 = new File(file.getPath());
            FileInputStream input = new FileInputStream(file2);
            ftp.storeFile(file2.getName(), input);
            input.close();
        }
    }    
   public static void main(String[] args) throws Exception{
	  FtpUploadService t = new FtpUploadService();
      t.connect("", "192.168.10.178", 21, "anonymous", "weinianjie@163.com");
      File file = new File("e:\\workspace\\data");
      t.upload(file);
   }  
}