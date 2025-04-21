--liquibase formatted sql

--changeset guronas:init-11
create or replace function storyteller.set_only_one_active_class()
returns trigger as $$
begin
  -- turn off all others
  update storyteller.character_class
  set is_active = false
  where character_id = new.character_id
    and id <> new.id;

  return new;
end
$$ language plpgsql;

--changeset guronas:init-12
create table if not exists storyteller.character_class (
    id integer generated always as identity primary key,
    character_id integer not null,
    class_id integer not null,
    experience bigint not null default 0,
    is_active boolean not null default false,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint fk_character foreign key (character_id) references storyteller.character(id),
    constraint fk_class foreign key (class_id) references storyteller.class_dictionary(id),
    constraint unique_character_class unique (character_id, class_id)
);

--changeset guronas:init-13
create unique index unique_active_class_per_char
on storyteller.character_class(character_id)
where is_active = true;

--changeset guronas:init-14
create trigger set_active_class_trigger
before insert or update of is_active
on storyteller.character_class
for each row
when (new.is_active = true)
execute function set_only_one_active_class();

--changeset guronas:init-15
create trigger update_timestamp_trigger
before update on storyteller.character_class
for each row
execute function update_timestamp();
