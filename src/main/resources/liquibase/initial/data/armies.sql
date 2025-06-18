--liquibase formatted sql

--changeset guronas:init-27
insert into army_dictionary (name, type, base_hp, base_quantity, base_atk, base_def, tier, advantage_bonus)
values
('seekers', 'SWORDSMAN', 200, 3, 50, 50, 1, 0.25),
('scouts', 'CAVALRY', 160, 3, 60, 40, 1, 0.25),
('peasants', 'SPEARMAN', 240, 3, 40, 60, 1, 0.25)