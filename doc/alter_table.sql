-- 2014-07-14 weinianjie
create database aio;

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


-- 课件表 ---
drop table if exists web_file;
create table web_file(
	id varchar(32) not null comment 'UUID',
	showName varchar(256) not null comment '课件显示名称',
	fileName varchar(256) not null comment '课件存储名称',
	courseName varchar(256) comment'课程名称',
	teacher varchar(256) comment '老师',
	className varchar(256) comment '年级',
	gradeName varchar(256) comment '班级',
	remark varchar(10240) comment '课程概要',
	fileSize bigint(20) not null default 0 comment '文件大小',
	duration varchar(32) default '' comment '录制时长',
	status int(4) not null default -1 comment '课件状态， -1删除，0正在录制，1录制完成',
	level int(4) not null default 0 comment '课件级别，0开放，1不开放',
	playCount int(4) not null default 0 comment '点播次数',
	downloadCount int(4) not null default 0 comment '下载次数',
	cts datetime comment '创建时间',
	uts datetime comment '修改时间',
	primary key (id)
) engine=InnoDB default charset=utf8 collate=utf8_bin;

insert into web_file values(replace(uuid(), '-', ''), '课件1', 'file1', '课程1', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件2', 'file2', '课程2', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件3', 'file3', '课程3', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件4', 'file4', '课程4', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件5', 'file5', '课程5', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件6', 'file6', '课程6', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件7', 'file7', '课程7', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件8', 'file8', '课程8', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件9', 'file9', '课程9', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件10', 'file10', '课程10', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件11', 'file11', '课程11', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件12', 'file12', '课程12', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件13', 'file13', '课程13', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件14', 'file14', '课程14', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件15', 'file15', '课程15', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件16', 'file16', '课程16', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件17', 'file17', '课程17', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件18', 'file18', '课程18', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件19', 'file19', '课程19', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件20', 'file20', '课程20', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件21', 'file21', '课程21', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件22', 'file22', '课程22', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件23', 'file23', '课程23', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件24', 'file24', '课程24', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件25', 'file25', '课程25', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());
insert into web_file values(replace(uuid(), '-', ''), '课件26', 'file26', '课程26', '', '', '','', 1024, '01:28', 1, 0, 1, 1, now(), now());