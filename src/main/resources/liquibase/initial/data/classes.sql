--liquibase formatted sql

--changeset guronas:init-10
insert into class_dictionary (id, name, base_hp, base_atk, base_def)
values
(1, 'warrior', 100, 100, 100),
(2, 'rogue', 59, 59, 59);