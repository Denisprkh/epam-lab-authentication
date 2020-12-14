INSERT INTO tag(name) VALUES ('sport'), ('food'), ('relax');

INSERT INTO gift_certificate(name, description, price, create_date, last_update_date, duration)
VALUES ('Gym', 'Simple description', 10.00, '2020-11-04 22:18:24', '2020-11-04 22:18:24', 30),
       ('Provenance', 'Just try it', 30.00, '2020-11-04 22:18:24', '2020-11-04 22:18:24' , 30),
       ('Robinson spa', 'Luxury relax club', 70, '2020-10-25 22:18:24', '2020-11-04 22:18:24', 30);

INSERT INTO gift_certificate_tag(tag_id,gift_certificate_id)
VALUES (1, 2),
       (3, 2),
       (1, 3),
       (2, 1);

INSERT INTO user_role (name) VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO user (login, password, user_role_id) VALUES
        ('john@gmail.com','5c7d0c90cf9e0ce560956179e8e82e7d', 1),
        ('helen54@mail.ru','7d9b6e2e6e712c6abe59f4a84b4b2ce1', 1);

INSERT INTO purchase(cost, purchase_date, user_id) VALUES
        ('100', '2020-11-22 18:17:54', 1),
        ('40', '2020-11-22 18:17:54', 2);

INSERT INTO purchase_gift_certificate(purchase_id, gift_certificate_id) VALUES
        (1, 2),
        (1, 3),
        (2, 1),
        (2, 2);