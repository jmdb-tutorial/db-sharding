CREATE TABLE customer (
    id bigserial primary key,
    full_name varchar
);


CREATE TABLE order (
    id bigserial primary key,
    customer_id bigint,
    total_amount varchar,
    ip_address varchar,
    postcode varchar,
    email_address varchar

);

CREATE TABLE order_item
(
    id bigserial primary key,
    order_id bigint
);
