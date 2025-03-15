--liquibase formatted sql

--changeset guronas:init-4
create table if not exists storyteller.class_dictionary (
    id integer generated always as identity primary key,
    name varchar(20) not null unique,
    base_hp integer not null,
    base_atk integer not null,
    base_def integer not null
);
