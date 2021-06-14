drop table if exists `t_user`;
create table `t_user`
(
    `id`   bigint not null comment 'id',
    `name` varchar(50) comment 'name',
    `password` varchar(50) comment 'password',
    primary key (`id`)
)

drop table if exists `t_ebook`;
create table `t_ebook`
(
    `id`   bigint not null comment 'id',
    `name` varchar(50) comment 'name',
    `category1_id`   bigint comment 'category1',
    `category2_id`   bigint comment 'category2',
    `description`   varchar(200) comment 'description',
    `cover`   varchar(200) comment 'cover',
    `doc_count` int comment 'doc_count',
    `view_count` int comment 'view_count',
    `vote_count` int comment 'vote_count',
    primary key (`id`)
) engine=innodb default charset=utf8mb4;

insert into `t_ebook` (id, name, description) values(1, 'spring boot入门教程', '零基础入门java开发，企业级应用开发最佳框架');
insert into `t_ebook` (id, name, description) values(2, 'Vue入门教程', '零基础入门前端开发，企业级应用开发最佳框架');

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