create table if not exists option_to_qn
(
    id serial primary key ,
    option_value varchar (100) not null ,
    question_fk int
    );