--liquibase formatted sql

--changeset guronas:init-29
create or replace view storyteller.active_character_class_view as
select
  cc.character_id,
  cc.id as character_class_id,
  cd.id as class_id,
  cd.name,
  cd.type,
  cd.advantage_bonus,
  cd.base_hp,
  cd.base_atk,
  cd.base_def,
  cc.experience
from storyteller.character_class cc
join storyteller.class_dictionary cd on cd.id = cc.class_id
where cc.is_active = true;