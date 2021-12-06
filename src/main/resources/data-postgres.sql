--user roles
insert into ROLE (name) values ('ROLE_ADMINISTRATOR');
insert into ROLE (name) values ('ROLE_DIRECTOR');
insert into ROLE (name) values ('ROLE_MANAGER');
insert into ROLE (name) values ('ROLE_WAITER');
insert into ROLE (name) values ('ROLE_COOK');
insert into ROLE (name) values ('ROLE_BARTENDER');
insert into ROLE (name) values ('ROLE_CHEF');

--admin
insert into users
(role_id, username, email, type, password, first_name, last_name, address, phone_number, image_path, isDeleted, enabled)
values
(1, 'admin', 'admin@maildrop.cc', 0,
'$2a$10$puaZa6SuasjiagmJJi6Dtecz7cxA3HuXJUzcqlana.SO.U22uXOJq', 'Isidora', 'Stanic', 'Ulica 1', '123456789',
'https://images.generated.photos/D3P-rBhbbbYX9Bg2gB9GE4Yl3_DHXbM4AE-YbNdL1jI/rs:fit:512:512/wm:0.95:sowe:18:18:0.33/czM6Ly9pY29uczgu/Z3Bob3Rvcy1wcm9k/LmNvbmQvMTcwN2Ew/NjYtY2JhZC00YTc4/LTg1ODktNDcwZDgx/MmJhOWQ0LmpwZw.jpg',
 false, true);