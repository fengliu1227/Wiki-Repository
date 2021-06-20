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

insert into `t_ebook` (id, name, `category1_id`, `category2_id`, description) values(1, 'spring boot', 200, 202,'Spring Boot makes it easy to create stand-alone that you can "just run".');
insert into `t_ebook` (id, name, `category1_id`, `category2_id`, description) values(2, 'Vue', 100, 101, 'The Progressive JavaScript Framework');
insert into `t_ebook` (id, name, `category1_id`, `category2_id`, description) values (3, 'Spring', 200, 202, 'The Spring Framework provides a comprehensive programming and configuration model for modern Java-based enterprise applications');
insert into `t_ebook` (id, name, `category1_id`, `category2_id`, description) values (4, 'SpringMVC', 200, 202, 'The Spring Web MVC framework provides Model-View-Controller (MVC) architecture');
insert into `t_ebook` (id, name, `category1_id`, `category2_id`, description) values (5, 'Mysql', 400, 401, 'MySQL is an open-source relational database management system (RDBMS)');
insert into `t_ebook` (id, name, `category1_id`, `category2_id`, description) values (6, 'Java Core', 200, 201, 'Java is at the heart of our digital lifestyle.');
insert into `t_ebook` (id, name, `category1_id`, `category2_id`, description) values (7, 'Mybatis', 200, 202, 'MyBatis is a first class persistence framework with support for custom SQL, stored procedures and advanced mappings.');
insert into `t_ebook` (id, name, `category1_id`, `category2_id`, description) values (8, 'Javascript', 100, 102, 'JavaScript is the programming language of the Web');


# category schema
drop table if exists `t_category`;
create table `t_category` (
                            `id` bigint not null comment 'id',
                            `parent` bigint not null default 0 comment 'parent',
                            `name` varchar(50) not null comment 'name',
                            `sort` int comment 'sort',
                            primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='category';

insert into `t_category` (id, parent, name, sort) values (100, 000, 'Front End', 100);
insert into `t_category` (id, parent, name, sort) values (101, 100, 'Vue', 101);
insert into `t_category` (id, parent, name, sort) values (102, 100, 'JS', 102);
insert into `t_category` (id, parent, name, sort) values (103, 100, 'HTML', 103);
insert into `t_category` (id, parent, name, sort) values (104, 100, 'CSS', 104);
insert into `t_category` (id, parent, name, sort) values (105, 100, 'echart.js', 105);
insert into `t_category` (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into `t_category` (id, parent, name, sort) values (201, 200, 'Java Core', 201);
insert into `t_category` (id, parent, name, sort) values (202, 200, 'FrameWork', 202);
insert into `t_category` (id, parent, name, sort) values (400, 000, 'DataBase', 400);
insert into `t_category` (id, parent, name, sort) values (401, 400, 'MySQL', 401);
insert into `t_category` (id, parent, name, sort) values (500, 000, 'Middleware', 500);
insert into `t_category` (id, parent, name, sort) values (501, 500, 'RocketMQ', 501);
insert into `t_category` (id, parent, name, sort) values (502, 500, 'Redis', 502);

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
) engine=innodb default charset=utf8mb4 comment='Content';

insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (1, 1, 0, 'Ch. 1', 1, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (2, 1, 1, 'Sect. 1.1', 1, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (3, 1, 0, 'Ch. 2', 2, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (4, 1, 3, 'Sect. 2.1', 1, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (5, 1, 3, 'Sect. 2.2', 2, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (6, 1, 5, 'Sect. 2.1.1', 1, 0, 0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (7,6,0,'Ch. 1 Basic Concept',0,2,0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (8,6,7,'Sect. 1.1 OOP',1,2,0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (9, 6,7,'Sect. 1.2 Keywords',2,1,0);
insert into `t_doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (10, 6,7,'Sect 1.3 Thread',3,1,0);


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

-- role schema
drop table if exists `t_role`;
create table `t_role` (
                                    `id` bigint auto_increment not null comment 'id',
                                    `role` char(32) not null comment 'Role',
                                    primary key (`id`),
                                    unique key `role_unique` (`role`)
) engine=innodb default charset=utf8mb4 comment='ebook_snapshot';

insert into `t_role` (id, `role` ) values (1, 'ROLE_USER');
insert into `t_role` (id, `role` ) values (2, 'ROLE_ADMIN');