SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS sys_api;
DROP TABLE IF EXISTS sys_app_version;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_org;
DROP TABLE IF EXISTS sys_area;
DROP TABLE IF EXISTS sys_attach;
DROP TABLE IF EXISTS sys_auth;
DROP TABLE IF EXISTS sys_bucket;
DROP TABLE IF EXISTS sys_dict;
DROP TABLE IF EXISTS sys_func;
DROP TABLE IF EXISTS sys_log;
DROP TABLE IF EXISTS sys_param;
DROP TABLE IF EXISTS sys_relate;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_vertical_domain;




/* Create Tables */

CREATE TABLE sys_api
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	api_id varchar(32) COMMENT '接口编号',
	api_name varchar(32) COMMENT '接口名称',
	api_type varchar(32) COMMENT '接口类型',
	url varchar(256) COMMENT '接口地址',
	parent_id varchar(32) DEFAULT '0' COMMENT '上级编码',
	sys_id varchar(32) COMMENT '系统编号',
	description varchar(256) COMMENT '描述',
	enable boolean DEFAULT '1' COMMENT '是否启用',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (api_id)
) COMMENT = '接口';


CREATE TABLE sys_app_version
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	app_id varchar(32) COMMENT '应用编号',
	version_no varchar(32) COMMENT '版本号',
	version_serial int COMMENT '版本序列',
	content varchar(4000) COMMENT '内容描述',
	enforce boolean COMMENT '是否强制更新',
	publisher varchar(32) COMMENT '发布人',
	publish_time datetime COMMENT '发布时间',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	CONSTRAINT app_version UNIQUE (app_id, version_no),
	CONSTRAINT app_version_serial UNIQUE (app_id, version_serial)
) COMMENT = '应用版本';


CREATE TABLE sys_area
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	area_id varchar(32) COMMENT '地区编码',
	area_name varchar(64) COMMENT '地区名称',
	parent_id varchar(32) COMMENT '上级编码',
	level int unsigned COMMENT '地区等级',
	x double COMMENT '经度',
	y double COMMENT '纬度',
	shape geometry COMMENT '空间几何数据',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (area_id)
) COMMENT = '地区';


CREATE TABLE sys_attach
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	domain_id varchar(32) COMMENT '模型标识',
	row_id varchar(32) COMMENT '行标识',
	field_id varchar(32) COMMENT '字段标识',
	original_name varchar(64) COMMENT '原名称',
	name varchar(64) COMMENT '真实名称',
	type varchar(32) COMMENT '附件类型',
	size bigint unsigned COMMENT '附件大小',
	storage varchar(32) COMMENT '存储类型',
	bucket varchar(32) COMMENT '存储板块',
	bucket_name varchar(32) COMMENT '板块名称',
	presigned_url varchar(256) COMMENT '预签名地址',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id)
) COMMENT = '附件';


CREATE TABLE sys_auth
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	auth_id varchar(32) COMMENT '权限编号',
	auth_type varchar(32) COMMENT '权限类型',
	relate_type varchar(32) COMMENT '关系类型',
	auth_mode varchar(32) COMMENT '权限模式',
	object_id varchar(32) COMMENT '对象编号',
	object_name varchar(32) COMMENT '对象名称',
	enable boolean DEFAULT '1' COMMENT '是否启用',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (auth_id)
) COMMENT = '权限';


CREATE TABLE sys_bucket
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	bucket varchar(32) COMMENT '存储板块',
	name varchar(32) COMMENT '描述名称',
	storage varchar(32) COMMENT '存储类型',
	description varchar(256) COMMENT '描述',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (bucket)
) COMMENT = '附件存储模块';


CREATE TABLE sys_dict
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	code varchar(32) COMMENT '字典代码',
	name varchar(32) COMMENT '字典名称',
	parent_id varchar(32) DEFAULT '-' COMMENT '上级编码',
	sort int unsigned COMMENT '排序',
	description varchar(256) COMMENT '描述',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	CONSTRAINT uniq_code UNIQUE (code, parent_id)
) COMMENT = '字典';


CREATE TABLE sys_func
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	func_id varchar(32) COMMENT '功能编号',
	func_name varchar(32) COMMENT '功能名称',
	func_type varchar(32) COMMENT '功能类型',
	parent_id varchar(32) DEFAULT '0' COMMENT '上级编码',
	sys_id varchar(32) COMMENT '系统编号',
	icon varchar(32) COMMENT '图标',
	path varchar(64) COMMENT '链接路径',
	auth varchar(32) COMMENT '认证标识',
	sort int unsigned COMMENT '排序',
	display boolean DEFAULT '1' COMMENT '是否显示',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (func_id)
) COMMENT = '功能';


