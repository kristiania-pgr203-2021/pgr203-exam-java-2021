create table if not exists members
(
    id serial primary key ,
    first_name varchar (50),
    last_name varchar (50),
    email varchar (50)
);