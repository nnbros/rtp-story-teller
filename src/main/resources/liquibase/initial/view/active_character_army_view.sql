--liquibase formatted sql

--changeset guronas:init-30
create or replace view storyteller.active_character_army_view as
select
  ca.character_id,
  ca.id as character_army_id,
  ad.id as army_id,
  ad.name,
  ad.type,
  ad.advantage_bonus,
  ad.base_quantity,
  ad.base_hp,
  ad.base_atk,
  ad.base_def,
  ad.tier,
  ca.level
from storyteller.character_army ca
join storyteller.army_dictionary ad on ad.id = ca.army_id
where ca.is_active = true;