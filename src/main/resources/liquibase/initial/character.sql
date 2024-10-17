--liquibase formatted sql

--changeset guronas:init-4
create type character_gender as enum ('MALE', 'FEMALE');

--changeset guronas:init-5
create table if not exists storyteller.character (
    id integer generated always as identity primary key,
    user_id bigint not null,
    gender character_gender not null,
    name varchar(32) not null unique check (name ~ '^[A-Za-zА-Яа-я0-9_]{4,32}$'),
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    active_class_id integer not null,
    constraint fk_active_class foreign key (active_class_id) references storyteller.class_dictionary(id)
);

--changeset guronas:init-6
create unique index idx_user_id on storyteller.character (user_id);

--changeset guronas:init-7
create trigger update_timestamp_trigger
before update on storyteller.character
for each row
execute function update_timestamp();
