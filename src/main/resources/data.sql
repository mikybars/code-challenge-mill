INSERT INTO brand (name) VALUES ('Zara');

INSERT INTO product (id, name, brand_id, description) VALUES
(35455, 'Cotton T-Shirt', 1, 'Classic cotton t-shirt in white');

INSERT INTO product_price (product_id, price_list_id, start_date, end_date, rank, amount, currency) VALUES
(35455, 1, '2020-06-14 00:00:00', '2020-12-31 23:59:59', 0, '35.50', 'EUR'),
(35455, 2, '2020-06-14 15:00:00', '2020-06-14 18:30:00', 1, '25.45', 'EUR'),
(35455, 3, '2020-06-15 00:00:00', '2020-06-15 11:00:00', 1, '30.50', 'EUR'),
(35455, 4, '2020-06-15 16:00:00', '2020-12-31 23:59:59', 1, '38.95', 'EUR');
