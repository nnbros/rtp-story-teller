--liquibase formatted sql

--changeset guronas:init-21
insert into army_dictionary (name, type, base_hp, base_count, base_atk, base_def, tier)
values
('seekers', 'SWORDSMAN', 100, 100, 100, 100, 1),
('scouts', 'CAVALRY', 59, 59, 59, 59, 1),
('peasants', 'SPEARMAN', 30, 30, 30, 30, 1)