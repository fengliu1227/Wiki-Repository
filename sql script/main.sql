drop table if exists `abc`;
create table `abc`
(
    `id`   bigint not null comment 'id',
    `name` varchar(50) comment 'name',
    `password` varchar(50) comment 'password',
    primary key (`id`)
)