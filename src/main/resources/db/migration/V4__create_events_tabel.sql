create table if not exists events(
    id serial,
    filename varchar(100) not null,
    uploaddate timestamp not null,
    userId int not null
);