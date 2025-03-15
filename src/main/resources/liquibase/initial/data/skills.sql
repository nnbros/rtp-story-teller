--liquibase formatted sql

--changeset guronas:init-19
insert into skill_dictionary (name, type, hero_atk, hero_def, army_atk, army_def, hero_split, army_split)
values
('sunder', 'WARRIOR', 1, 1, 1, 1, 1, 1),
('riposte', 'WARRIOR', 1, 1, 1, 1, 1, 1),
('pierce', 'ROGUE', 1, 1, 1, 1, 1, 1),
('annihilation', 'ROGUE', 1, 1, 1, 1, 1, 1);