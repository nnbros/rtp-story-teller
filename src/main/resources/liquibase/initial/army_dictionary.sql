--liquibase formatted sql

--changeset guronas:init-6
create table if not exists storyteller.army_dictionary (
    id integer generated always as identity primary key,
    name varchar(20) not null unique,
    type archetype not null,
    base_hp integer not null,
    base_quantity integer not null,
    base_atk integer not null,
    base_def integer not null,
    tier integer not null,
    advantage_bonus real not null
);
