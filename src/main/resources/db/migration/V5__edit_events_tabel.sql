drop table events;

create table if not exists events(
    id serial,
    filename varchar(100) not null,
    uploadDate timestamp not null
);