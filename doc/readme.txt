一：目录结构
/opt/AIO/Service/
	|___/webservice/
	|___/control/
	|___/sources/
	|___/recorder/
	|___/uniserver/
	|___/vodserver
	|___/maintain/
	|___/update/
	|___/lcd/
	|___其他公共配置文件
/data/
|___/教室ID1
   /___/录制名1			//录制指令指定的录制路径到此层
		|___/rsrc		//资源模式课件
		|___/mv			//电影模式课件
		|___/cmps		//合成模式课件
   /___/录制名2
|___/教室ID2
   /___/录制名1
   /___/录制名2

1、webservice，系统管理和业务管理。包括系统设置、用户管理、日志管理、会议管理、资源管理、系统状态展示等功能。
2、control，协议层和调度层。包括协议解析、任务分发、模块调度、总体资源控制。
3、sources，数据源服务。数据源连接、数据分发等功能。每个进程负责一个数据的连接
4、recorder，录制服务。录制功能，每个进程负责一个会议或教室的录制。
5、uniserver，直播服务。对外提供直播服务，
6、vodserver，点播服务。负责对外提供点播服务，一台设备有且仅有一个点播服务，负责所有文件的对外点播。
7、maintain，监控进程。负责系统中所有服务的守护，出现异常时及时恢复现场。

二：webservice结构
.../webservice/
	|___/jdk1.7.0_60/				//jdk目录
	|___/apache-tomcat-7.0.54/		//tomcat目录
	|___/webapp/					//应用目录
		|___/shell/					//应用中用到的shell脚本目录
	|___/appbackup/					//应用备份目录
	|___/dbbackup/					//数据库备份目录
	|___/upload/					//用户上传数据目录