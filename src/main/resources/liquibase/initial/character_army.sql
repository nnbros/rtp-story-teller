--liquibase formatted sql

--changeset guronas:init-16
create or replace function storyteller.set_only_one_active_army()
returns trigger as $$
begin
  -- turn off all others
  update storyteller.character_army
  set is_active = false
  where character_id = new.character_id
    and id <> new.id;

  return new;
end
$$ language plpgsql;

--changeset guronas:init-17
create table if not exists storyteller.character_army (
    id integer generated always as identity primary key,
    character_id integer not null,
    army_id integer not null,
    level integer not null default 1,
    is_active boolean not null default false,
    created_at timestamp not null default now(),
    updated_at timestamp not null default now(),
    constraint fk_character foreign key (character_id) references storyteller.character(id),
    constraint fk_army foreign key (army_id) references storyteller.army_dictionary(id),
    constraint unique_character_army unique (character_id, army_id)
);

--changeset guronas:init-18
create unique index unique_active_army_per_char
on storyteller.character_army(character_id)
where is_active = true;

--changeset guronas:init-19
create trigger set_active_army_trigger
before insert or update of is_active
on storyteller.character_army
for each row
when (new.is_active = true)
execute function set_only_one_active_army();

--changeset guronas:init-20
create trigger update_timestamp_trigger
before update on storyteller.character_army
for each row
execute function update_timestamp();
