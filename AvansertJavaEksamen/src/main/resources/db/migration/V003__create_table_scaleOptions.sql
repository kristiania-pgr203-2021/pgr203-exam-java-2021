create table if not exists scale_options
(
    id serial primary key ,
    scale_value varchar (50) not null ,
    question_fk int
)