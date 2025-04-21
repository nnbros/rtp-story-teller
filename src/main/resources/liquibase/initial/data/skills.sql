--liquibase formatted sql

--changeset guronas:init-25
insert into skill_dictionary (name, type, effective_against)
values
('reckless_attack', 'BASIC_CHARACTER', null),
('assault', 'BASIC_CHARACTER', null),
('defense', 'BASIC_CHARACTER', null),
('full_defense', 'BASIC_CHARACTER', null),
('lead', 'BASIC_CHARACTER', null),
('battle_readiness', 'BASIC_CHARACTER', null),
('duel', 'WARRIOR', 'SPEARMAN'),
('cleave', 'WARRIOR', 'SPEARMAN'),
('elimination', 'ROGUE', null),
('fire_bomb', 'ROGUE', null),
('onslaught', 'CAVALRY', 'SWORDSMAN'),
('raid', 'SCOUTS', 'SWORDSMAN'),
('iron_tide', 'SWORDSMAN', 'SPEARMAN'),
('unearth', 'SEEKERS', 'SPEARMAN'),
('hold_the_line', 'SPEARMAN', 'CAVALRY'),
('make_the_way', 'PEASANTS', 'CAVALRY')