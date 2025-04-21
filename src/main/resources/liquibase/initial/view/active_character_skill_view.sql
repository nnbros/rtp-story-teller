--liquibase formatted sql

--changeset guronas:init-28
create or replace view storyteller.active_character_skill_view as
select
  cc.character_id,
  ccs.id as character_skill_id,
  sd.id as skill_id,
  sd.name,
  sd.type,
  sd.effective_against
from storyteller.character_class_skill ccs
join storyteller.character_class cc on cc.id = ccs.character_class_id
join storyteller.skill_dictionary sd on sd.id = ccs.skill_id
where cc.is_active = true
union
select
  ca.character_id,
  cas.id as character_skill_id,
  sd.id as skill_id,
  sd.name,
  sd.type,
  sd.effective_against
from storyteller.character_army_skill cas
join storyteller.character_army ca on ca.id = cas.character_army_id
join storyteller.skill_dictionary sd on sd.id = cas.skill_id
where ca.is_active = true;