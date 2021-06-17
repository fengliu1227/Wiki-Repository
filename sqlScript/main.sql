drop table if exists `t_ebook`;
create table `t_ebook`
(
    `id`   bigint not null comment 'id',
    `name` varchar(50) comment 'name',
    `category1_id`   bigint comment 'category1',
    `category2_id`   bigint comment 'category2',
    `description`   varchar(200) comment 'description',
    `cover`   varchar(200) comment 'cover',
    `doc_count` int default 0 comment 'doc_count',
    `view_count` int default 0 comment 'view_count',
    `vote_count` int default 0 comment 'vote_count',
    primary key (`id`)
) engine=innodb default charset=utf8mb4;

insert into `t_ebook` (id, name, description) values(1, 'spring boot入门教程', '零基础入门java开发，企业级应用开发最佳框架');
insert into `t_ebook` (id, name, description) values(2, 'Vue入门教程', '零基础入门前端开发，企业级应用开发最佳框架');
insert into `t_ebook` (id, name, description) values (3, 'Python 入门教程', '零基础入门 Python 开发，企业级应用开发最佳首选框架');
insert into `t_ebook` (id, name, description) values (4, 'Mysql 入门教程', '零基础入门 Mysql 开发，企业级应用开发最佳首选框架');
insert into `t_ebook` (id, name, description) values (5, 'Oracle 入门教程', '零基础入门 Oracle 开发，企业级应用开发最佳首选框架');

# 分类
drop table if exists `t_category`;
create table `t_category` (
                            `id` bigint not null comment 'id',
                            `parent` bigint not null default 0 comment '父id',
                            `name` varchar(50) not null comment '名称',
                            `sort` int comment '顺序',
                            primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='分类';

insert into `t_category` (id, parent, name, sort) values (100, 000, '前端开发', 100);
insert into `t_category` (id, parent, name, sort) values (101, 100, 'Vue', 101);
insert into `t_category` (id, parent, name, sort) values (102, 100, 'HTML & CSS', 102);
insert into `t_category` (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into `t_category` (id, parent, name, sort) values (201, 200, '基础应用', 201);
insert into `t_category` (id, parent, name, sort) values (202, 200, '框架应用', 202);
insert into `t_category` (id, parent, name, sort) values (300, 000, 'Python', 300);
insert into `t_category` (id, parent, name, sort) values (301, 300, '基础应用', 301);
insert into `t_category` (id, parent, name, sort) values (302, 300, '进阶方向应用', 302);
insert into `t_category` (id, parent, name, sort) values (400, 000, '数据库', 400);
insert into `t_category` (id, parent, name, sort) values (401, 400, 'MySQL', 401);
insert into `t_category` (id, parent, name, sort) values (500, 000, '其它', 500);
insert into `t_category` (id, parent, name, sort) values (501, 500, '服务器', 501);
insert into `t_category` (id, parent, name, sort) values (502, 500, '开发工具', 502);

-- 文档表
drop table if exists `t_doc`;
create table `t_doc` (
                       `id` bigint not null comment 'id',
                       `ebook_id` bigint not null default 0 comment '电子书id',
                       `parent` bigint not null default 0 comment '父id',
                       `name` varchar(50) not null comment '名称',
                       `sort` int comment '顺序',
                       `view_count` int default 0 comment '阅读数',
                       `vote_count` int default 0 comment '点赞数',
                       primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文档';

insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (1, 1, 0, '文档1', 1, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (2, 1, 1, '文档1.1', 1, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (3, 1, 0, '文档2', 2, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (4, 1, 3, '文档2.1', 1, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (5, 1, 3, '文档2.2', 2, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (6, 1, 5, '文档2.2.1', 1, 0, 0);

-- 文档内容
drop table if exists `t_content`;
create table `t_content` (
                           `id` bigint not null comment '文档id',
                           `content` mediumtext not null comment '内容',
                           primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文档内容';

-- user
drop table if exists `t_user`;
create table `t_user` (
                        `id` bigint not null comment 'ID',
                        `login_name` varchar(50) not null comment '登陆名',
                        `name` varchar(50) comment '昵称',
                        `password` char(32) not null comment '密码',
                        `role` char(32) not null default 'ROLE_USER' comment 'Role',
                        primary key (`id`),
                        unique key `login_name_unique` (`login_name`)
) engine=innodb default charset=utf8mb4 comment='用户';

insert into `t_user` (id, `login_name`, `name`, `password`, `role` ) values (1, 'user1', 'user1', '0d8d5cd06832b29560745fe4e1b941cf', 'ROLE_USER');
insert into `t_user` (id, `login_name`, `name`, `password`, `role` ) values (2, 'admin1', 'admin1', 'c3284d0f94606de1fd2af172aba15bf3', 'ROLE_ADMIN');

-- user-vote schemas
drop table if exists `t_user2vote`;
create table `t_user2vote` (
                          `id` bigint not null comment 'ID',
                          `user_id` bigint not null comment 'userId',
                          `doc_id` bigint not null comment 'DocId',
                          primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='user2vote';