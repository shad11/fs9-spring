CREATE DATABASE bank;

USE bank;

CREATE TABLE customers (
  id bigint NOT NULL AUTO_INCREMENT,
  email varchar(100) NOT NULL,
  name varchar(100) NOT NULL,
  age tinyint DEFAULT(18),
  PRIMARY KEY (id),
  UNIQUE KEY email_UNIQUE (email)
);


CREATE TABLE accounts (
  id bigint NOT NULL AUTO_INCREMENT,
  balance double DEFAULT '0',
  currency enum('CHF','EUR','GBP','UAH','USD') NOT NULL,
  number varchar(45) NOT NULL,
  customer_id bigint NOT NULL,
  PRIMARY KEY (id),
  KEY FK_CustomerAccount (customer_id),
  CONSTRAINT FK_CustomerAccount FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE TABLE employers (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  address varchar(300) NOT NULL,
  PRIMARY KEY (id)
);

SET SESSION sql_require_primary_key = 0;

CREATE TABLE customer_employer (
  customer_id bigint DEFAULT NULL,
  employer_id bigint DEFAULT NULL
);