create table if not exists questions
(
    id serial primary key ,
    question_title varchar (50) not null ,
    question_text varchar (200) not null
);