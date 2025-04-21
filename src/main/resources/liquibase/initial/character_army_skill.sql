--liquibase formatted sql

--changeset guronas:init-23
create table if not exists storyteller.character_army_skill (
    id integer generated always as identity primary key,
    character_army_id integer not null,
    skill_id integer not null,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint fk_character_army foreign key (character_army_id) references storyteller.character_army(id),
    constraint fk_skill foreign key (skill_id) references storyteller.skill_dictionary(id),
    constraint unique_character_army_skill unique (character_army_id, skill_id)
);

--changeset guronas:init-24
create trigger update_timestamp_trigger
before update on storyteller.character_army_skill
for each row
execute function update_timestamp();