-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
  dept_id           bigint(20)      not null auto_increment    comment '部门id',
  parent_id         bigint(20)      default 0                  comment '父部门id',
  ancestors         varchar(50)     default ''                 comment '祖级列表',
  dept_name         varchar(30)     default ''                 comment '部门名称',
  order_num         int(4)          default 0                  comment '显示顺序',
  leader            varchar(20)     default null               comment '负责人',
  phone             varchar(11)     default null               comment '联系电话',
  email             varchar(50)     default null               comment '邮箱',
  status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (dept_id)
) engine=innodb auto_increment=200 comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into sys_dept values(100,  0,   '0',          '若依科技',   0, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(101,  100, '0,100',      '深圳总公司', 1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(102,  100, '0,100',      '长沙分公司', 2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(103,  101, '0,100,101',  '研发部门',   1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(104,  101, '0,100,101',  '市场部门',   2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(105,  101, '0,100,101',  '测试部门',   3, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(106,  101, '0,100,101',  '财务部门',   4, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(107,  101, '0,100,101',  '运维部门',   5, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(108,  102, '0,100,102',  '市场部门',   1, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);
insert into sys_dept values(109,  102, '0,100,102',  '财务部门',   2, '若依', '15888888888', 'ry@qq.com', '0', '0', 'admin', sysdate(), '', null);


-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  user_id           bigint(20)      not null auto_increment    comment '用户ID',
  dept_id           bigint(20)      default null               comment '部门ID',
  login_name        varchar(30)     not null                   comment '登录账号',
  user_name         varchar(30)     default ''                 comment '用户昵称',
  user_type         varchar(2)      default '00'               comment '用户类型（00系统用户 01注册用户）',
  email             varchar(50)     default ''                 comment '用户邮箱',
  phonenumber       varchar(11)     default ''                 comment '手机号码',
  sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
  avatar            varchar(100)    default ''                 comment '头像路径',
  password          varchar(50)     default ''                 comment '密码',
  salt              varchar(20)     default ''                 comment '盐加密',
  status            char(1)         default '0'                comment '帐号状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  login_ip          varchar(128)    default ''                 comment '最后登录IP',
  login_date        datetime                                   comment '最后登录时间',
  pwd_update_date   datetime                                   comment '密码最后更新时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (user_id)
) engine=innodb auto_increment=100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user values(1,  103, 'admin', '管理员', '00', 'admin@163.com', '15888888888', '1', '', '29c67a30398638269fe600f73a054934', '111111', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '管理员');
-- insert into sys_user values(2,  105, 'ry',    '若依', '00', 'ry@qq.com',  '15666666666', '1', '', '8e6d98b90472783cc73c17047ddccf36', '222222', '0', '0', '127.0.0.1', sysdate(), sysdate(), 'admin', sysdate(), '', null, '测试员');


-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post
(
  post_id       bigint(20)      not null auto_increment    comment '岗位ID',
  post_code     varchar(64)     not null                   comment '岗位编码',
  post_name     varchar(50)     not null                   comment '岗位名称',
  post_sort     int(4)          not null                   comment '显示顺序',
  status        char(1)         not null                   comment '状态（0正常 1停用）',
  create_by     varchar(64)     default ''                 comment '创建者',
  create_time   datetime                                   comment '创建时间',
  update_by     varchar(64)     default ''			       comment '更新者',
  update_time   datetime                                   comment '更新时间',
  remark        varchar(500)    default null               comment '备注',
  primary key (post_id)
) engine=innodb comment = '岗位信息表';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into sys_post values(1, 'ceo',  '董事长',    1, '0', 'admin', sysdate(), '', null, '');
insert into sys_post values(2, 'se',   '项目经理',  2, '0', 'admin', sysdate(), '', null, '');
insert into sys_post values(3, 'hr',   '人力资源',  3, '0', 'admin', sysdate(), '', null, '');
insert into sys_post values(4, 'user', '普通员工',  4, '0', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  role_id           bigint(20)      not null auto_increment    comment '角色ID',
  role_name         varchar(30)     not null                   comment '角色名称',
  role_key          varchar(100)    not null                   comment '角色权限字符串',
  role_sort         int(4)          not null                   comment '显示顺序',
  data_scope        char(1)         default '1'                comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  status            char(1)         not null                   comment '角色状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role values('1', '超级管理员', 'admin',  1, 1, '0', '0', 'admin', sysdate(), '', null, '超级管理员');
insert into sys_role values('2', '普通角色',   'common', 2, 2, '0', '0', 'admin', sysdate(), '', null, '普通角色');


-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  menu_id           bigint(20)      not null auto_increment    comment '菜单ID',
  menu_name         varchar(50)     not null                   comment '菜单名称',
  parent_id         bigint(20)      default 0                  comment '父菜单ID',
  order_num         int(4)          default 0                  comment '显示顺序',
  url               varchar(200)    default '#'                comment '请求地址',
  target            varchar(20)     default ''                 comment '打开方式（menuItem页签 menuBlank新窗口）',
  menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
  visible           char(1)         default 0                  comment '菜单状态（0显示 1隐藏）',
  is_refresh        char(1)         default 1                  comment '是否刷新（0刷新 1不刷新）',
  perms             varchar(100)    default null               comment '权限标识',
  icon              varchar(100)    default '#'                comment '菜单图标',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu values('1', '系统管理', '0', '5', '#',                '',          'M', '0', '1', '', 'fa fa-gear',           'admin', sysdate(), '', null, '系统管理目录');
insert into sys_menu values('2', '系统监控', '0', '6', '#',                '',          'M', '0', '1', '', 'fa fa-video-camera',   'admin', sysdate(), '', null, '系统监控目录');
insert into sys_menu values('3', '系统工具', '0', '7', '#',                '',          'M', '0', '1', '', 'fa fa-bars',           'admin', sysdate(), '', null, '系统工具目录');
-- insert into sys_menu values('4', '若依官网', '0', '4', 'http://ruoyi.vip', 'menuBlank', 'C', '0', '1', '', 'fa fa-location-arrow', 'admin', sysdate(), '', null, '若依官网地址');
-- 二级菜单
insert into sys_menu values('100',  '用户管理', '1', '1', '/system/user',          '', 'C', '0', '1', 'system:user:view',         'fa fa-user-o',          'admin', sysdate(), '', null, '用户管理菜单');
insert into sys_menu values('101',  '角色管理', '1', '2', '/system/role',          '', 'C', '0', '1', 'system:role:view',         'fa fa-user-secret',     'admin', sysdate(), '', null, '角色管理菜单');
insert into sys_menu values('102',  '菜单管理', '1', '3', '/system/menu',          '', 'C', '0', '1', 'system:menu:view',         'fa fa-th-list',         'admin', sysdate(), '', null, '菜单管理菜单');
insert into sys_menu values('103',  '部门管理', '1', '4', '/system/dept',          '', 'C', '0', '1', 'system:dept:view',         'fa fa-outdent',         'admin', sysdate(), '', null, '部门管理菜单');
insert into sys_menu values('104',  '岗位管理', '1', '5', '/system/post',          '', 'C', '0', '1', 'system:post:view',         'fa fa-address-card-o',  'admin', sysdate(), '', null, '岗位管理菜单');
insert into sys_menu values('105',  '字典管理', '1', '6', '/system/dict',          '', 'C', '0', '1', 'system:dict:view',         'fa fa-bookmark-o',      'admin', sysdate(), '', null, '字典管理菜单');
insert into sys_menu values('106',  '参数设置', '1', '7', '/system/config',        '', 'C', '0', '1', 'system:config:view',       'fa fa-sun-o',           'admin', sysdate(), '', null, '参数设置菜单');
insert into sys_menu values('107',  '通知公告', '1', '8', '/system/notice',        '', 'C', '0', '1', 'system:notice:view',       'fa fa-bullhorn',        'admin', sysdate(), '', null, '通知公告菜单');
insert into sys_menu values('108',  '日志管理', '1', '9', '#',                     '', 'M', '0', '1', '',                         'fa fa-pencil-square-o', 'admin', sysdate(), '', null, '日志管理菜单');
insert into sys_menu values('109',  '在线用户', '2', '1', '/monitor/online',       '', 'C', '0', '1', 'monitor:online:view',      'fa fa-user-circle',     'admin', sysdate(), '', null, '在线用户菜单');
insert into sys_menu values('110',  '定时任务', '2', '2', '/monitor/job',          '', 'C', '0', '1', 'monitor:job:view',         'fa fa-tasks',           'admin', sysdate(), '', null, '定时任务菜单');
insert into sys_menu values('111',  '数据监控', '2', '3', '/monitor/data',         '', 'C', '0', '1', 'monitor:data:view',        'fa fa-bug',             'admin', sysdate(), '', null, '数据监控菜单');
insert into sys_menu values('112',  '服务监控', '2', '4', '/monitor/server',       '', 'C', '0', '1', 'monitor:server:view',      'fa fa-server',          'admin', sysdate(), '', null, '服务监控菜单');
insert into sys_menu values('113',  '缓存监控', '2', '5', '/monitor/cache',        '', 'C', '0', '1', 'monitor:cache:view',       'fa fa-cube',            'admin', sysdate(), '', null, '缓存监控菜单');
insert into sys_menu values('114',  '表单构建', '3', '1', '/tool/build',           '', 'C', '0', '1', 'tool:build:view',          'fa fa-wpforms',         'admin', sysdate(), '', null, '表单构建菜单');
insert into sys_menu values('115',  '代码生成', '3', '2', '/tool/gen',             '', 'C', '0', '1', 'tool:gen:view',            'fa fa-code',            'admin', sysdate(), '', null, '代码生成菜单');
insert into sys_menu values('116',  '系统接口', '3', '3', '/tool/swagger',         '', 'C', '0', '1', 'tool:swagger:view',        'fa fa-gg',              'admin', sysdate(), '', null, '系统接口菜单');
-- 三级菜单
insert into sys_menu values('500',  '操作日志', '108', '1', '/monitor/operlog',    '', 'C', '0', '1', 'monitor:operlog:view',     'fa fa-address-book',    'admin', sysdate(), '', null, '操作日志菜单');
insert into sys_menu values('501',  '登录日志', '108', '2', '/monitor/logininfor', '', 'C', '0', '1', 'monitor:logininfor:view',  'fa fa-file-image-o',    'admin', sysdate(), '', null, '登录日志菜单');
-- 用户管理按钮
insert into sys_menu values('1000', '用户查询', '100', '1',  '#', '',  'F', '0', '1', 'system:user:list',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1001', '用户新增', '100', '2',  '#', '',  'F', '0', '1', 'system:user:add',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1002', '用户修改', '100', '3',  '#', '',  'F', '0', '1', 'system:user:edit',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1003', '用户删除', '100', '4',  '#', '',  'F', '0', '1', 'system:user:remove',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1004', '用户导出', '100', '5',  '#', '',  'F', '0', '1', 'system:user:export',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1005', '用户导入', '100', '6',  '#', '',  'F', '0', '1', 'system:user:import',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1006', '重置密码', '100', '7',  '#', '',  'F', '0', '1', 'system:user:resetPwd',    '#', 'admin', sysdate(), '', null, '');
-- 角色管理按钮
insert into sys_menu values('1007', '角色查询', '101', '1',  '#', '',  'F', '0', '1', 'system:role:list',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1008', '角色新增', '101', '2',  '#', '',  'F', '0', '1', 'system:role:add',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1009', '角色修改', '101', '3',  '#', '',  'F', '0', '1', 'system:role:edit',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1010', '角色删除', '101', '4',  '#', '',  'F', '0', '1', 'system:role:remove',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1011', '角色导出', '101', '5',  '#', '',  'F', '0', '1', 'system:role:export',      '#', 'admin', sysdate(), '', null, '');
-- 菜单管理按钮
insert into sys_menu values('1012', '菜单查询', '102', '1',  '#', '',  'F', '0', '1', 'system:menu:list',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1013', '菜单新增', '102', '2',  '#', '',  'F', '0', '1', 'system:menu:add',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1014', '菜单修改', '102', '3',  '#', '',  'F', '0', '1', 'system:menu:edit',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1015', '菜单删除', '102', '4',  '#', '',  'F', '0', '1', 'system:menu:remove',      '#', 'admin', sysdate(), '', null, '');
-- 部门管理按钮
insert into sys_menu values('1016', '部门查询', '103', '1',  '#', '',  'F', '0', '1', 'system:dept:list',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1017', '部门新增', '103', '2',  '#', '',  'F', '0', '1', 'system:dept:add',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1018', '部门修改', '103', '3',  '#', '',  'F', '0', '1', 'system:dept:edit',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1019', '部门删除', '103', '4',  '#', '',  'F', '0', '1', 'system:dept:remove',      '#', 'admin', sysdate(), '', null, '');
-- 岗位管理按钮
insert into sys_menu values('1020', '岗位查询', '104', '1',  '#', '',  'F', '0', '1', 'system:post:list',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1021', '岗位新增', '104', '2',  '#', '',  'F', '0', '1', 'system:post:add',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1022', '岗位修改', '104', '3',  '#', '',  'F', '0', '1', 'system:post:edit',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1023', '岗位删除', '104', '4',  '#', '',  'F', '0', '1', 'system:post:remove',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1024', '岗位导出', '104', '5',  '#', '',  'F', '0', '1', 'system:post:export',      '#', 'admin', sysdate(), '', null, '');
-- 字典管理按钮
insert into sys_menu values('1025', '字典查询', '105', '1',  '#', '',  'F', '0', '1', 'system:dict:list',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1026', '字典新增', '105', '2',  '#', '',  'F', '0', '1', 'system:dict:add',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1027', '字典修改', '105', '3',  '#', '',  'F', '0', '1', 'system:dict:edit',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1028', '字典删除', '105', '4',  '#', '',  'F', '0', '1', 'system:dict:remove',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1029', '字典导出', '105', '5',  '#', '',  'F', '0', '1', 'system:dict:export',      '#', 'admin', sysdate(), '', null, '');
-- 参数设置按钮
insert into sys_menu values('1030', '参数查询', '106', '1',  '#', '',  'F', '0', '1', 'system:config:list',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1031', '参数新增', '106', '2',  '#', '',  'F', '0', '1', 'system:config:add',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1032', '参数修改', '106', '3',  '#', '',  'F', '0', '1', 'system:config:edit',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1033', '参数删除', '106', '4',  '#', '',  'F', '0', '1', 'system:config:remove',    '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1034', '参数导出', '106', '5',  '#', '',  'F', '0', '1', 'system:config:export',    '#', 'admin', sysdate(), '', null, '');
-- 通知公告按钮
insert into sys_menu values('1035', '公告查询', '107', '1',  '#', '',  'F', '0', '1', 'system:notice:list',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1036', '公告新增', '107', '2',  '#', '',  'F', '0', '1', 'system:notice:add',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1037', '公告修改', '107', '3',  '#', '',  'F', '0', '1', 'system:notice:edit',      '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1038', '公告删除', '107', '4',  '#', '',  'F', '0', '1', 'system:notice:remove',    '#', 'admin', sysdate(), '', null, '');
-- 操作日志按钮
insert into sys_menu values('1039', '操作查询', '500', '1',  '#', '',  'F', '0', '1', 'monitor:operlog:list',    '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1040', '操作删除', '500', '2',  '#', '',  'F', '0', '1', 'monitor:operlog:remove',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1041', '详细信息', '500', '3',  '#', '',  'F', '0', '1', 'monitor:operlog:detail',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1042', '日志导出', '500', '4',  '#', '',  'F', '0', '1', 'monitor:operlog:export',  '#', 'admin', sysdate(), '', null, '');
-- 登录日志按钮
insert into sys_menu values('1043', '登录查询', '501', '1',  '#', '',  'F', '0', '1', 'monitor:logininfor:list',         '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1044', '登录删除', '501', '2',  '#', '',  'F', '0', '1', 'monitor:logininfor:remove',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1045', '日志导出', '501', '3',  '#', '',  'F', '0', '1', 'monitor:logininfor:export',       '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1046', '账户解锁', '501', '4',  '#', '',  'F', '0', '1', 'monitor:logininfor:unlock',       '#', 'admin', sysdate(), '', null, '');
-- 在线用户按钮
insert into sys_menu values('1047', '在线查询', '109', '1',  '#', '',  'F', '0', '1', 'monitor:online:list',             '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1048', '批量强退', '109', '2',  '#', '',  'F', '0', '1', 'monitor:online:batchForceLogout', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1049', '单条强退', '109', '3',  '#', '',  'F', '0', '1', 'monitor:online:forceLogout',      '#', 'admin', sysdate(), '', null, '');
-- 定时任务按钮
insert into sys_menu values('1050', '任务查询', '110', '1',  '#', '',  'F', '0', '1', 'monitor:job:list',                '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1051', '任务新增', '110', '2',  '#', '',  'F', '0', '1', 'monitor:job:add',                 '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1052', '任务修改', '110', '3',  '#', '',  'F', '0', '1', 'monitor:job:edit',                '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1053', '任务删除', '110', '4',  '#', '',  'F', '0', '1', 'monitor:job:remove',              '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1054', '状态修改', '110', '5',  '#', '',  'F', '0', '1', 'monitor:job:changeStatus',        '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1055', '任务详细', '110', '6',  '#', '',  'F', '0', '1', 'monitor:job:detail',              '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1056', '任务导出', '110', '7',  '#', '',  'F', '0', '1', 'monitor:job:export',              '#', 'admin', sysdate(), '', null, '');
-- 代码生成按钮
insert into sys_menu values('1057', '生成查询', '115', '1',  '#', '',  'F', '0', '1', 'tool:gen:list',     '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1058', '生成修改', '115', '2',  '#', '',  'F', '0', '1', 'tool:gen:edit',     '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1059', '生成删除', '115', '3',  '#', '',  'F', '0', '1', 'tool:gen:remove',   '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1060', '预览代码', '115', '4',  '#', '',  'F', '0', '1', 'tool:gen:preview',  '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('1061', '生成代码', '115', '5',  '#', '',  'F', '0', '1', 'tool:gen:code',     '#', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
  user_id   bigint(20) not null comment '用户ID',
  role_id   bigint(20) not null comment '角色ID',
  primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values ('1', '1');
insert into sys_user_role values ('2', '2');


-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
  role_id   bigint(20) not null comment '角色ID',
  menu_id   bigint(20) not null comment '菜单ID',
  primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
insert into sys_role_menu values ('2', '1');
insert into sys_role_menu values ('2', '2');
insert into sys_role_menu values ('2', '3');
insert into sys_role_menu values ('2', '4');
insert into sys_role_menu values ('2', '100');
insert into sys_role_menu values ('2', '101');
insert into sys_role_menu values ('2', '102');
insert into sys_role_menu values ('2', '103');
insert into sys_role_menu values ('2', '104');
insert into sys_role_menu values ('2', '105');
insert into sys_role_menu values ('2', '106');
insert into sys_role_menu values ('2', '107');
insert into sys_role_menu values ('2', '108');
insert into sys_role_menu values ('2', '109');
insert into sys_role_menu values ('2', '110');
insert into sys_role_menu values ('2', '111');
insert into sys_role_menu values ('2', '112');
insert into sys_role_menu values ('2', '113');
insert into sys_role_menu values ('2', '114');
insert into sys_role_menu values ('2', '115');
insert into sys_role_menu values ('2', '116');
insert into sys_role_menu values ('2', '500');
insert into sys_role_menu values ('2', '501');
insert into sys_role_menu values ('2', '1000');
insert into sys_role_menu values ('2', '1001');
insert into sys_role_menu values ('2', '1002');
insert into sys_role_menu values ('2', '1003');
insert into sys_role_menu values ('2', '1004');
insert into sys_role_menu values ('2', '1005');
insert into sys_role_menu values ('2', '1006');
insert into sys_role_menu values ('2', '1007');
insert into sys_role_menu values ('2', '1008');
insert into sys_role_menu values ('2', '1009');
insert into sys_role_menu values ('2', '1010');
insert into sys_role_menu values ('2', '1011');
insert into sys_role_menu values ('2', '1012');
insert into sys_role_menu values ('2', '1013');
insert into sys_role_menu values ('2', '1014');
insert into sys_role_menu values ('2', '1015');
insert into sys_role_menu values ('2', '1016');
insert into sys_role_menu values ('2', '1017');
insert into sys_role_menu values ('2', '1018');
insert into sys_role_menu values ('2', '1019');
insert into sys_role_menu values ('2', '1020');
insert into sys_role_menu values ('2', '1021');
insert into sys_role_menu values ('2', '1022');
insert into sys_role_menu values ('2', '1023');
insert into sys_role_menu values ('2', '1024');
insert into sys_role_menu values ('2', '1025');
insert into sys_role_menu values ('2', '1026');
insert into sys_role_menu values ('2', '1027');
insert into sys_role_menu values ('2', '1028');
insert into sys_role_menu values ('2', '1029');
insert into sys_role_menu values ('2', '1030');
insert into sys_role_menu values ('2', '1031');
insert into sys_role_menu values ('2', '1032');
insert into sys_role_menu values ('2', '1033');
insert into sys_role_menu values ('2', '1034');
insert into sys_role_menu values ('2', '1035');
insert into sys_role_menu values ('2', '1036');
insert into sys_role_menu values ('2', '1037');
insert into sys_role_menu values ('2', '1038');
insert into sys_role_menu values ('2', '1039');
insert into sys_role_menu values ('2', '1040');
insert into sys_role_menu values ('2', '1041');
insert into sys_role_menu values ('2', '1042');
insert into sys_role_menu values ('2', '1043');
insert into sys_role_menu values ('2', '1044');
insert into sys_role_menu values ('2', '1045');
insert into sys_role_menu values ('2', '1046');
insert into sys_role_menu values ('2', '1047');
insert into sys_role_menu values ('2', '1048');
insert into sys_role_menu values ('2', '1049');
insert into sys_role_menu values ('2', '1050');
insert into sys_role_menu values ('2', '1051');
insert into sys_role_menu values ('2', '1052');
insert into sys_role_menu values ('2', '1053');
insert into sys_role_menu values ('2', '1054');
insert into sys_role_menu values ('2', '1055');
insert into sys_role_menu values ('2', '1056');
insert into sys_role_menu values ('2', '1057');
insert into sys_role_menu values ('2', '1058');
insert into sys_role_menu values ('2', '1059');
insert into sys_role_menu values ('2', '1060');
insert into sys_role_menu values ('2', '1061');

-- ----------------------------
-- 8、角色和部门关联表  角色1-N部门
-- ----------------------------
drop table if exists sys_role_dept;
create table sys_role_dept (
  role_id   bigint(20) not null comment '角色ID',
  dept_id   bigint(20) not null comment '部门ID',
  primary key(role_id, dept_id)
) engine=innodb comment = '角色和部门关联表';

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------
insert into sys_role_dept values ('2', '100');
insert into sys_role_dept values ('2', '101');
insert into sys_role_dept values ('2', '105');

-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
drop table if exists sys_user_post;
create table sys_user_post
(
  user_id   bigint(20) not null comment '用户ID',
  post_id   bigint(20) not null comment '岗位ID',
  primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------
insert into sys_user_post values ('1', '1');
insert into sys_user_post values ('2', '2');


-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log (
  oper_id           bigint(20)      not null auto_increment    comment '日志主键',
  title             varchar(50)     default ''                 comment '模块标题',
  business_type     int(2)          default 0                  comment '业务类型（0其它 1新增 2修改 3删除）',
  method            varchar(100)    default ''                 comment '方法名称',
  request_method    varchar(10)     default ''                 comment '请求方式',
  operator_type     int(1)          default 0                  comment '操作类别（0其它 1后台用户 2手机端用户）',
  oper_name         varchar(50)     default ''                 comment '操作人员',
  dept_name         varchar(50)     default ''                 comment '部门名称',
  oper_url          varchar(255)    default ''                 comment '请求URL',
  oper_ip           varchar(128)    default ''                 comment '主机地址',
  oper_location     varchar(255)    default ''                 comment '操作地点',
  oper_param        varchar(2000)   default ''                 comment '请求参数',
  json_result       varchar(2000)   default ''                 comment '返回参数',
  status            int(1)          default 0                  comment '操作状态（0正常 1异常）',
  error_msg         varchar(2000)   default ''                 comment '错误消息',
  oper_time         datetime                                   comment '操作时间',
  cost_time         bigint(20)      default 0                  comment '消耗时间',
  primary key (oper_id),
  key idx_sys_oper_log_bt (business_type),
  key idx_sys_oper_log_s  (status),
  key idx_sys_oper_log_ot (oper_time)
) engine=innodb auto_increment=100 comment = '操作日志记录';


-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
  dict_id          bigint(20)      not null auto_increment    comment '字典主键',
  dict_name        varchar(100)    default ''                 comment '字典名称',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_id),
  unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

insert into sys_dict_type values(1,  '用户性别', 'sys_user_sex',        '0', 'admin', sysdate(), '', null, '用户性别列表');
insert into sys_dict_type values(2,  '菜单状态', 'sys_show_hide',       '0', 'admin', sysdate(), '', null, '菜单状态列表');
insert into sys_dict_type values(3,  '系统开关', 'sys_normal_disable',  '0', 'admin', sysdate(), '', null, '系统开关列表');
insert into sys_dict_type values(4,  '任务状态', 'sys_job_status',      '0', 'admin', sysdate(), '', null, '任务状态列表');
insert into sys_dict_type values(5,  '任务分组', 'sys_job_group',       '0', 'admin', sysdate(), '', null, '任务分组列表');
insert into sys_dict_type values(6,  '系统是否', 'sys_yes_no',          '0', 'admin', sysdate(), '', null, '系统是否列表');
insert into sys_dict_type values(7,  '通知类型', 'sys_notice_type',     '0', 'admin', sysdate(), '', null, '通知类型列表');
insert into sys_dict_type values(8,  '通知状态', 'sys_notice_status',   '0', 'admin', sysdate(), '', null, '通知状态列表');
insert into sys_dict_type values(9,  '操作类型', 'sys_oper_type',       '0', 'admin', sysdate(), '', null, '操作类型列表');
insert into sys_dict_type values(10, '系统状态', 'sys_common_status',   '0', 'admin', sysdate(), '', null, '登录状态列表');


-- ----------------------------
-- 12、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
  dict_code        bigint(20)      not null auto_increment    comment '字典编码',
  dict_sort        int(4)          default 0                  comment '字典排序',
  dict_label       varchar(100)    default ''                 comment '字典标签',
  dict_value       varchar(100)    default ''                 comment '字典键值',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  css_class        varchar(100)    default null               comment '样式属性（其他样式扩展）',
  list_class       varchar(100)    default null               comment '表格回显样式',
  is_default       char(1)         default 'N'                comment '是否默认（Y是 N否）',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_code)
) engine=innodb auto_increment=100 comment = '字典数据表';

insert into sys_dict_data values(1,  1,  '男',       '0',       'sys_user_sex',        '',   '',        'Y', '0', 'admin', sysdate(), '', null, '性别男');
insert into sys_dict_data values(2,  2,  '女',       '1',       'sys_user_sex',        '',   '',        'N', '0', 'admin', sysdate(), '', null, '性别女');
insert into sys_dict_data values(3,  3,  '未知',     '2',       'sys_user_sex',        '',   '',        'N', '0', 'admin', sysdate(), '', null, '性别未知');
insert into sys_dict_data values(4,  1,  '显示',     '0',       'sys_show_hide',       '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '显示菜单');
insert into sys_dict_data values(5,  2,  '隐藏',     '1',       'sys_show_hide',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '隐藏菜单');
insert into sys_dict_data values(6,  1,  '正常',     '0',       'sys_normal_disable',  '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(7,  2,  '停用',     '1',       'sys_normal_disable',  '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into sys_dict_data values(8,  1,  '正常',     '0',       'sys_job_status',      '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(9,  2,  '暂停',     '1',       'sys_job_status',      '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');
insert into sys_dict_data values(10, 1,  '默认',     'DEFAULT', 'sys_job_group',       '',   '',        'Y', '0', 'admin', sysdate(), '', null, '默认分组');
insert into sys_dict_data values(11, 2,  '系统',     'SYSTEM',  'sys_job_group',       '',   '',        'N', '0', 'admin', sysdate(), '', null, '系统分组');
insert into sys_dict_data values(12, 1,  '是',       'Y',       'sys_yes_no',          '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '系统默认是');
insert into sys_dict_data values(13, 2,  '否',       'N',       'sys_yes_no',          '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '系统默认否');
insert into sys_dict_data values(14, 1,  '通知',     '1',       'sys_notice_type',     '',   'warning', 'Y', '0', 'admin', sysdate(), '', null, '通知');
insert into sys_dict_data values(15, 2,  '公告',     '2',       'sys_notice_type',     '',   'success', 'N', '0', 'admin', sysdate(), '', null, '公告');
insert into sys_dict_data values(16, 1,  '正常',     '0',       'sys_notice_status',   '',   'primary', 'Y', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(17, 2,  '关闭',     '1',       'sys_notice_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '关闭状态');
insert into sys_dict_data values(18, 99, '其他',     '0',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '其他操作');
insert into sys_dict_data values(19, 1,  '新增',     '1',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '新增操作');
insert into sys_dict_data values(20, 2,  '修改',     '2',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', sysdate(), '', null, '修改操作');
insert into sys_dict_data values(21, 3,  '删除',     '3',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '删除操作');
insert into sys_dict_data values(22, 4,  '授权',     '4',       'sys_oper_type',       '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '授权操作');
insert into sys_dict_data values(23, 5,  '导出',     '5',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '导出操作');
insert into sys_dict_data values(24, 6,  '导入',     '6',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '导入操作');
insert into sys_dict_data values(25, 7,  '强退',     '7',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '强退操作');
insert into sys_dict_data values(26, 8,  '生成代码', '8',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', sysdate(), '', null, '生成操作');
insert into sys_dict_data values(27, 9,  '清空数据', '9',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '清空操作');
insert into sys_dict_data values(28, 1,  '成功',     '0',       'sys_common_status',   '',   'primary', 'N', '0', 'admin', sysdate(), '', null, '正常状态');
insert into sys_dict_data values(29, 2,  '失败',     '1',       'sys_common_status',   '',   'danger',  'N', '0', 'admin', sysdate(), '', null, '停用状态');


-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  config_id         int(5)          not null auto_increment    comment '参数主键',
  config_name       varchar(100)    default ''                 comment '参数名称',
  config_key        varchar(100)    default ''                 comment '参数键名',
  config_value      varchar(2048)    default ''                 comment '参数键值',
  config_type       char(1)         default 'N'                comment '系统内置（Y是 N否）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

insert into sys_config values(1,  '主框架页-默认皮肤样式名称',     'sys.index.skinName',               'skin-blue',     'Y', 'admin', sysdate(), '', null, '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
insert into sys_config values(2,  '用户管理-账号初始密码',         'sys.user.initPassword',            '123456',        'Y', 'admin', sysdate(), '', null, '初始化密码 123456');
insert into sys_config values(3,  '主框架页-侧边栏主题',           'sys.index.sideTheme',              'theme-dark',    'Y', 'admin', sysdate(), '', null, '深黑主题theme-dark，浅色主题theme-light，深蓝主题theme-blue');
insert into sys_config values(4,  '账号自助-是否开启用户注册功能', 'sys.account.registerUser',         'false',         'Y', 'admin', sysdate(), '', null, '是否开启注册用户功能（true开启，false关闭）');
insert into sys_config values(5,  '用户管理-密码字符范围',         'sys.account.chrtype',              '0',             'Y', 'admin', sysdate(), '', null, '默认任意字符范围，0任意（密码可以输入任意字符），1数字（密码只能为0-9数字），2英文字母（密码只能为a-z和A-Z字母），3字母和数字（密码必须包含字母，数字）,4字母数字和特殊字符（目前支持的特殊字符包括：~!@#$%^&*()-=_+）');
insert into sys_config values(6,  '用户管理-初始密码修改策略',     'sys.account.initPasswordModify',   '0',             'Y', 'admin', sysdate(), '', null, '0：初始密码修改策略关闭，没有任何提示，1：提醒用户，如果未修改初始密码，则在登录时就会提醒修改密码对话框');
insert into sys_config values(7,  '用户管理-账号密码更新周期',     'sys.account.passwordValidateDays', '0',             'Y', 'admin', sysdate(), '', null, '密码更新周期（填写数字，数据初始化值为0不限制，若修改必须为大于0小于365的正整数），如果超过这个周期登录系统时，则在登录时就会提醒修改密码对话框');
insert into sys_config values(8,  '主框架页-菜单导航显示风格',     'sys.index.menuStyle',              'default',       'Y', 'admin', sysdate(), '', null, '菜单导航显示风格（default为左侧导航菜单，topnav为顶部导航菜单）');
insert into sys_config values(9,  '主框架页-是否开启页脚',         'sys.index.footer',                 'true',          'Y', 'admin', sysdate(), '', null, '是否开启底部页脚显示（true显示，false隐藏）');
insert into sys_config values(10, '主框架页-是否开启页签',         'sys.index.tagsView',               'true',          'Y', 'admin', sysdate(), '', null, '是否开启菜单多页签显示（true显示，false隐藏）');
insert into sys_config values(11, '用户登录-黑名单列表',           'sys.login.blackIPList',            '',              'Y', 'admin', sysdate(), '', null, '设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）');


-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor (
  info_id        bigint(20)     not null auto_increment   comment '访问ID',
  login_name     varchar(50)    default ''                comment '登录账号',
  ipaddr         varchar(128)   default ''                comment '登录IP地址',
  login_location varchar(255)   default ''                comment '登录地点',
  browser        varchar(50)    default ''                comment '浏览器类型',
  os             varchar(50)    default ''                comment '操作系统',
  status         char(1)        default '0'               comment '登录状态（0成功 1失败）',
  msg            varchar(255)   default ''                comment '提示消息',
  login_time     datetime                                 comment '访问时间',
  primary key (info_id),
  key idx_sys_logininfor_s  (status),
  key idx_sys_logininfor_lt (login_time)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 15、在线用户记录
-- ----------------------------
drop table if exists sys_user_online;
create table sys_user_online (
  sessionId         varchar(50)   default ''                comment '用户会话id',
  login_name        varchar(50)   default ''                comment '登录账号',
  dept_name         varchar(50)   default ''                comment '部门名称',
  ipaddr            varchar(128)  default ''                comment '登录IP地址',
  login_location    varchar(255)  default ''                comment '登录地点',
  browser           varchar(50)   default ''                comment '浏览器类型',
  os                varchar(50)   default ''                comment '操作系统',
  status            varchar(10)   default ''                comment '在线状态on_line在线off_line离线',
  start_timestamp   datetime                                comment 'session创建时间',
  last_access_time  datetime                                comment 'session最后访问时间',
  expire_time       int(5)        default 0                 comment '超时时间，单位为分钟',
  primary key (sessionId)
) engine=innodb comment = '在线用户记录';


-- ----------------------------
-- 16、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  job_id              bigint(20)    not null auto_increment    comment '任务ID',
  job_name            varchar(64)   default ''                 comment '任务名称',
  job_group           varchar(64)   default 'DEFAULT'          comment '任务组名',
  invoke_target       varchar(500)  not null                   comment '调用目标字符串',
  cron_expression     varchar(255)  default ''                 comment 'cron执行表达式',
  misfire_policy      varchar(20)   default '3'                comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  concurrent          char(1)       default '1'                comment '是否并发执行（0允许 1禁止）',
  status              char(1)       default '0'                comment '状态（0正常 1暂停）',
  create_by           varchar(64)   default ''                 comment '创建者',
  create_time         datetime                                 comment '创建时间',
  update_by           varchar(64)   default ''                 comment '更新者',
  update_time         datetime                                 comment '更新时间',
  remark              varchar(500)  default ''                 comment '备注信息',
  primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

insert into sys_job values(1, '系统默认（无参）', 'DEFAULT', 'ryTask.ryNoParams',        '0/10 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
insert into sys_job values(2, '系统默认（有参）', 'DEFAULT', 'ryTask.ryParams(\'ry\')',  '0/15 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');
insert into sys_job values(3, '系统默认（多参）', 'DEFAULT', 'ryTask.ryMultipleParams(\'ry\', true, 2000L, 316.50D, 100)',  '0/20 * * * * ?', '3', '1', '1', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 17、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  job_log_id          bigint(20)     not null auto_increment    comment '任务日志ID',
  job_name            varchar(64)    not null                   comment '任务名称',
  job_group           varchar(64)    not null                   comment '任务组名',
  invoke_target       varchar(500)   not null                   comment '调用目标字符串',
  job_message         varchar(500)                              comment '日志信息',
  status              char(1)        default '0'                comment '执行状态（0正常 1失败）',
  exception_info      varchar(2000)  default ''                 comment '异常信息',
  create_time         datetime                                  comment '创建时间',
  primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';


-- ----------------------------
-- 18、通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice (
  notice_id         int(4)          not null auto_increment    comment '公告ID',
  notice_title      varchar(50)     not null                   comment '公告标题',
  notice_type       char(1)         not null                   comment '公告类型（1通知 2公告）',
  notice_content    varchar(2000)   default null               comment '公告内容',
  status            char(1)         default '0'                comment '公告状态（0正常 1关闭）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(255)    default null               comment '备注',
  primary key (notice_id)
) engine=innodb auto_increment=10 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------
insert into sys_notice values('1', '温馨提醒：2018-07-01 若依新版本发布啦', '2', '新版本内容', '0', 'admin', sysdate(), '', null, '管理员');
insert into sys_notice values('2', '维护通知：2018-07-01 若依系统凌晨维护', '1', '维护内容',   '0', 'admin', sysdate(), '', null, '管理员');


-- ----------------------------
-- 19、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  table_id             bigint(20)      not null auto_increment    comment '编号',
  table_name           varchar(200)    default ''                 comment '表名称',
  table_comment        varchar(500)    default ''                 comment '表描述',
  sub_table_name       varchar(64)     default null               comment '关联子表的表名',
  sub_table_fk_name    varchar(64)     default null               comment '子表关联的外键名',
  class_name           varchar(100)    default ''                 comment '实体类名称',
  tpl_category         varchar(200)    default 'crud'             comment '使用的模板（crud单表操作 tree树表操作 sub主子表操作）',
  package_name         varchar(100)                               comment '生成包路径',
  module_name          varchar(30)                                comment '生成模块名',
  business_name        varchar(30)                                comment '生成业务名',
  function_name        varchar(50)                                comment '生成功能名',
  function_author      varchar(50)                                comment '生成功能作者',
  gen_type             char(1)         default '0'                comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path             varchar(200)    default '/'                comment '生成路径（不填默认项目路径）',
  options              varchar(1000)                              comment '其它生成选项',
  create_by            varchar(64)     default ''                 comment '创建者',
  create_time 	       datetime                                   comment '创建时间',
  update_by            varchar(64)     default ''                 comment '更新者',
  update_time          datetime                                   comment '更新时间',
  remark               varchar(500)    default null               comment '备注',
  primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 20、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  column_id         bigint(20)      not null auto_increment    comment '编号',
  table_id          bigint(20)                                 comment '归属表编号',
  column_name       varchar(200)                               comment '列名称',
  column_comment    varchar(500)                               comment '列描述',
  column_type       varchar(100)                               comment '列类型',
  java_type         varchar(500)                               comment 'JAVA类型',
  java_field        varchar(200)                               comment 'JAVA字段名',
  is_pk             char(1)                                    comment '是否主键（1是）',
  is_increment      char(1)                                    comment '是否自增（1是）',
  is_required       char(1)                                    comment '是否必填（1是）',
  is_insert         char(1)                                    comment '是否为插入字段（1是）',
  is_edit           char(1)                                    comment '是否编辑字段（1是）',
  is_list           char(1)                                    comment '是否列表字段（1是）',
  is_query          char(1)                                    comment '是否查询字段（1是）',
  query_type        varchar(200)    default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type         varchar(200)                               comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type         varchar(200)    default ''                 comment '字典类型',
  sort              int                                        comment '排序',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';


INSERT INTO sys_dict_type (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (100, '配置类型', 'bot_config_type', '0', 'admin', '2023-11-19 23:44:00', '', NULL, NULL);
INSERT INTO sys_dict_type (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (101, '触发方式', 'bot_trigger_type', '0', 'admin', '2023-11-19 23:44:18', '', NULL, NULL);

INSERT INTO sys_dict_data (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (100, 1, '普通按钮', 'B', 'bot_config_type', NULL, 'info', 'N', '0', 'admin', '2023-11-19 23:44:43', '', NULL, NULL);
INSERT INTO sys_dict_data (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (101, 2, '菜单按钮', 'F', 'bot_config_type', NULL, 'info', 'N', '0', 'admin', '2023-11-19 23:45:01', '', NULL, NULL);
INSERT INTO sys_dict_data (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (102, 3, '文本', 'T', 'bot_config_type', NULL, 'info', 'N', '0', 'admin', '2023-11-19 23:45:18', '', NULL, NULL);
INSERT INTO sys_dict_data (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (103, 4, '图片', 'I', 'bot_config_type', NULL, 'info', 'N', '0', 'admin', '2023-11-19 23:45:34', '', NULL, NULL);
INSERT INTO sys_dict_data (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (104, 5, '视频', 'V', 'bot_config_type', NULL, 'info', 'N', '0', 'admin', '2023-11-19 23:45:55', '', NULL, NULL);
INSERT INTO sys_dict_data (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (105, 6, '自定义', 'D', 'bot_config_type', NULL, 'info', 'N', '0', 'admin', '2023-11-19 23:46:12', '', NULL, NULL);
INSERT INTO sys_dict_data (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (106, 1, '链接', 'URL', 'bot_trigger_type', NULL, 'success', 'N', '0', 'admin', '2023-11-19 23:46:39', '', NULL, NULL);
INSERT INTO sys_dict_data (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (107, 2, '图+文', 'CALLBACK_IMG', 'bot_trigger_type', NULL, 'success', 'Y', '0', 'admin', '2023-11-19 23:47:00', '', NULL, NULL);
INSERT INTO sys_dict_data (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (108, 3, '视+文', 'CALLBACK_VIDEO', 'bot_trigger_type', NULL, 'success', 'N', '0', 'admin', '2023-11-19 23:47:21', '', NULL, NULL);

INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (102, '支付状态', 'pay_status', '0', 'admin', '2023-11-25 00:56:08', '', NULL, NULL);
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (103, '下发状态', 'pay_out_status', '0', 'admin', '2023-11-25 00:56:31', '', NULL, NULL);

INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (109, 1, '待支付', '1', 'pay_status', NULL, 'warning', 'Y', '0', 'admin', '2023-11-25 00:57:09', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (110, 2, '成功', '0', 'pay_status', '', 'primary', 'Y', '0', 'admin', '2023-11-25 00:57:28', 'admin', '2023-11-25 00:57:44', '');
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (111, 1, '待下发', '1', 'pay_out_status', NULL, 'warning', 'Y', '0', 'admin', '2023-11-25 00:58:18', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (112, 2, '审核中', '2', 'pay_out_status', NULL, 'success', 'Y', '0', 'admin', '2023-11-25 00:58:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (113, 3, '下发成功', '3', 'pay_out_status', NULL, 'primary', 'Y', '0', 'admin', '2023-11-25 00:59:22', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (114, 4, '审核失败', '4', 'pay_out_status', NULL, 'danger', 'Y', '0', 'admin', '2023-11-25 01:00:02', '', NULL, NULL);
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (115, 3, '支付失败', '2', 'pay_status', NULL, 'danger', 'Y', '0', 'admin', '2023-11-25 21:54:16', '', NULL, NULL);
-- 注意：以下四项为运行时业务配置，此处仅为占位示例，部署后请在【系统管理 - 参数设置】中改为自己的真实值
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (100, '链上记录缓存数据', 'online.record', '💰 当前下发记录\n------------\n', 'Y', 'admin', '2023-11-24 00:37:30', '', '2023-11-24 00:48:14', '由定时任务从链上拉取后自动覆盖，无需手动维护');
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (101, '链上记录api地址', 'online.api.url', 'https://apilist.tronscanapi.com/api/new/filter/trc20/transfers?limit=20&start=0&sort=-timestamp&count=true&fromAddress=YOUR_TRC20_ADDRESS&filterTokenValue=0&relatedAddress=YOUR_TRC20_ADDRESS', 'Y', 'admin', '2023-11-24 03:15:41', 'admin', '2023-11-24 03:16:04', '请将 YOUR_TRC20_ADDRESS 替换为自己的 TRC20 收款地址');
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (102, 'api接口认证token', 'api.auth.token', 'CHANGE_ME_TO_A_RANDOM_TOKEN', 'Y', 'admin', '2023-11-26 01:34:19', '', NULL, '请自行生成高强度随机串，切勿沿用默认值');
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (103, '创建交易接口', 'create.order.api', 'https://your-payment-gateway.example.com/api/v1/order/create-transaction', 'Y', 'admin', '2023-11-26 01:38:59', '', NULL, '上游支付网关地址，需替换为自己对接的服务');
INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (104, '异步回调地址', 'notify.url', 'http://localhost:8088/index', 'Y', 'admin', '2023-11-26 01:42:04', '', NULL, NULL);

-- 一级菜单
insert into sys_menu values('4', '机器人管理', '0', '1', '#',                '',          'M', '0', '1', '', 'fa fa-paper-plane-o', 'admin', sysdate(), '', null, '机器人管理目录');
insert into sys_menu values('5', '客户管理', '0', '1', '#',                '',          'M', '0', '1', '', 'fa fa-users', 'admin', sysdate(), '', null, '客户管理目录');
insert into sys_menu values('6', '汇率管理', '0', '1', '#',                '',          'M', '0', '1', '', 'fa fa-balance-scale', 'admin', sysdate(), '', null, '汇率管理目录');
insert into sys_menu values('7', '支付管理', '0', '1', '#',                '',          'M', '0', '1', '', 'fa fa-money', 'admin', sysdate(), '', null, '支付管理目录');
insert into sys_menu values('8', '通知管理', '0', '1', '#',                '',          'M', '0', '1', '', 'fa fa-volume-up', 'admin', sysdate(), '', null, '通知管理目录');

-- 二级菜单
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人配置', '4', '2', '/bot/config', 'C', '0', 'bot:config:view', '#', 'admin', sysdate(), '', null, '机器人配置菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人配置查询', @parentId, '1',  '#',  'F', '0', 'bot:config:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人配置新增', @parentId, '2',  '#',  'F', '0', 'bot:config:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人配置修改', @parentId, '3',  '#',  'F', '0', 'bot:config:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人配置删除', @parentId, '4',  '#',  'F', '0', 'bot:config:remove',       '#', 'admin', sysdate(), '', null, '');

-- insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
-- values('机器人配置导出', @parentId, '5',  '#',  'F', '0', 'bot:config:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人列表', '4', '1', '/bot/list', 'C', '0', 'bot:list:view', '#', 'admin', sysdate(), '', null, '机器人列表菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人列表查询', @parentId, '1',  '#',  'F', '0', 'bot:list:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人列表新增', @parentId, '2',  '#',  'F', '0', 'bot:list:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人列表修改', @parentId, '3',  '#',  'F', '0', 'bot:list:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人列表删除', @parentId, '4',  '#',  'F', '0', 'bot:list:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('机器人列表导出', @parentId, '5',  '#',  'F', '0', 'bot:list:export',       '#', 'admin', sysdate(), '', null, '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户列表', '5', '1', '/bot/user', 'C', '0', 'bot:user:view', '#', 'admin', sysdate(), '', null, '客户列表菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户列表查询', @parentId, '1',  '#',  'F', '0', 'bot:user:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户列表删除', @parentId, '4',  '#',  'F', '0', 'bot:user:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户列表导出', @parentId, '5',  '#',  'F', '0', 'bot:user:export',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户列表修改', @parentId, '2',  '#',  'F', '0', 'bot:user:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('客户列表新增', @parentId, '3',  '#',  'F', '0', 'bot:user:add',         '#', 'admin', sysdate(), '', null, '');

select * from sys_menu where parent_id = '2011';

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('汇率管理', '6', '1', '/bot/rate', 'C', '0', 'bot:rate:view', '#', 'admin', sysdate(), '', null, '汇率管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('汇率管理查询', @parentId, '1',  '#',  'F', '0', 'bot:rate:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('汇率管理新增', @parentId, '2',  '#',  'F', '0', 'bot:rate:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('汇率管理修改', @parentId, '3',  '#',  'F', '0', 'bot:rate:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('汇率管理删除', @parentId, '4',  '#',  'F', '0', 'bot:rate:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('汇率管理导出', @parentId, '5',  '#',  'F', '0', 'bot:rate:export',       '#', 'admin', sysdate(), '', null, '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('支付记录', '7', '1', '/bot/pay', 'C', '0', 'bot:pay:view', '#', 'admin', sysdate(), '', null, '支付记录菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('支付记录查询', @parentId, '1',  '#',  'F', '0', 'bot:pay:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('支付记录导出', @parentId, '5',  '#',  'F', '0', 'bot:pay:export',       '#', 'admin', sysdate(), '', null, '');

-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('下发记录', '7', '1', '/bot/payout', 'C', '0', 'bot:payout:view', '#', 'admin', sysdate(), '', null, '下发记录菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('下发记录查询', @parentId, '1',  '#',  'F', '0', 'bot:payout:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('下发记录导出', @parentId, '5',  '#',  'F', '0', 'bot:payout:export',       '#', 'admin', sysdate(), '', null, '');


-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('通知管理', '8', '1', '/bot/notify', 'C', '0', 'bot:notify:view', '#', 'admin', sysdate(), '', null, '通知管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('通知管理查询', @parentId, '1',  '#',  'F', '0', 'bot:notify:list',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('通知管理新增', @parentId, '2',  '#',  'F', '0', 'bot:notify:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('通知管理删除', @parentId, '4',  '#',  'F', '0', 'bot:notify:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, url, menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('通知管理导出', @parentId, '5',  '#',  'F', '0', 'bot:notify:export',       '#', 'admin', sysdate(), '', null, '');


-- ----------------------------
-- 21、机器人列表
-- ----------------------------
drop table if exists bot_list;
create table bot_list (
                            bot_id            int(4)          not null auto_increment    comment '机器人ID',
                            bot_user_name     varchar(50)     not null                   comment '机器人用户名',
                            bot_token         varchar(100)    not null                   comment '机器人TOKEN',
                            status            char(1)         default '1'                comment '机器人状态（0正常 1关闭）',
                            create_by         varchar(64)     default ''                 comment '创建者',
                            create_time       datetime                                   comment '创建时间',
                            update_by         varchar(64)     default ''                 comment '更新者',
                            update_time       datetime                                   comment '更新时间',
                            remark            varchar(255)    default null               comment '备注',
                            primary key (bot_id),
                            unique (bot_token)
) engine=innodb auto_increment=10 comment = '机器人列表';

-- 机器人需自行在 Telegram @BotFather 申请后，登录后台在【机器人管理 - 机器人列表】中录入，此处不预置任何 Token
-- 示例（请勿直接执行，Token 需替换为自己的）：
-- INSERT INTO `bot_list` (`bot_id`, `bot_user_name`, `bot_token`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (10, 'yourBotName', 'YOUR_BOT_TOKEN_FROM_BOTFATHER', '0', '', '2023-11-21 23:18:14', 'admin', '2023-11-22 00:42:58', '');


-- ----------------------------
-- 22、机器人配置
-- ----------------------------
drop table if exists bot_config;
create table bot_config (
                          config_id         bigint(20)      not null auto_increment    comment '配置ID',
                          config_name       varchar(50)     default ''                 comment '配置名称',
                          show_name         varchar(50)     default ''                 comment '按钮显示名称',
                          config_type       char(1)         not null                   comment '配置类型（B普通按钮 F菜单按钮 T文本 I图片 V视频 D自定义）',
                          parent_id         bigint(20)      default 0                  comment '父配置ID',
                          order_num         int(4)          default 0                  comment '显示顺序',
                          url               varchar(1024)    default ''                comment '自定义请求地址',
                          image             varchar(200)    default ''                comment '自定义图片',
                          video             varchar(200)    default ''                comment '自定义视频',
                          text              varchar(1024)   default ''                 comment '自定义文字',
                          trigger_type      varchar(50)     default ''                 comment '触发方式（URL打开指定链接 CALLBACK_IMG发送指定图片+文字 CALLBACK_VIDEO发送指定视频+文字）',
                          callback_data     varchar(50)     default ''                 comment '按下按钮时要在回调查询中发送到bot的数据',
                          callback_text     varchar(100)    default ''                 comment '发送信息时要在回调查询中发送到bot的数据',
                          visible           char(1)         default 0                  comment '配置状态（0显示 1隐藏）',
                          create_by         varchar(64)     default ''                 comment '创建者',
                          create_time       datetime                                   comment '创建时间',
                          update_by         varchar(64)     default ''                 comment '更新者',
                          update_time       datetime                                   comment '更新时间',
                          remark            varchar(500)    default ''                 comment '备注',
                          primary key (config_id)
) engine=innodb auto_increment=2000 comment = '机器人配置';


INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2003, '实时汇率', '实时汇率', 'F', 0, 1, '', '', '', '', 'CALLBACK_IMG', '', '/rate', '0', '', '2023-11-20 23:25:00', 'admin', '2023-11-26 07:01:15', '此按钮特殊处理，抓取实时汇率');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2004, '在线购买', '在线购买', 'F', 0, 2, '', '', '', '', 'CALLBACK_IMG', '', '/to_buy', '0', '', '2023-11-20 23:41:00', 'admin', '2023-11-26 07:01:41', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2005, '我的订单', '我的订单', 'F', 0, 3, '', '', '', '', 'CALLBACK_IMG', '', '/order', '0', '', '2023-11-20 23:42:25', 'admin', '2023-11-26 07:01:51', '此按钮特殊处理，查询用户订单');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2006, '人工客服', '人工客服', 'F', 0, 4, '', '', '', '', 'CALLBACK_IMG', '', '/service', '0', '', '2023-11-20 23:44:05', 'admin', '2023-11-26 07:02:04', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2007, '代理中心', '代理中心', 'F', 0, 5, '', '', '', '', 'CALLBACK_IMG', '', '/agent', '0', '', '2023-11-20 23:44:41', 'admin', '2023-11-26 07:02:12', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2008, '个人中心', '个人中心', 'F', 0, 6, '', '', '', '', 'CALLBACK_IMG', '', '/mine', '0', '', '2023-11-20 23:45:20', 'admin', '2023-11-26 07:02:19', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2009, '实时汇率图片', '', 'I', 2003, 1, '', '/profile/upload/2023/11/23/home_20231123005339A001.jpg', '', '', 'undefined', '', '', '0', '', '2023-11-21 21:50:36', 'admin', '2023-11-24 02:33:47', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2010, '购买估价', '购买估价', 'B', 2003, 3, '', '', '', '', 'CALLBACK_IMG', '/valuation', '', '0', '', '2023-11-21 21:55:00', '', '2023-11-21 22:12:04', '此按钮特殊处理，获取估价信息');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2011, '购买估价文本', '', 'T', 2010, 1, '', '', '', '自定义文本\r\n请输入您要购买的数量', 'undefined', '', '', '0', '', '2023-11-21 21:56:44', '', '2023-11-21 22:03:18', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2012, '实时汇率文本', '', 'T', 2003, 2, '', '', '', '💰 当前下发记录\r\n------------\r\n{record}\r\n------------\r\n实时汇率自定义文本\r\n实时汇率自定义文本\r\n实时汇率自定义文本', 'undefined', '', '', '0', '', '2023-11-21 22:05:04', 'admin', '2023-11-24 22:49:41', '{record} 链上下发记录');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2013, '在线购买图片', '', 'I', 2004, 1, '', '/profile/upload/2023/11/21/home_20231121221309A001.jpg', '', '', 'undefined', '', '', '0', '', '2023-11-21 22:13:09', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2014, '在线购买文本', '', 'T', 2004, 2, '', '', '', '在线购买自定义文本\r\n在线购买自定义文本\r\n在线购买自定义文本', 'undefined', '', '', '0', '', '2023-11-21 22:14:14', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2015, '点击购买', '点击购买', 'B', 2004, 3, '', '', '', '', 'CALLBACK_IMG', '/buy', '', '0', '', '2023-11-21 22:16:45', 'admin', '2023-11-24 02:35:50', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2016, '点击购买文本', '', 'T', 2015, 1, '', '', '', '点击购买自定义文本\r\n点击购买自定义文本\r\n点击购买自定义文本', 'undefined', '', '', '0', '', '2023-11-21 22:17:52', 'admin', '2023-11-26 06:46:21', '此按钮特殊处理，与用户进行购买交互');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2017, '我的订单图片', '', 'I', 2005, 1, '', '/profile/upload/2023/11/21/home_20231121223256A002.jpg', '', '', 'undefined', '', '', '0', '', '2023-11-21 22:32:56', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2019, '人工客服图片', '', 'I', 2006, 1, '', '/profile/upload/2023/11/21/home_20231121223637A003.jpg', '', '', 'undefined', '', '', '0', '', '2023-11-21 22:36:38', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2020, '人工客服文本', '', 'T', 2006, 2, '', '', '', '人工客服自定义文本\r\n人工客服自定义文本\r\n人工客服自定义文本', 'undefined', '', '', '0', '', '2023-11-21 22:37:27', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2021, '联系客服', '联系客服', 'B', 2006, 3, 'https://t.me/YOUR_SUPPORT_ACCOUNT', '', '', '', 'URL', '/kefu', '', '0', '', '2023-11-21 22:41:49', 'admin', '2023-11-23 20:40:33', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2022, '官方群组', '官方群组', 'B', 2006, 4, 'https://t.me/YOUR_GROUP_LINK', '', '', '', 'URL', '/group', '', '0', '', '2023-11-21 22:42:23', 'admin', '2023-11-23 20:41:06', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2023, '代理中心图片', '', 'I', 2007, 1, '', '/profile/upload/2023/11/21/home_20231121225026A004.jpg', '', '', 'undefined', '', '', '0', '', '2023-11-21 22:50:27', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2024, '代理中心文本', '', 'T', 2007, 2, '', '', '', '代理中心自定义文本\r\n代理中心自定义文本\r\n代理中心自定义文本', 'undefined', '', '', '0', '', '2023-11-21 22:51:04', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2025, '按钮1', '按钮1', 'B', 2007, 3, '', '', '', '', 'CALLBACK_IMG', '/button1', '', '0', '', '2023-11-21 22:51:34', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2026, '按钮2', '按钮2', 'B', 2007, 4, '', '', '', '', 'CALLBACK_IMG', '/button2', '', '0', '', '2023-11-21 22:51:55', '', '2023-11-21 22:52:07', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2027, '个人中心图片', '', 'I', 2008, 1, '', '/profile/upload/2023/11/21/home_20231121225239A005.jpg', '', '', 'undefined', '', '', '0', '', '2023-11-21 22:52:39', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2028, '个人中心文本', '', 'T', 2008, 2, '', '', '', '💎个人中心\r\n\r\n飞机名：{nickname}\r\n用户名：{username}\r\n用户ID：{user_id}\r\n\r\n个人中心自定义文本\r\n个人中心自定义文本\r\n个人中心自定义文本\r\n\r\n` ydahdahdashjksahfjsahfjkahf `', 'undefined', '', '', '0', '', '2023-11-21 22:53:03', 'admin', '2023-11-24 04:06:53', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2030, '开始', '', 'D', 0, 7, '', '', '', '', 'CALLBACK_VIDEO', '', '/start', '0', 'admin', '2023-11-23 20:47:43', 'admin', '2023-11-23 20:50:30', '用户发送/start触发，仅能设置文本/图片/视频，设置按钮不生效');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2031, '开始自定义文本', '', 'T', 2030, 1, '', '', '', '开始自定义文本\r\n开始自定义文本\r\n开始自定义文本', 'undefined', '', '', '0', 'admin', '2023-11-23 20:48:10', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2032, '开始自定义视频', '', 'V', 2030, 2, '', '', '/profile/upload/2023/11/23/v_20231123205018A001.mp4', '', 'undefined', '', '', '0', 'admin', '2023-11-23 20:50:18', 'admin', '2023-11-24 02:34:24', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2033, '我的订单文本', '', 'T', 2005, 2, '', '', '', '我的订单自定义文本\r\n我的订单自定义文本\r\n我的订单自定义文本', 'undefined', '', '', '0', 'admin', '2023-11-24 01:10:45', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2036, '支付信息', '', 'D', 0, 8, '', '', '', '', 'CALLBACK_IMG', '', '/pay_info', '0', 'admin', '2023-11-24 21:40:35', 'admin', '2023-11-24 21:55:21', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2037, '支付信息文本', '', 'T', 2036, 2, '', '', '', '订单编号：{order_id}\r\n\r\n购买金额：{amount}\r\n当前汇率：{rate}\r\n您会收到：{receive}\r\n您的接收地址：\r\n`{receive_address}`\r\n➖➖➖➖➖➖➖➖➖➖➖➖\r\n支付金额：{pay_amount}\r\n\r\n支付地址 (USDT - TRC20) \r\n`{wallet_address}`\r\n\r\n👆 点击复制 金额/地址，可重复充值!\r\n👆 上面地址和二维码不一致，请不要付款!\r\n\r\n提示：\r\n- 对上述地址👆支付后, 经过3次网络确认, 充值成功!\r\n- 请耐心等待, 支付成功后 系统会通知您!', 'CALLBACK_IMG', '', '', '0', 'admin', '2023-11-24 21:44:00', 'admin', '2023-11-26 05:11:20', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2038, '支付信息图片', '', 'I', 2036, 1, '', '/profile/upload/2023/11/24/home_20231124214433A001.jpg', '', '', 'CALLBACK_IMG', '', '', '0', 'admin', '2023-11-24 21:44:33', '', NULL, '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2039, '取消返回', '取消返回', 'B', 2036, 3, '', '', '', '', 'CALLBACK_IMG', '/pay_cancel', '', '0', 'admin', '2023-11-24 21:46:56', 'admin', '2023-11-24 21:55:02', '');
INSERT INTO `bot_config` (`config_id`, `config_name`, `show_name`, `config_type`, `parent_id`, `order_num`, `url`, `image`, `video`, `text`, `trigger_type`, `callback_data`, `callback_text`, `visible`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2040, '联系客服', '联系客服', 'B', 2036, 4, 'https://t.me/YOUR_SUPPORT_ACCOUNT', '', '', '', 'URL', '', '', '0', 'admin', '2023-11-24 21:47:42', 'admin', '2023-11-24 21:48:11', '');

drop table if exists tg_user;
CREATE TABLE `tg_user` (
                           `id` int NOT NULL AUTO_INCREMENT comment 'ID',
                           `user_id` varchar(100) NOT NULL comment '用户ID',
                           `username` varchar(100) NOT NULL comment '用户名',
                           `nickname` varchar(100) comment '昵称',
                           `is_admin` varchar(1) default 'N' comment '是否管理员',
                           `pay_count` int default 0 comment '成功交易',
                           `pay_amount` decimal(16,2) default 0.00 comment '共支付',
                           `payout_count` int default 0 comment '共下发',
                           `status` varchar(1) default '0' comment '状态',
                           `create_by` varchar(100) NOT NULL comment '创建人',
                           `create_time` datetime NOT NULL comment '创建时间',
                           `update_by` varchar(100)  comment '更新人',
                           `update_time` datetime comment '更新时间',
                           PRIMARY KEY (`id`),
                           KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci comment = '客户管理';


drop table if exists exchange_rate;
CREATE TABLE `exchange_rate` (
                                 `id` int NOT NULL AUTO_INCREMENT comment 'ID',
                                 `min` decimal(16,2) NOT NULL comment '起始值',
                                 `max` decimal(16,2) NOT NULL comment '结束值',
                                 `rate` decimal(6,2) NOT NULL comment '汇率',
                                 `create_by` varchar(100) NOT NULL comment '创建人',
                                 `create_time` datetime NOT NULL comment '创建时间',
                                 `update_by` varchar(100)  comment '更新人',
                                 `update_time` datetime comment '更新时间',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci comment = '汇率管理';

INSERT INTO `exchange_rate` (`id`, `min`, `max`, `rate`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (1, 1.00, 50.00, 8.80, 'admin', '2023-11-23 22:14:44', NULL, NULL);
INSERT INTO `exchange_rate` (`id`, `min`, `max`, `rate`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (2, 51.00, 100.00, 8.90, 'admin', '2023-11-23 22:15:09', NULL, NULL);
INSERT INTO `exchange_rate` (`id`, `min`, `max`, `rate`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (3, 101.00, 200.00, 9.10, 'admin', '2023-11-23 22:15:22', NULL, NULL);
INSERT INTO `exchange_rate` (`id`, `min`, `max`, `rate`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES (4, 201.00, 10000.00, 9.30, 'admin', '2023-11-23 22:15:34', NULL, NULL);


drop table if exists payment_record;
CREATE TABLE `payment_record` (
                                 `id` int NOT NULL AUTO_INCREMENT comment 'ID',
                                 `order_id` varchar(100) NOT NULL comment '订单编号',
                                 `user_id` varchar(100) NOT NULL comment '电报ID',
                                 `username` varchar(100) NOT NULL comment '用户名',
                                 `nickname` varchar(100) comment '网名',
                                 `amount` decimal(16,2) NOT NULL comment '支付金额',
                                 `actual_amount` decimal(16,2) NOT NULL comment '实际支付金额',
                                 `status` varchar(1) default '1' comment '支付状态',
                                 `address` varchar(100) comment '下发地址',
                                 `block_id` varchar(200) comment '区块交易id',
                                 `create_by` varchar(100) NOT NULL comment '创建人',
                                 `create_time` datetime NOT NULL comment '创建时间',
                                 `update_by` varchar(100)  comment '更新人',
                                 `update_time` datetime comment '更新时间',
                                 PRIMARY KEY (`id`),
                                 unique (order_id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci comment = '支付记录表';


drop table if exists payout_record;
CREATE TABLE `payout_record` (
                                  `id` int NOT NULL AUTO_INCREMENT comment 'ID',
                                  `order_id` varchar(100) NOT NULL comment '订单编号',
                                  `user_id` varchar(100) NOT NULL comment '电报ID',
                                  `username` varchar(100) NOT NULL comment '用户名',
                                  `nickname` varchar(100) comment '网名',
                                  `amount` decimal(16,2) NOT NULL comment '支付金额',
                                  `actual_amount` decimal(16,2) NOT NULL comment '实际支付金额',
                                  `status` varchar(1) default '1' comment '支付状态',
                                  `pay_amount` decimal(16,2) NOT NULL comment '应下发',
                                  `p_status` varchar(1) default '1' comment '下发状态',
                                  `address` varchar(100) comment '下发地址',
                                  `hash` varchar(200) comment '交易哈希',
                                  `remark` varchar(500) comment '备注',
                                  `create_by` varchar(100) NOT NULL comment '创建人',
                                  `create_time` datetime NOT NULL comment '创建时间',
                                  `update_by` varchar(100)  comment '更新人',
                                  `update_time` datetime comment '更新时间',
                                  PRIMARY KEY (`id`),
                                  unique (order_id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci comment = '下发记录表';

alter table payment_record add column `block_id` varchar(200) comment '区块交易id';
alter table payment_record add column `expiration_time` datetime comment '过期时间';
alter table tg_user modify column `payout_count` decimal(16,2) default 0.00 comment '共下发';
alter table tg_user add column `remark` varchar(500) comment '备注';
alter table tg_user add column `is_red` varchar(1)  default '1' comment '标红';



drop table if exists user_notify;
CREATE TABLE `user_notify` (
                               `id` int NOT NULL AUTO_INCREMENT comment 'ID',
                               `notify_type` varchar(1) NOT NULL comment '通知类型',
                               `user_id` varchar(2048) default '' comment '用户ID',
                               `image`   varchar(200)    default ''  comment '通知图片',
                               `text`    varchar(1024)   default ''  comment '通知内容',
                               `status`  varchar(1) default '2' comment '状态',
                               `create_by` varchar(100) NOT NULL comment '创建人',
                               `create_time` datetime NOT NULL comment '创建时间',
                               `update_by` varchar(100)  comment '更新人',
                               `update_time` datetime comment '更新时间',
                               PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci comment = '通知管理';
