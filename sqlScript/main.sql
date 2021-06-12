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