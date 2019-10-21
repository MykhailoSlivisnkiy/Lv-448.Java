INSERT INTO public.audiences(name)
VALUES ('Leonardo da Vinci'),
       ('Pictures of the Middle Ages'),
       ('Sculptures');


INSERT INTO public.autors(first_name, last_name)
VALUES ('Leonardo', 'da Vinci'),
       ('Francesco', 'Melzi'),
       ('Bernardino', 'Luini'),
       ('Auguste', 'Rodin');


INSERT INTO public.exhibits(type, material, techic, audience_id, name)
VALUES ('PAINTING', null, 'Oils', '1', 'Mona Lisa'),
       ('PAINTING', null, 'Oils', '1', 'Madonna Litta'),
       ('PAINTING', null, 'Watercolor', '2', 'Female Saint'),
       ('PAINTING', null, 'Pencil', '2', 'Flora'),
       ('SCULPTURE', 'Bronze', null, '3', 'A man with a broken nose'),
       ('SCULPTURE', 'Stone', null, '3', 'Bronze Age'),
       ('SCULPTURE', 'Clay', null, '2', 'Stone Age Gold'),
       ('SCULPTURE', 'Bronze', null, '3', 'Clay Bronze Age');


INSERT INTO public.autor_exhibit(
    autor_id, exhibit_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (1, 3),
       (3, 4),
       (4, 5),
       (4, 6);


INSERT INTO public.employees(first_name, last_name, "position", login, password, audience_id)
VALUES
('Anna', 'Kentor', 'MANAGER', 'a_kentor', 'anna1230', null),
('Bogdan', 'Korty', 'AUDIENCE_MANAGER', 'Bogdan_Korty', '_Korty59', 1),
('Bill', 'Kell', 'AUDIENCE_MANAGER', 'Kell_007', 'Bond_007', 2),
('Jeck', 'Loper', 'AUDIENCE_MANAGER', 'loper_79', '123456qwe', 3),
('Tom', 'Vagik', 'TOUR_GUIDE', 'Vagik2005', '1Best', null),
('Angela', 'Sadik', 'TOUR_GUIDE', 'angel', 'IamAngel', null),
('Julia', 'Karenge', 'TOUR_GUIDE', 'Karenge_j', 'hello_world', null);


INSERT INTO public.excursion(name)
VALUES ('Golden Spring'),
       ('Da Vinci Demons'),
       ('Inferno'),
       ('Angels and Deamons');

INSERT INTO public.timetable(employee_id, excursion_id, date_start, date_end)
VALUES
(1, 1, '2019-07-02 12:10:00', '2019-07-02 13:10:00'),
(2, 2, '2019-07-02 12:10:00', '2019-07-02 13:10:00'),
(3, 3, '2019-07-02 12:10:00', '2019-07-02 13:10:00'),
(4, 4, '2019-07-03 14:10:00', '2019-07-03 15:10:00'),
(5, 1, '2019-07-03 14:30:00', '2019-07-03 15:30:00'),
(6, 2, '2019-07-03 14:30:00', '2019-07-03 15:30:00'),
(7, 3, '2019-07-04 19:30:00', '2019-07-04 20:30:00'),
(5, 4, '2019-07-05 17:30:00', '2019-07-05 18:30:00');

-- SELECT DISTINCT e.id AS employee_id, e.first_name AS employee_first_name, e.last_name AS employee_last_name,
--                         e.position AS employee_position, e.login AS employee_login, e.password AS employee_password
--                         FROM employees AS e
--                         WHERE e.id NOT IN(
--                         SELECT employee_id FROM timetable
--                         WHERE date_start BETWEEN '2019-07-02 17:00:00' AND '2019-07-03 17:00:00'
--                         OR date_end BETWEEN '2019-07-02 17:00:00' AND '2019-07-03 17:00:00');