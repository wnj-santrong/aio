-- 2014-07-14 weinianjie
set names utf8;
create database if not exists aio;
use aio;

-- 用户表 ---
drop table if exists web_user;
create table web_user(
	id varchar(32) not null comment 'UUID',
	showName varchar(128) not null comment '显示名',
	username varchar(64) not null comment '用户名',
	password varchar(64) not null comment '密码',
	cts datetime comment '创建时间',
	uts datetime comment '修改时间',
	primary key (id)
) engine=InnoDB default charset=utf8 collate=utf8_bin;

insert into web_user values('10000', 'admin', 'admin', md5('admin'), now(), now());


-- 菜单表 ---
drop table if exists web_menu;
create table web_menu(
	id varchar(32) not null comment 'UUID',
	menuName varchar(32) not null comment '菜单名',
	parentId varchar(32) not null default '0' comment '父UUID',
	pageUrl varchar(256) not null comment '页面地址',
	priority int(4) default 0 comment '优先级',
	cts datetime comment '创建时间',
	uts datetime comment '修改时间',
	primary key (id)
) engine=InnoDB default charset=utf8 collate=utf8_bin;

insert into web_menu values('10000', 'meetingMgr', '0', 'meeting/home.action', 0, now(), now());
insert into web_menu values('10001', 'filePlay', '0', 'play/home.action', 100, now(), now());
insert into web_menu values('10002', 'fileMgr', '0', 'file/home.action', 200, now(), now());
insert into web_menu values('10003', 'systemMgr', '0', 'setting/home.action', 300, now(), now());
insert into web_menu values('10004', 'systemInfo', '0', 'info/home.action', 400, now(), now());
insert into web_menu values('10005', 'conPlatform', '0', 'plt/home.action', 500, now(), now());


-- 课件表 ---
drop table if exists web_file;
create table web_file(
	id varchar(32) not null comment 'UUID',
	fileName varchar(256) not null comment '课件存储名称',
	courseName varchar(256) comment'课程名称',
	teacher varchar(256) comment '老师',
	remark varchar(10240) comment '课程概要',
	fileSize bigint(20) not null default 0 comment '文件大小',
	tarSize bigint(20) not null default 0 comment 'tar以后的大小，下载时候使用，防止重复计算',
	duration varchar(32) default '' comment '录制时长',
	recordType int(4) not null default 1 comment '录制类型（资源、电影、合成）',
	status int(4) not null default 1 comment '课件状态，0正在录制，1录制完成，2已上传中，3上传完成',
	level int(4) not null default 0 comment '课件级别，0开放，1不开放',
	channel int(4) not null default 1 comment '产生课件的会议室通道',
	bitRate int(4) comment '码率单位kbps',
	resolution int(4) comment '分辨率',
	playCount int(4) not null default 0 comment '点播次数',
	downloadCount int(4) not null default 0 comment '下载次数',
	cts datetime comment '创建时间',
	uts datetime comment '修改时间',
	primary key (id)
) engine=InnoDB default charset=utf8 collate=utf8_bin;

insert into web_file values(replace(uuid(), '-', ''), 'file1', '课程1', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file2', '课程2', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file3', '课程3', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file4', '课程4', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file5', '课程5', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file6', '课程6', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file7', '课程7', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file8', '课程8', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file9', '课程9', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file10', '课程10', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file11', '课程11', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file12', '课程12', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file13', '课程13', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file14', '课程14', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file15', '课程15', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file16', '课程16', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file17', '课程17', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file18', '课程18', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file19', '课程19', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file20', '课程20', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file21', '课程21', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file22', '课程22', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file23', '课程23', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file24', '课程24', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file25', '课程25', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), 'file26', '课程26', '','', 1024, 0, '01:28', 1, 1, 0, 1, 768, 1, 1, 1, now(), now());

-- 推送到云表 ---
drop table if exists web_file_push;
create table web_file_push(
	id varchar(32) not null comment 'UUID',
	fileId varchar(32) not null comment '文件ID',
	username varchar(64) not null comment '平台用户名',
	status int(4) not null default 1 comment '推送状态，0待推送，1推送中，2推送异常，3推送完成',
	remoteId varchar(32) not null comment '标识ID',
	cts datetime comment '创建时间',
	primary key (id)
) engine=InnoDB default charset=utf8 collate=utf8_bin;

-- 会议表 ---
drop table if exists web_meeting;
create table web_meeting(
	id varchar(32) not null comment 'UUID',
	courseName varchar(256) comment'课程名称',
	teacher varchar(256) comment '老师',
	remark varchar(10240) comment '课程概要',
	bitRate int(4) comment '码率单位kbps',
	resolution int(4) comment '分辨率',
	maxTime int(4) not null comment '允许录制最大时间分钟',
	useRecord int(4) not null default 0 comment '是否录制',
	recordMode int(4) not null default 0 comment '录制模式（视频布局）',
	recordType int(4) not null default 1 comment '录制类型（资源、电影、合成）',
	channel int(4) not null default 1 comment '通道号，1开始',
	cts datetime comment '创建时间',
	uts datetime comment '修改时间',
	primary key (id)
) engine=InnoDB default charset=utf8 collate=utf8_bin;

insert into web_meeting values('10000', '', '', '', 1024, 22, 480, 0, 1, 4, 1, now(), now());

-- 数据源表 ---
drop table if exists web_datasource;
create table web_datasource(
	id varchar(32) not null comment 'UUID',
	addr varchar(256) comment '地址',
	port int(4) comment'端口',
	username varchar(256) comment '用户名',
	password varchar(256) comment '密码',
	meetingId varchar(32) not null comment '会议ID',
	dsType int(4) not null comment '数据源类型',
	priority int(4) default 0 comment '优先级',
	cts datetime comment '创建时间',
	uts datetime comment '修改时间',
	primary key (id)
) engine=InnoDB default charset=utf8 collate=utf8_bin;

-- 标签表 ---
drop table if exists web_tag;
create table web_tag(
	id varchar(32) not null comment 'UUID',
	tagName varchar(256) comment '地址',
	priority int(4) default 0 comment '优先级',
	cts datetime comment '创建时间',
	uts datetime comment '修改时间',
	primary key (id)
) engine=InnoDB default charset=utf8 collate=utf8_bin;
insert into web_tag values('10000', 'CLSRM', 0, now(), now());

-- 操作日志表 ---
drop table if exists web_opt_log;
create table web_opt_log(
	id varchar(32) not null comment 'UUID',
	username varchar(128) comment '用户名',
	title varchar(128) comment '标题',
	content varchar(1024) comment '内容',
	ip varchar(32) comment 'IP',
	cts datetime comment '创建时间',
	uts datetime comment '修改时间',
	primary key (id)
) engine=InnoDB default charset=utf8 collate=utf8_bin;

-- 操作日志表 ---
drop table if exists web_request_log;
create table web_request_log(
	id varchar(32) not null comment 'UUID',
	uri varchar(64) comment 'uri',
	param varchar(256) comment '参数',
	method varchar(16) comment '请求方法',
	ip varchar(32) comment 'IP',
	cts datetime comment '创建时间',
	uts datetime comment '修改时间',
	primary key (id)
) engine=InnoDB default charset=utf8 collate=utf8_bin;