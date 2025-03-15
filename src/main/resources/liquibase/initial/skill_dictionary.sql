--liquibase formatted sql

--changeset guronas:init-2
create type skill_type as enum ('BASIC_CHARACTER', 'BASIC_ARMY', 'WARRIOR', 'ROGUE', 'RIDER', 'GUARDIAN',
 'SWORDSMAN', 'CAVALRY', 'SPEARMAN', 'SEEKERS', 'SCOUTS', 'PEASANTS');

--changeset guronas:init-3
create table if not exists storyteller.skill_dictionary (
    id integer generated always as identity primary key,
    name varchar(32) not null unique,
    type skill_type not null,
    effective_against integer,
    hero_atk integer not null,
    hero_def integer not null,
    army_atk integer not null,
    army_def integer not null,
    hero_split integer not null,
    army_split integer not null
);