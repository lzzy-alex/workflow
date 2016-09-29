if object_id('wf_tpl_ctrl_dat', 'U') is not null  drop table wf_tpl_ctrl_dat;
if object_id('wf_biz_app', 'U') is not null  drop table wf_biz_app;
if object_id('wf_tpl_ctrl', 'U') is not null  drop table wf_tpl_ctrl;
if object_id('wf_step', 'U') is not null  drop table wf_step;
if object_id('wf_tpl', 'U') is not null  drop table wf_tpl;
if object_id('wf_biz', 'U') is not null  drop table wf_biz;

-- 业务
create table wf_biz (
	id int not null identity(1,1),
	code nvarchar(50) not null unique,
	name nvarchar(100) not null,
	primary key(id)
);

-- 模板
create table wf_tpl (
	id int not null identity(1,1),
	name nvarchar(100) not null unique,
	primary key(id)
);

-- 步骤
create table wf_step (
	id int not null identity(1,1),
	tpl_id int not null,
	biz_code nvarchar(50) not null,
	code nvarchar(50) not null unique,
	name nvarchar(100) not null,
	seq_no int,
	primary key(id),
	foreign key (tpl_id) references wf_tpl(id),
	foreign key (biz_code) references wf_biz(code)
);

-- 模板控件
create table wf_tpl_ctrl (
	id int not null identity(1,1) ,
	tpl_id int not null ,
	name nvarchar(100) ,
	type nvarchar(100) ,
	extra_values nvarchar(255) ,
	seq_no int ,
	style nvarchar(255) ,
	require bit ,
	regex nvarchar(40),
	access_role nvarchar(512) ,
	access_right nvarchar(512),
	primary key(id),
	foreign key (tpl_id) references wf_tpl(id)
);

-- 业务申报记录
create table wf_biz_app (
	id int not null identity(1,1) ,
	app_code nvarchar(50) not null unique,
	biz_code nvarchar(50) not null ,
	step_code nvarchar(50) not null,
	status int,
	primary key(id),
	foreign key (biz_code) references wf_biz(code),
	foreign key (step_code) references wf_step(code)
);

-- 模板控件数据
create table wf_tpl_ctrl_dat (
	id int not null identity(1,1) ,
	ctrl_id int not null ,
	app_code nvarchar(50) not null ,
	step_code nvarchar(50) not null ,
	dat nvarchar(1024) ,
	primary key(id),
	foreign key (ctrl_id) references wf_tpl_ctrl(id),
	foreign key (app_code) references wf_biz_app(app_code),
	foreign key (step_code) references wf_step(code)
);