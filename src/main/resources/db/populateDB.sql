DELETE FROM user_roles;
DELETE FROM eats;
DELETE FROM VOICES;
DELETE FROM restaurants;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('user1', 'user1@yandex.ru', '{noop}password'),
  ('user2', 'user2@yandex.ru', '{noop}password'),
  ('admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_USER', 100001),
  ('ROLE_ADMIN', 100002);

INSERT INTO restaurants (name) VALUES
  ('Палкин'),
  ('Россия'),
  ('Тройка'),
  ('Сытинъ');

INSERT INTO eats (date_time, name, price, restaurant_id) VALUES
  ('2018-03-24', 'Пельмени', 500, 100003),
  ('2018-03-24', 'Голубцы', 700, 100003),
  ('2018-03-24', 'Сок', 100, 100003),
  ('2018-03-24', 'Суп из краба', 1200, 100004),
  ('2018-03-24', 'Жаркое из кенгуру', 1500, 100004),
  ('2018-03-24', 'Мясо', 800, 100005),
  ('2018-03-24', 'Гарнир', 400, 100005),
  ('2018-03-24', 'Фруктовый салат', 700, 100005),
  ('2018-03-24', 'Шницель', 400, 100005),
  ('2018-03-24', 'Шашлык', 600, 100006),
  ('2018-03-24', 'Фондю', 1100, 100006),
  ('2018-03-25', 'Драники', 350, 100003);

INSERT INTO VOICES (USER_ID, RESTAURANT_ID, DATE) VALUES
  (100000,100003,'2018-03-24'),
  (100001,100004,'2018-03-24'),
  (100002,100003,'2018-03-24'),
  (100000,100003,'2018-03-25'),
  (100001,100004,'2018-03-25');