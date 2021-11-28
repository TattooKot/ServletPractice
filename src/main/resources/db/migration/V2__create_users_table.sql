drop table users;

create table if not exists users(
    id serial,
    ipaddress varchar(15) not null unique
);