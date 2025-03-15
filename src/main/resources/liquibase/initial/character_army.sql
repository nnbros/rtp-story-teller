--liquibase formatted sql

--changeset guronas:init-13
create table if not exists storyteller.character_army (
    id integer generated always as identity primary key,
    character_id integer not null,
    army_id integer not null,
    level integer not null default 1,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint fk_character foreign key (character_id) references storyteller.character(id),
    constraint fk_army foreign key (army_id) references storyteller.army_dictionary(id),
    constraint unique_character_army unique (character_id, army_id)
);

--changeset guronas:init-14
create trigger update_timestamp_trigger
before update on storyteller.character_army
for each row
execute function update_timestamp();
