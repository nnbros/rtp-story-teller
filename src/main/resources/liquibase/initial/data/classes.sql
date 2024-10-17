--liquibase formatted sql

--changeset guronas:init-10
insert into class_dictionary (name, description, base_hp, base_atk, base_def)
values
('Воин', 'Сильный боец ближнего боя', 100, 100, 100),
('Плут', 'Мастер подлых трюков', 59, 59, 59);