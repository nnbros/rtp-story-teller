--liquibase formatted sql

--changeset guronas:init-8
create table if not exists storyteller.character_class (
    id integer generated always as identity primary key,
    character_id integer not null,
    class_id integer not null,
    experience bigint not null default 0,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint fk_character foreign key (character_id) references storyteller.character(id),
    constraint fk_class foreign key (class_id) references storyteller.class_dictionary(id),
    constraint unique_character_class unique (character_id, class_id)
);

--changeset guronas:init-9
create trigger update_timestamp_trigger
before update on storyteller.character_class
for each row
execute function update_timestamp();
