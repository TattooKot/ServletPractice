drop table users;

create table if not exists users(
    id serial,
    userName varchar(50) not null unique
);