--liquibase formatted sql

--changeset guronas:init-20
insert into class_dictionary (name, base_hp, base_atk, base_def)
values
('warrior', 100, 100, 100),
('rogue', 59, 59, 59);