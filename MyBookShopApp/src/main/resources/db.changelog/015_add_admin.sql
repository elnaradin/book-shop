update users set name = 'admin',
                 password = '$2a$10$SinR3P7Upxr52SDAuowT4eDoQFq6eXzUTlhaUIxOFpdyDu9cktLHu',
                 balance = 0,
                 email = 'admin@mybookshop.ru'
where id = 1000;
insert into user2role (user_id, role_id) values (1000, 1);
insert into user2role (user_id, role_id) values (1000, 2);
