package com.santrong.system;

import com.santrong.tcp.TcpDefine;
import com.santrong.util.XmlReader;


public class Global {
	
	/*
	 * static final config
	 */
	public static final String Default_Encoding = "UTF-8";
	public static final String Module_Sign = "id_web_conn";
	public static final String LoginUser_key = "loginUser";
    
	
    /*
     * [DataSource]
     */
    public static String CameraUsername = "admin";						// 摄像头默认用户名
    public static String CameraPassword = "12345";						// 摄像头密码
    public static int CameraPort = 80;									// 摄像头默认端口
    public static int VedioCount = 3;									// 视频路数，VGA + Camera
    
    
    /*
     * [System]
     */
    public static String Title = "三简课程直播录制系统";
    public static String Language = "zh_CN";
    public static int WanIndex = 1;										// wan网口的索引
    public static int LanIndex = 0;										// lan网口的索引
    public static String LanDeviceName = "";							// lan网口的网口名
    public static String WanDeviceName = "";							// wan网口的网口名
    public static int DownloadMaxCount = 10;                            // 控制前台下载人数
    public static int HeartInterval = 10000;							// 心跳时间
    public static int HeartTimeout = 15000;								// 心跳过时时间
    
    /*
     * [Ftp]
     */
    public static int FTPConnectMode = 0;								// 0主动模式，1被动模式
    

    static {
        String configFile = Global.class.getClassLoader().getResource("") + "Global.ini";
        if (configFile.startsWith("file:/")) {
            configFile = configFile.substring(5);
        }
        
        Ini ini = new Ini();
        if (ini.read(configFile)) {

            CameraUsername = ini.readString("DataSource", "CameraUsername", CameraUsername);
            CameraPassword = ini.readString("DataSource", "CameraPassword", CameraPassword);
            CameraPort = ini.readInt("DataSource", "CameraPort", CameraPort);
            VedioCount = ini.readInt("DataSource", "VedioCount", VedioCount);

            Title = ini.readString("System", "Title", Title);
            Language = ini.readString("System", "Language", Language);
            WanIndex = ini.readInt("System", "WanIndex", WanIndex);
            LanIndex = ini.readInt("System", "LanIndex", LanIndex);
            WanDeviceName = ini.readString("System", "WanDeviceName", WanDeviceName);
            LanDeviceName = ini.readString("System", "LanDeviceName", LanDeviceName);
            DownloadMaxCount =ini.readInt("System", "DownloadMaxCount", DownloadMaxCount);
            HeartInterval =ini.readInt("System", "HeartInterval", HeartInterval);
            HeartTimeout =ini.readInt("System", "HeartTimeout", HeartTimeout);
            
            FTPConnectMode =ini.readInt("Ftp", "FTPConnectMode", FTPConnectMode);
        }
        
        
        // 读取共享的配置文件
        XmlReader xml = new XmlReader();
        xml.open(DirDefine.SysConfigDir + "/aio-cfg.xml");
        
        TcpDefine.Basic_Client_Addr = xml.getString("/control/addr", TcpDefine.Basic_Client_Addr);
        TcpDefine.Basic_Client_Port = xml.getInt("/control/port", TcpDefine.Basic_Client_Port);
        TcpDefine.Main_Client_Addr = xml.getString("/maintain/addr", TcpDefine.Main_Client_Addr);
        TcpDefine.Main_Client_Port = xml.getInt("/maintain/port", TcpDefine.Main_Client_Port);
    }
    
}
