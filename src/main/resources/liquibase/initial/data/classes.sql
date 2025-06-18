--liquibase formatted sql

--changeset guronas:init-26
insert into class_dictionary (name, type, base_hp, base_atk, base_def, advantage_bonus)
values
('warrior', 'SWORDSMAN', 1000, 100, 100, 0.25),
('rogue', 'NEUTRAL', 1000, 100, 100, 0.25);