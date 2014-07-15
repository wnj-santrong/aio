-- 2014-07-14 weinianjie
create database aio;

-- 用户表 ---
create table web_user(
	id varchar(32) not null comment 'UUID',
	showname varchar(128) not null comment '显示名',
	username varchar(64) not null comment '用户名',
	password varchar(64) not null comment '密码'
) engine=InnoDB default charset=utf8 collate=utf8_bin;

insert into web_user values('10000', 'admin', 'admin', md5('admin'));

-- 菜单表 ---
create table web_menu(
	id varchar(32) not null comment 'UUID',
	menuname varchar(32) not null comment '菜单名',
	parentId varchar(32) not null default '0' comment '父UUID',
	pageUrl varchar(256) not null comment '页面地址',
	priority int(4) default 0 comment '优先级'
) engine=InnoDB default charset=utf8 collate=utf8_bin;

insert into web_menu values('10000', 'meetingMgr', '0', 'meeting/home.action', 0);
insert into web_menu values('10003', 'filePlay', '0', 'play/home.action', 100);
insert into web_menu values('10001', 'fileMgr', '0', 'file/home.action', 200);
insert into web_menu values('10002', 'systemMgr', '0', 'setting/home.action', 300);
insert into web_menu values('10003', 'systemInfo', '0', 'info/home.action', 400);

--insert into web_menu values('10100', 'meetingBegin', '10000', 0);
--insert into web_menu values('10101', 'meetingHistory', '10000', 1);
--insert into web_menu values('10200', 'fileCategory', '10001', 0);
--insert into web_menu values('10201', 'fileList', '10001', 1);
--insert into web_menu values('10202', 'fileOpen', '10001', 2);
--insert into web_menu values('10300', 'warnList', '10002', 0);
--insert into web_menu values('10301', 'logList', '10002', 1);
--insert into web_menu values('10400', 'systemInfo', '10003', 0);
--insert into web_menu values('10401', 'roomSetting', '10003', 1);
--insert into web_menu values('10402', 'netSetting', '10003', 2);
--insert into web_menu values('10403', 'ftpSetting', '10003', 3);
--insert into web_menu values('10404', 'webNotice', '10003', 4);
--insert into web_menu values('10405', 'sqlBackup', '10003', 5);
--insert into web_menu values('10406', 'changePwd', '10003', 6);
--insert into web_menu values('10407', 'upgrade', '10003', 7);
