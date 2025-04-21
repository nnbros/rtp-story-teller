--liquibase formatted sql

--changeset guronas:init-21
create table if not exists storyteller.character_class_skill (
    id integer generated always as identity primary key,
    character_class_id integer not null,
    skill_id integer not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint fk_character_class foreign key (character_class_id) references storyteller.character_class(id),
    constraint fk_skill foreign key (skill_id) references storyteller.skill_dictionary(id),
    constraint unique_character_class_skill unique (character_class_id, skill_id)
);

--changeset guronas:init-22
create trigger update_timestamp_trigger
before update on storyteller.character_class_skill
for each row
execute function update_timestamp();