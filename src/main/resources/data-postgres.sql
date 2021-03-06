/* PREGLED BAZE IZ TERMINALA:
   sudo -i -u postgres
   psql -d jpa
   \dt  //izlista sve tabele prisutne u bazi
 */


insert into ROLE (name) values ('ROLE_ADMINISTRATOR');
insert into ROLE (name) values ('ROLE_DIRECTOR');
insert into ROLE (name) values ('ROLE_MANAGER');
insert into ROLE (name) values ('ROLE_WAITER');
insert into ROLE (name) values ('ROLE_COOK');
insert into ROLE (name) values ('ROLE_BARTENDER');
insert into ROLE (name) values ('ROLE_CHEF');

--admin
insert into users
(username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
values
    ('admin', 'admin@maildrop.cc', 'ADMINISTRATOR',
     '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
     'Isidora', 'Stanic', 'Ulica 1', '123456789',
     'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
     false, true);

insert into users
(username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
values
    ('managerko', 'managerko@maildrop.cc', 'MANAGER',
     '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
     'Manager', 'Manageric', 'Ulica 2', '123456789',
     'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
     false, true);

insert into users
(username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
values
    ('kuvarko', 'cook1@maildrop.cc', 'COOK',
     '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
     'Kuvarko', 'Kukic', 'Ulica 3', '123456789',
     'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
     false, true);

insert into users
(username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
values
    ('Kuki', 'cook2@maildrop.cc', 'COOK',
     '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
     'Kuki', 'Mukic', 'Ulica 3a', '123456789',
     'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
     false, true);

insert into users
(username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
values
    ('potrcko', 'trcko1@maildrop.cc', 'WAITER',
     '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
     'Konobarko', 'Konobic', 'Ulica 4', '123456789',
     'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
     false, true);

insert into users
(username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
values
    ('Trcika', 'trcko2@maildrop.cc', 'WAITER',
     '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
     'Konobarka', 'Konobarevicc', 'Ulica 5', '123456789',
     'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
     false, true);

insert into users
(username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
values
    ('Benko', 'trcko3@maildrop.cc', 'WAITER',
     '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
     'Benko', 'Nondic', 'Ulica 6', '123456789',
     'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
     false, true);

insert into users
(username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
values
    ('Gonko', 'sankeru.budi.drug@maildrop.cc', 'BARTENDER',
     '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
     'Gonko', 'Sljiko', 'Ulica 7', '123456789',
     'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
     false, true);

--manager
insert into users
(username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
values
    ('manager', 'manager@maildrop.cc', 'MANAGER',
     '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
     'Menadzer', 'Menadzment', 'Ulica 1', '123456789',
     'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
     false, true);

--director
insert into users
(username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
values
 ('director', 'director@maildrop.cc', 'DIRECTOR',
  '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
  'Matija', 'Matovic', 'Ulica 1', '123456789',
  'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
  false, true);

  --chef
  insert into users
  (username, email, type, password, first_name, last_name, address, phone_number, image_path, is_deleted, enabled)
  values
   ('chef', 'chef@maildrop.cc', 'CHEF',
    '$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq',
    'Jovana', 'Klimenta', 'Ulica 3', '123456798',
    'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
    false, true);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (100, '2021-08-30', 1, false);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (100, '2021-08-30', 2, false);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (100, '2021-08-30', 3, false);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (100, '2021-08-10', 4, false);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (100, '2021-08-10', 5, false);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (100, '2021-08-10', 6, false);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (100, '2021-08-19', 7, false);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (100, '2021-08-19', 8, false);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (100, '2021-08-19', 9, false);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (300, '2021-08-19', 10, false);

insert into user_salary
(salary, salary_date, user_id, is_deleted)
values (100, '2021-08-19', 11, false);


--USER ROLE
insert into user_role
(user_id, role_id)
values (1, 1);

insert into user_role
(user_id, role_id)
values (2, 3);

insert into user_role
(user_id, role_id)
values (3, 5);

insert into user_role
(user_id, role_id)
values (4, 5);

insert into user_role
(user_id, role_id)
values (5, 4);

insert into user_role
(user_id, role_id)
values (6, 4);

insert into user_role
(user_id, role_id)
values (7, 4);

insert into user_role
(user_id, role_id)
values (8, 6);

insert into user_role
(user_id, role_id)
values (9, 3);

insert into user_role
(user_id, role_id)
values (10, 2);

insert into user_role
(user_id, role_id)
values (11, 7);



--SITTING TABLES
insert into sitting_table
(name, x, y, is_deleted)
values
    ('Sto 1', 1, 1, false);

insert into sitting_table
(name, x, y, is_deleted)
values
    ('Sto 2', 1, 2, false);



--DISH
insert into dish
(code, name, allergens, ingredients, recipe, preparation_price, description, image_path, preparation_time, category, is_deleted)
values
    ('D1', 'Burek', 'None', 'Kore, meso, luk', 'Pomesaj i peci', 100, 'Dobro ali masno', 'https://www.recepti.com/img/recipe/32602-preliveni-burek-s-mesom_zoom.jpg', 30, 'MAIN_COURSE', false);

insert into dish
(code, name, allergens, ingredients, recipe, preparation_price, description, image_path, preparation_time, category, is_deleted)
values
    ('D2', 'Pita', 'None', 'Kore, sir, luk', 'Pomesaj i peci', 100, 'Dobro ali masno', 'https://staticstvarukusa.mondo.rs/Picture/22542/png/Mekana-pita-sa-sirom-spremna-za-pola-sata-VIDEO', 30, 'MAIN_COURSE', false);

insert into dish
(code, name, allergens, ingredients, recipe, preparation_price, description, image_path, preparation_time, category, is_deleted)
values
    ('D3', 'Kroasan', 'None', 'Testo, krem, secer', 'Pomesaj i peci', 100, 'Dobro ali secer', 'https://i1.wp.com/www.soibiberblog.com/wp-content/uploads/2019/04/Doma%C4%87i-kroasani-10-1.jpg?resize=665%2C444', 20, 'DESSERT', false);

insert into dish
(code, name, allergens, ingredients, recipe, preparation_price, description, image_path, preparation_time, category, is_deleted)
values
    ('D4', 'Rol virsla', 'None', 'Rol, Virsla', 'Pomesaj i peci', 100, 'Dobro ali hemija', 'https://www.recepti.com/img/recipe/17107-rol-virsle.jpg', 30, 'BREAKFAST', false);

insert into dish
(code, name, allergens, ingredients, recipe, preparation_price, description, image_path, preparation_time, category, is_deleted)
values
    ('D5', 'Zu-Zu', 'None', 'Kore, pecivo', 'Pomesaj i peci', 100, 'Dobro bezukusno', 'https://podravkaiovariations.azureedge.net/ad2b314c-6110-11eb-bc64-0242ac12002c/v/f2b1f6a6-64bc-11eb-b6c2-0242ac130010/1024x768-f2b21802-64bc-11eb-a115-0242ac130010.webp', 15, 'APPETIZER', false);

insert into dish
(code, name, allergens, ingredients, recipe, preparation_price, description, image_path, preparation_time, category, is_deleted)
values
    ('D6', 'Riblja corba', 'None', 'Riba, voda, paradajiz', 'Pomesaj i kuvaj', 100, 'Dobro ali zdravo', 'https://i.ytimg.com/vi/y2TKsRY3YJI/maxresdefault.jpg', 40, 'SOUP', false);

insert into dish
(code, name, allergens, ingredients, recipe, preparation_price, description, image_path, preparation_time, category, is_deleted)
values
    ('D7', 'Burek sa sirom', 'None', 'Kore, sir, mast', 'Pomesaj i peci', 100, 'Dobro ali masno', 'https://podravkaiovariations.azureedge.net/d3d932e4-6408-11eb-a3c6-0242ac12001a/v/f2b1f6a6-64bc-11eb-b6c2-0242ac130010/1024x768-f2b21802-64bc-11eb-a115-0242ac130010.webp', 30, 'MAIN_COURSE', false);




--DRINK
insert into drink
(code, name, category, allergens, ingredients, purchase_price, description, image_path, is_deleted)
values
    ('DR1', 'Kokakola', 'NON_ALCOHOLIC', 'None', 'Poslovna tajna', 55, 'TOO BATO KOKAKOLA', 'https://www.dnevno.rs/wp-content/uploads/2018/06/14776-koka-kola-sok.jpg', false);

insert into drink
(code, name, category, allergens, ingredients, purchase_price, description, image_path, is_deleted)
values
    ('DR2', 'Caj', 'NON_ALCOHOLIC', 'None', 'Voda i trava', 55, 'Zdravo', 'https://www.magazin.novosti.rs/upload/Article/Image/2021_10/1635514273_tea-g38726a8c1_1920.jpg', false);

insert into drink
(code, name, category, allergens, ingredients, purchase_price, description, image_path, is_deleted)
values
    ('DR3', 'Rakija', 'ALCOHOLIC', 'None', 'SLJIVA', 55, 'UDRI', 'https://www.espreso.co.rs/data/images/2018/01/08/12/314701_rakija-domaca_share.jpg', false);


insert into drink
(code, name, category, allergens, ingredients, purchase_price, description, image_path, is_deleted)
values
    ('DR4', 'Domaca kafa', 'HOT_BEVERAGE', 'None', 'samlevena zrna kafe', 55, 'Za razbudjivanje', 'https://www.korpa-deli-market.com/media/2021/01/Korpa-deli-kafa-domaca-2.jpg', false);




--DISH PRICES
insert into dish_price
(price, price_date, dish_id, is_deleted)
values
    (150, '2021-12-06', 1, false);

insert into dish_price
(price, price_date, dish_id, is_deleted)
values
    (140, '2021-12-06', 2, false);

insert into dish_price
(price, price_date, dish_id, is_deleted)
values
    (130, '2021-12-06', 3, false);

insert into dish_price
(price, price_date, dish_id, is_deleted)
values
    (120, '2021-12-06', 4, false);

insert into dish_price
(price, price_date, dish_id, is_deleted)
values
    (150, '2021-12-06', 5, false);

insert into dish_price
(price, price_date, dish_id, is_deleted)
values
    (160, '2021-12-06', 6, false);

--food menus
-- insert into food_menu (date, is_deleted) values ('2021-11-06', false);
-- insert into food_menu (date, is_deleted) values ('2021-12-06', false);
--
-- insert into food_menu_prices (menu_id, price_id) values (1, 4);
--
-- insert into food_menu_prices (menu_id, price_id) values (2, 1);
-- insert into food_menu_prices (menu_id, price_id) values (2, 2);
-- insert into food_menu_prices (menu_id, price_id) values (2, 3);
-- insert into food_menu_prices (menu_id, price_id) values (2, 4);
-- insert into food_menu_prices (menu_id, price_id) values (2, 5);
-- insert into food_menu_prices (menu_id, price_id) values (2, 6);

--DRINK PRICES
insert into drink_price
(price, price_date, drink_id, is_deleted)
values
    (150, '2021-12-06', 1, false);

insert into drink_price
(price, price_date, drink_id, is_deleted)
values
    (140, '2021-12-06', 2, false);

insert into drink_price
(price, price_date, drink_id, is_deleted)
values
    (130, '2021-12-06', 3, false);



--DISH ORDER ITEMS
insert into dish_order_item
(status, note, amount, priority, dish_price_id, cook_id, is_deleted)
values
    ('CREATED', 'Nek bude zapeceno', 2, 1, 1, 3, false);

insert into dish_order_item
(status, note, amount, priority, dish_price_id, cook_id, is_deleted)
values
    ('CREATED', 'Nek bude zapeceno', 2, 1, 2, 3, false);

insert into dish_order_item
(status, note, amount, priority, dish_price_id, cook_id, is_deleted)
values
    ('CREATED', 'Nek bude zapeceno', 2, 1, 3, 3, false);

insert into dish_order_item
(status, note, amount, priority, dish_price_id, cook_id, is_deleted)
values
    ('IN_PROGRESS', 'Nek bude ukusno', 2, 1, 4, 4, false);

insert into dish_order_item
(status, note, amount, priority, dish_price_id, cook_id, is_deleted)
values
    ('READY', 'Nek bude hrskavo', 2, 1, 5, 4, false);

insert into dish_order_item
(status, note, amount, priority, dish_price_id, cook_id, is_deleted)
values
    ('DELIVERED', 'Nek bude ljuto i zacinjeno', 2, 1, 6, 4, false);

--drink menus
-- insert into drink_menu (date, is_deleted) values ('2021-11-06', false);
-- insert into drink_menu (date, is_deleted) values ('2021-12-06', false);
--
-- insert into drink_menu_prices (menu_id, price_id) values (1, 1);
--
-- insert into drink_menu_prices (menu_id, price_id) values (2, 1);
-- insert into drink_menu_prices (menu_id, price_id) values (2, 2);
-- insert into drink_menu_prices (menu_id, price_id) values (2, 3);

--DRINK ORDER ITEMS
insert into drink_order_item
(status, note, amount, drink_price_id, bartender_id, is_deleted)
values
    ('CREATED', 'Nista', 2, 1, 8, false);

insert into drink_order_item
(status, note, amount, drink_price_id, bartender_id, is_deleted)
values
    ('READY', 'Nista', 2, 2, 8, false);

insert into drink_order_item
(status, note, amount, drink_price_id, bartender_id, is_deleted)
values
    ('READY', 'Nista', 3, 3, 8, false);


--ORDERS
insert into orders
(status, creation_date_time, note, table_id, waiter_id, is_deleted)
values
    ('NOT_FINISHED', '2022-01-06 18:00', 'Brzobrzo', 1, 5, false);

insert into orders
(status, creation_date_time, note, table_id, waiter_id, is_deleted)
values
    ('NOT_FINISHED', '2022-01-06 17:55', 'Donesi vruce', 2, 6, false);

--ORDER dishes
insert into order_dishes
(order_id, item_id)
values
    (1, 1);

insert into order_dishes
(order_id, item_id)
values
    (1, 2);

insert into order_dishes
(order_id, item_id)
values
    (1, 3);

insert into order_dishes
(order_id, item_id)
values
    (2, 4);

insert into order_dishes
(order_id, item_id)
values
    (2, 5);

insert into order_dishes
(order_id, item_id)
values
    (2, 6);


--ORDER drinks
insert into order_drinks
(order_id, item_id)
    values(1, 1);

insert into order_drinks
(order_id, item_id)
values(2, 2);

insert into order_drinks
(order_id, item_id)
values(2, 3);


--food menus
insert into food_menu (date, is_deleted) values ('2021-11-06', false);
insert into food_menu (date, is_deleted) values ('2021-12-06', false);

insert into food_menu_prices (menu_id, price_id) values (1, 4);
insert into food_menu_prices (menu_id, price_id) values (1, 5);
insert into food_menu_prices (menu_id, price_id) values (1, 6);

insert into food_menu_prices (menu_id, price_id) values (2, 4);
insert into food_menu_prices (menu_id, price_id) values (2, 5);
insert into food_menu_prices (menu_id, price_id) values (2, 6);


--drink menus
insert into drink_menu (date, is_deleted) values ('2021-11-06', false);
insert into drink_menu (date, is_deleted) values ('2021-12-06', false);

insert into drink_menu_prices (menu_id, price_id) values (1, 1);
insert into drink_menu_prices (menu_id, price_id) values (1, 2);

insert into drink_menu_prices (menu_id, price_id) values (2, 1);
insert into drink_menu_prices (menu_id, price_id) values (2, 2);