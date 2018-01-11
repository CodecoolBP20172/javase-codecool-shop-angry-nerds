ALTER TABLE IF EXISTS ONLY public.cart DROP CONSTRAINT IF EXISTS pk_cart_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.user_data DROP CONSTRAINT IF EXISTS pk_user_data_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.order DROP CONSTRAINT IF EXISTS pk_order_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.order DROP CONSTRAINT IF EXISTS fk_user_data_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.order DROP CONSTRAINT IF EXISTS fk_cart_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.product DROP CONSTRAINT IF EXISTS pk_product_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.product DROP CONSTRAINT IF EXISTS fk_product_category_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.product DROP CONSTRAINT IF EXISTS fk_supplier_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.product_category DROP CONSTRAINT IF EXISTS pk_product_category_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.supplier DROP CONSTRAINT IF EXISTS pk_supplier_id CASCADE;

DROP TABLE IF EXISTS public.cart;
DROP SEQUENCE IF EXISTS public.question_id_seq
CREATE TABLE cart (
    id serial NOT NULL,
    product_list text NOT NULL
);

DROP TABLE IF EXISTS public.user_data;
DROP SEQUENCE IF EXISTS public.user_data_id_seq;
CREATE TABLE user_data (
    id serial NOT NULL,
    name text, 
    email text, 
    phone_number text,
    billing_address text,
    billing_city text, 
    billing_zipcode text, 
    billing_country text,
    shipping_address text, 
    shipping_city text,
    shipping_zipcode text,
    shipping_country text
);

DROP TABLE IF EXISTS public.order;
DROP SEQUENCE IF EXISTS public.order_id_seq;
CREATE TABLE order (
    id serial NOT NULL,
    user_data_id integer,
    cart_id integer
);


DROP TABLE IF EXISTS public.product;
DROP SEQUENCE IF EXISTS public.product_id_seq;
CREATE TABLE product (
    id serial NOT NULL,
    name text,
    default_price float(1),
    default_currency integer,
    product_category_id integer,
    supplier_id integer
);

DROP TABLE IF EXISTS public.product_category;
DROP SEQUENCE IF EXISTS public.product_category_id_seq;
CREATE TABLE product_category (
    id serial NOT NULL,
    name text,
    description text,
    department text
);

DROP TABLE IF EXISTS public.supplier;
DROP SEQUENCE IF EXISTS public.supplier_id_seq;
CREATE TABLE supplier (
    id serial NOT NULL,
    name text,
    description text
);


ALTER TABLE ONLY cart
    ADD CONSTRAINT pk_cart_id PRIMARY KEY (id);

ALTER TABLE ONLY user_data
    ADD CONSTRAINT pk_user_data_id PRIMARY KEY (id);

ALTER TABLE ONLY order
    ADD CONSTRAINT pk_order_id PRIMARY KEY (id);

ALTER TABLE ONLY product
    ADD CONSTRAINT pk_product_id PRIMARY KEY (id);

ALTER TABLE ONLY product_category
    ADD CONSTRAINT pk_product_category_id PRIMARY KEY (id);

ALTER TABLE ONLY supplier
    ADD CONSTRAINT pk_supplier_id PRIMARY KEY (id);


ALTER TABLE ONLY product
    ADD CONSTRAINT fk_product_category_id FOREIGN KEY (product_category_id) REFERENCES product_category(id);

ALTER TABLE ONLY product
    ADD CONSTRAINT fk_supplier_id FOREIGN KEY (supplier_id) REFERENCES supplier(id);

ALTER TABLE ONLY order
    ADD CONSTRAINT fk_user_data_id FOREIGN KEY (user_data_id) REFERENCES user_data(id);

ALTER TABLE ONLY order
    ADD CONSTRAINT fk_cart_id FOREIGN KEY (cart_id) REFERENCES cart(id);


INSERT INTO supplier VALUES(1, "Amazon", "Digital content and services");
INSERT INTO supplier VALUES(2, "Lenovo", "Computers");
INSERT INTO supplier VALUES(3, "Apple", "Computers");
SELECT pg_catalog.setval('supplier_id_seq', 3, true);

INSERT INTO product_category VALUES (1, "Notebook", "A portable computer.", "Hardware");
INSERT INTO product_category VALUES (2, "Tablet", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.", "Hardware");
INSERT INTO product_category VALUES (3, "Audio", "bla bla", "Hardware");
INSERT INTO product_category VALUES (4, "Electronics", "bla bla", "Hardware");
SELECT pg_catalog.setval('product_category_id_seq', 4, true);

INSERT INTO product VALUES (1, "Amazon Fire", 49.9, "USD",  "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", 2, 1);
INSERT INTO answer VALUES (2, "Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", 2, 2);
INSERT INTO answer VALUES (3, "Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", 2, 1);
SELECT pg_catalog.setval('product_id_seq', 3, true);

INSERT INTO cart VALUES(1, "1,1,2,3")
SELECT pg_catalog.setval('cart_id_seq', 1, true);

INSERT INTO user_data VALUES(1, "Gipsz Jakab", "testemail@gmail.com", "303377027", "Kőbányai utca", "Budakalász", "2011", "Hungary", "Déryné utca", "Gödöllő", "2100", "Hungary");
SELECT pg_catalog.setval('user_data_id_seq', 1, true);

INSERT INTO order VALUES(1, 1, 1);
SELECT pg_catalog.setval('order_id_seq', 1, true);
