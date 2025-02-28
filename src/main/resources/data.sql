-- Sample Products
INSERT INTO products (name, price, discountable) VALUES ('T-Shirt', 20.00, TRUE);
INSERT INTO products (name, price, discountable) VALUES ('Book', 15.00, FALSE);
INSERT INTO products (name, price, discountable) VALUES ('Jeans', 45.00, TRUE);
INSERT INTO products (name, price, discountable) VALUES ('Laptop', 1200.00, FALSE);

-- Sample Discounts
INSERT INTO discounts (code, percentage, valid_from, valid_to)
VALUES ('SUMMER2025', 20, '2025-06-01 00:00:00', '2025-08-31 23:59:59');

INSERT INTO discounts (code, percentage, valid_from, valid_to)
VALUES ('BLACKFRIDAY', 30, '2025-11-25 00:00:00', '2025-11-30 23:59:59');

-- Sample Carts
INSERT INTO carts (total_price, discount_code) VALUES (0.00, NULL);
INSERT INTO carts (total_price, discount_code) VALUES (0.00, NULL);

-- Sample Cart Items
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (1, 1, 2);  -- 2 T-Shirts
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (1, 2, 1);  -- 1 Book
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (2, 3, 3);  -- 3 Jeans
INSERT INTO cart_items (cart_id, product_id, quantity) VALUES (2, 4, 1);  -- 1 Laptop
