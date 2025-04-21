--liquibase formatted sql

--changeset guronas:init-7
create type character_gender as enum ('MALE', 'FEMALE');

--changeset guronas:init-8
create table if not exists storyteller.character (
    id integer generated always as identity primary key,
    user_id bigint not null,
    gender character_gender not null,
    name varchar(32) not null unique check (name ~ '^[A-Za-zА-Яа-я0-9_]{4,32}$'),
    created_at timestamp not null default now(),
    updated_at timestamp not null default now()
);

--changeset guronas:init-9
create unique index idx_user_id on storyteller.character (user_id);

--changeset guronas:init-10
create trigger update_timestamp_trigger
before update on storyteller.character
for each row
execute function update_timestamp();
