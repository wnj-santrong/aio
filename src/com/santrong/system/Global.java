package com.santrong.system;

import com.santrong.tcp.TcpDefine;
import com.santrong.util.XmlReader;


public class Global {
	
	/*
	 * static final config
	 */
	public static final String Default_Encoding = "UTF-8";
	public static final String Module_Sign = "id_web_conn";
	public static final String SessionKey_LoginUser = "loginUser";
    
	
    /*
     * [System]
     */
    public static String Version = "";
    public static String Title = "三简课程直播录制系统";
    public static String Language = "zh_CN";
    public static String LanDeviceName = "eth0";								// lan网口的网口名
    public static String WanDeviceName = "eth1";								// wan网口的网口名
    public static int DownloadMaxCount = 10;                            		// 控制下载人数
    public static int HeartInterval = 10000;									// 心跳时间
    public static int HeartTimeout = 15000;										// 心跳过期时间
    public static long DiskErrorSize = 2048;									// 禁止录制的磁盘空间剩余量，单位M
    public static long DiskErrorSizeCancel = 3072;								// 禁止录制解除的磁盘空间剩余量，单位M
    public static long DiskWainSize = 10240;									// 磁盘剩余空间不足的提醒值，单位M
    public static long DiskWainSizeCancel = 11264;								// 磁盘剩余空间不足的提醒解除值，单位M
    public static int UploadFileSizeLimit = 150;								// 升级文件大小限制，单位M----请确保tomcat限制大小不小于用户配置大小
    public static boolean OpenPlatform = true;								// 是否开启课云平台功能
    
    
    /*
     * [DataSource]
     */
    public static String CameraUsername = "admin";						// 摄像头默认用户名
    public static String CameraPassword = "12345";						// 摄像头密码
    public static int CameraPort = 80;									// 摄像头默认端口
    public static int VedioCount = 3;									// 视频路数，VGA + Camera    
    
    
    /*
     * [Ftp]
     */
    public static int FTPConnectMode = 0;								// 0主动模式，1被动模式
    

    static {
        String configFile = Global.class.getClassLoader().getResource("") + "Config.ini";
        if (configFile.startsWith("file:/")) {
            configFile = configFile.substring(5);
        }
        
        Ini ini = new Ini();
        if (ini.read(configFile)) {
            Version = ini.readString("System", "Version", Version);
            Title = ini.readString("System", "Title", Title);
            Language = ini.readString("System", "Language", Language);
            WanDeviceName = ini.readString("System", "WanDeviceName", WanDeviceName);
            LanDeviceName = ini.readString("System", "LanDeviceName", LanDeviceName);
            DownloadMaxCount =ini.readInt("System", "DownloadMaxCount", DownloadMaxCount);
            HeartInterval =ini.readInt("System", "HeartInterval", HeartInterval);
            HeartTimeout =ini.readInt("System", "HeartTimeout", HeartTimeout);
            DiskErrorSize =ini.readLong("System", "DiskErrorSize", DiskErrorSize);
            DiskErrorSizeCancel =ini.readLong("System", "DiskErrorSizeCancel", DiskErrorSizeCancel);
            DiskWainSize =ini.readLong("System", "DiskWainSize", DiskWainSize);
            DiskWainSizeCancel =ini.readLong("System", "DiskWainSizeCancel", DiskWainSizeCancel);
            UploadFileSizeLimit =ini.readInt("System", "UploadFileSizeLimit", UploadFileSizeLimit);
            OpenPlatform =ini.readBoolean("System", "OpenPlatform", OpenPlatform);
            
            CameraUsername = ini.readString("DataSource", "CameraUsername", CameraUsername);
            CameraPassword = ini.readString("DataSource", "CameraPassword", CameraPassword);
            CameraPort = ini.readInt("DataSource", "CameraPort", CameraPort);
            VedioCount = ini.readInt("DataSource", "VedioCount", VedioCount);            
            
            FTPConnectMode =ini.readInt("Ftp", "FTPConnectMode", FTPConnectMode);
        }
        
        
        // 读取共享的配置文件
        XmlReader xml = new XmlReader();
        
        xml.open(DirDefine.SysConfigDir + "/aio-cfg.xml");
        TcpDefine.Basic_Client_Addr = xml.getString("/control/addr", TcpDefine.Basic_Client_Addr);
        TcpDefine.Basic_Client_Port = xml.getInt("/control/port", TcpDefine.Basic_Client_Port);
        TcpDefine.Main_Client_Addr = xml.getString("/maintain/addr", TcpDefine.Main_Client_Addr);
        TcpDefine.Main_Client_Port = xml.getInt("/maintain/port", TcpDefine.Main_Client_Port);
        
        xml.open(DirDefine.SysConfigDir + "/maintain.xml");
        SystemUpdateService.OnlineUpdateAddr = xml.getString("/UpdateMode/HttpUrl", SystemUpdateService.OnlineUpdateAddr);
    }
    
}
