--liquibase formatted sql

--changeset guronas:init-2
create type archetype as enum ('SWORDSMAN', 'CAVALRY', 'SPEARMAN', 'NEUTRAL');

--changeset guronas:init-3
create type skill_type as enum ('BASIC_CHARACTER', 'BASIC_ARMY', 'NEUTRAL', 'WARRIOR', 'ROGUE', 'RIDER', 'GUARDIAN',
 'SWORDSMAN', 'CAVALRY', 'SPEARMAN', 'SEEKERS', 'SCOUTS', 'PEASANTS');

--changeset guronas:init-4
create table if not exists storyteller.skill_dictionary (
    id integer generated always as identity primary key,
    name varchar(32) not null unique,
    type skill_type not null,
    effective_against archetype
);