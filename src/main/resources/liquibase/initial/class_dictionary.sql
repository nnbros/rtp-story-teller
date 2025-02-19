--liquibase formatted sql

--changeset guronas:init-2
create table if not exists storyteller.class_dictionary (
    id integer primary key,
    name varchar(20) not null unique,
    base_hp integer not null,
    base_atk integer not null,
    base_def integer not null
);