CREATE TABLE sys_log
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	log_type varchar(32) COMMENT '日志类型',
	sys_id varchar(32) COMMENT '系统编号',
	info varchar(1000) COMMENT '操作信息',
	path varchar(512) COMMENT '请求路径',
	param varchar(1000) COMMENT '参数',
	ip varchar(64) COMMENT 'IP地址',
	user_id varchar(32) COMMENT '用户编号',
	exception varchar(1000) COMMENT '异常信息',
	record_time timestamp DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id)
) COMMENT = '日志';


CREATE TABLE sys_org
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	org_id varchar(32) COMMENT '机构代码',
	org_name varchar(32) COMMENT '机构名称',
	org_type varchar(32) COMMENT '机构类型',
	parent_id varchar(32) COMMENT '上级编码',
	sort int unsigned COMMENT '排序',
	area_id varchar(32) COMMENT '地区编码',
	area_name varchar(64) COMMENT '地区名称',
	person varchar(32) COMMENT '联系人',
	tel varchar(16) COMMENT '联系电话',
	address varchar(256) COMMENT '地址',
	email varchar(128) COMMENT '电子邮箱',
	fax varchar(16) COMMENT '传真',
	description varchar(256) COMMENT '描述',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (org_id)
) COMMENT = '机构';


CREATE TABLE sys_param
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	param_key varchar(32) COMMENT '参数键',
	param_value varchar(1000) COMMENT '参数值',
	param_label varchar(32) COMMENT '参数名称',
	enable boolean DEFAULT '1' COMMENT '是否启用',
	description varchar(256) COMMENT '描述',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (param_key)
) COMMENT = '参数';


CREATE TABLE sys_relate
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	object_id varchar(32) COMMENT '对象编号',
	target_id varchar(32) COMMENT '目标编号',
	relate_type varchar(32) COMMENT '关系类型',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id)
) COMMENT = '关联关系';


CREATE TABLE sys_role
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	role_id varchar(32) COMMENT '角色编号',
	role_name varchar(32) COMMENT '角色名称',
	enable boolean DEFAULT '1' COMMENT '是否启用',
	description varchar(256) COMMENT '描述',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (role_id)
) COMMENT = '角色';


CREATE TABLE sys_user
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	user_id varchar(32) COMMENT '用户编号',
	password varchar(32) COMMENT '密码',
	real_name varchar(32) COMMENT '真实姓名',
	gender char(1) COMMENT '性别',
	birthday date COMMENT '出生年月',
	org_id varchar(32) COMMENT '机构代码',
	org_name varchar(32) COMMENT '机构名称',
	area_id varchar(32) COMMENT '地区编码',
	area_name varchar(64) COMMENT '地区名称',
	phone varchar(16) COMMENT '手机',
	address varchar(256) COMMENT '地址',
	email varchar(128) COMMENT '电子邮箱',
	enable boolean DEFAULT '1' COMMENT '是否启用',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id),
	UNIQUE (user_id)
) COMMENT = '用户';


CREATE TABLE sys_vertical_domain
(
	id bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
	domain_id varchar(32) COMMENT '模型标识',
	row_id varchar(32) COMMENT '行标识',
	field_id varchar(32) COMMENT '字段标识',
	field_value varchar(1000) COMMENT '字段值',
	create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (id)
) COMMENT = '纵向数据模型';



/* Create Indexes */

CREATE INDEX row_id ON sys_attach (row_id ASC);
CREATE INDEX bucket ON sys_attach (bucket ASC);
CREATE INDEX domain_id ON sys_attach (domain_id ASC);
CREATE INDEX object_id ON sys_auth (object_id ASC);
CREATE INDEX user_id ON sys_log (user_id ASC);
CREATE INDEX record_time ON sys_log (record_time DESC);
CREATE INDEX sys_id ON sys_log (sys_id ASC);
CREATE INDEX object_relate ON sys_relate (object_id ASC, relate_type ASC);
CREATE INDEX domain_id ON sys_vertical_domain (domain_id ASC);
CREATE INDEX row_id ON sys_vertical_domain (row_id ASC);



