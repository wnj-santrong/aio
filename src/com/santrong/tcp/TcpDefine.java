package com.santrong.tcp;

/**
 * @Author weinianjie
 * @Date 2014-7-9
 * @Time 上午1:26:41
 */
public class TcpDefine {
	public static final int Basic_Client_Port = 30000;
	public static final int Basic_Server_port = 9009;
	
	public static final String Xml_Header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public static final String ModuleSign = "id_web_conn";
	public static final String Default_Encoding = "UTF-8";
	
	public static final String Basic_Client_Login 				= "31001";	// 连接请求
	public static final String Basic_Client_Logout 				= "31002";	// 断开连接
	public static final String Basic_Client_StartConfRecord 	= "31004";	// 开始会议
	public static final String Basic_Client_StopConfRecord 		= "31005";	// 结束会议请求
	public static final String Basic_Client_RecordCtl 			= "31006";	// 启动/停止录制请求 （暂停/继续录制）
	public static final String Basic_Client_GetConfInfo 		= "31007";	// 获取会议列表信息请求
	public static final String Basic_Client_SetThreshold 		= "31008";	// 设定录制资源阀值
	public static final String Basic_Client_GetResource 		= "31009";	// 获取负载状态请求（支持的最大通道数，已用通道数，直播数，点播数，磁盘占用情况）
	public static final String Basic_Client_DeleteCourse 		= "31010";	// 删除课件
	public static final String Basic_Client_SetLogLevel 		= "31011";	// 设置日志等级
	public static final String Basic_Client_TiltCtrl 			= "31012";	// 摄像头控制
	public static final String Basic_Client_DirectCtrl 			= "31013";	// 导播控制
	public static final String Basic_Client_AddSource 			= "31014";	// 添加数据源
	public static final String Basic_Client_DelSource 			= "31015";	// 删除数据源
	public static final String Basic_Client_GetSourceState 		= "31016";	// 获取数据源状态
}
