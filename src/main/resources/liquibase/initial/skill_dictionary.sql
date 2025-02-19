--liquibase formatted sql

--changeset guronas:init-3
create table if not exists storyteller.skill_dictionary (
    id integer primary key,
    name varchar(32) not null unique,
    class_id integer not null,
    constraint fk_class foreign key (class_id) references storyteller.class_dictionary(id)
);