DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS discounts;
DROP TABLE IF EXISTS products;

CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          price DECIMAL(10,2) NOT NULL,
                          discountable BOOLEAN NOT NULL
);

CREATE TABLE discounts (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           code VARCHAR(50) UNIQUE NOT NULL,
                           percentage INT NOT NULL,
                           valid_from TIMESTAMP NOT NULL,
                           valid_to TIMESTAMP NOT NULL
);

CREATE TABLE carts (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       total_price DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                       discount_code VARCHAR(50) NULL
);

CREATE TABLE cart_items (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            cart_id BIGINT NOT NULL,
                            product_id BIGINT NOT NULL,
                            quantity INT NOT NULL,
                            FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
                            FOREIGN KEY (product_id) REFERENCES products(id)
);
