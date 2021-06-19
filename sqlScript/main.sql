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

# category schema
drop table if exists `t_category`;
create table `t_category` (
                            `id` bigint not null comment 'id',
                            `parent` bigint not null default 0 comment 'parent',
                            `name` varchar(50) not null comment 'name',
                            `sort` int comment 'sort',
                            primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='category';

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

-- doc schema
drop table if exists `t_doc`;
create table `t_doc` (
                       `id` bigint not null comment 'id',
                       `ebook_id` bigint not null default 0 comment 'ebook_id',
                       `parent` bigint not null default 0 comment 'parent',
                       `name` varchar(50) not null comment 'name',
                       `sort` int comment 'sort',
                       `view_count` int default 0 comment 'view_count',
                       `vote_count` int default 0 comment 'vote_count',
                       primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='文档';

insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (1, 1, 0, '文档1', 1, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (2, 1, 1, '文档1.1', 1, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (3, 1, 0, '文档2', 2, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (4, 1, 3, '文档2.1', 1, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (5, 1, 3, '文档2.2', 2, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (6, 1, 5, '文档2.2.1', 1, 0, 0);

-- content schema
drop table if exists `t_content`;
create table `t_content` (
                           `id` bigint not null comment 'id',
                           `content` mediumtext not null comment 'content',
                           primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='content';

-- user schema
drop table if exists `t_user`;
create table `t_user` (
                        `id` bigint not null comment 'id',
                        `login_name` varchar(50) not null comment 'login_name',
                        `name` varchar(50) comment 'name',
                        `password` char(32) not null comment 'password',
                        `role` char(32) not null default 'ROLE_USER' comment 'Role',
                        primary key (`id`),
                        unique key `login_name_unique` (`login_name`)
) engine=innodb default charset=utf8mb4 comment='user';

insert into `t_user` (id, `login_name`, `name`, `password`, `role` ) values (1, 'user1', 'user1', '0d8d5cd06832b29560745fe4e1b941cf', 'ROLE_USER');
insert into `t_user` (id, `login_name`, `name`, `password`, `role` ) values (2, 'admin1', 'admin1', 'c3284d0f94606de1fd2af172aba15bf3', 'ROLE_ADMIN');

-- user-vote schema
drop table if exists `t_user2vote`;
create table `t_user2vote` (
                          `id` bigint not null comment 'ID',
                          `user_id` bigint not null comment 'userId',
                          `doc_id` bigint not null comment 'DocId',
                          primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='user2vote';

-- ebook_snapshot schema
drop table if exists `t_ebook_snapshot`;
create table `t_ebook_snapshot` (
                                  `id` bigint auto_increment not null comment 'id',
                                  `ebook_id` bigint not null default 0 comment 'ebook_id',
                                  `date` date not null comment 'date',
                                  `view_count` int not null default 0 comment 'view_count',
                                  `vote_count` int not null default 0 comment 'vote_count',
                                  `view_increase` int not null default 0 comment 'view_increase',
                                  `vote_increase` int not null default 0 comment 'vote_increase',
                                  primary key (`id`),
                                  unique key `ebook_id_date_unique` (`ebook_id`, `date`)
) engine=innodb default charset=utf8mb4 comment='ebook_snapshot';