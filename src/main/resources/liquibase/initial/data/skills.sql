--liquibase formatted sql

--changeset guronas:init-11
insert into skill_dictionary (id, name, class_id)
values
(1, 'sunder', 1),
(2, 'riposte', 1),
(3, 'pierce', 2),
(4, 'annihilation', 2);