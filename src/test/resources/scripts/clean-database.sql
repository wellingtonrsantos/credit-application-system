DROP TABLE IF EXISTS credit;

CREATE TABLE credit (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   credit_code UUID NOT NULL,
   credit_value DECIMAL NOT NULL,
   day_first_installment date NOT NULL,
   number_of_installments INT NOT NULL,
   status SMALLINT,
   customer_id BIGINT,
   CONSTRAINT pk_credit PRIMARY KEY (id)
);

ALTER TABLE credit ADD CONSTRAINT uc_credit_credit_code UNIQUE (credit_code);

ALTER TABLE credit ADD CONSTRAINT FK_CREDIT_ON_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id);

ALTER TABLE credit DROP CONSTRAINT FK_CREDIT_ON_CUSTOMER;

DROP TABLE IF EXISTS customer;

CREATE TABLE customer (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   first_name VARCHAR(255) NOT NULL,
   last_name VARCHAR(255) NOT NULL,
   cpf VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   zip_code VARCHAR(255) NOT NULL,
   street VARCHAR(255) NOT NULL,
   income DECIMAL NOT NULL,
   CONSTRAINT pk_customer PRIMARY KEY (id)
);

ALTER TABLE customer ADD CONSTRAINT uc_customer_cpf UNIQUE (cpf);

ALTER TABLE customer ADD CONSTRAINT uc_customer_email UNIQUE (email);
