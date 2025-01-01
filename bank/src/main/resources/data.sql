CREATE TABLE customers (
  id bigint NOT NULL AUTO_INCREMENT,
  email varchar(100) NOT NULL,
  name varchar(100) NOT NULL,
  age tinyint DEFAULT(18),
  PRIMARY KEY (id),
  UNIQUE (email)
);

CREATE TABLE accounts (
  id bigint NOT NULL AUTO_INCREMENT,
  balance double DEFAULT '0',
  currency enum('CHF','EUR','GBP','UAH','USD') NOT NULL,
  number varchar(45) NOT NULL,
  customer_id bigint NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (customer_id) REFERENCES customers (id)
);

CREATE TABLE employers (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(100) NOT NULL,
  address varchar(300) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE customer_employer (
  customer_id bigint DEFAULT NULL,
  employer_id bigint DEFAULT NULL
);

-- Insert sample customers
INSERT INTO customers (email, name, age)
VALUES
  ('john.doe@example.com', 'John Doe', 35),
  ('jane.smith@example.com', 'Jane Smith', 28),
  ('alex.brown@example.com', 'Alex Brown', 42);

-- Insert sample accounts
INSERT INTO accounts (balance, currency, number, customer_id)
VALUES
  (1000.50, 'USD', 'ACC12345', 1),
  (1500.00, 'EUR', 'ACC23456', 2),
  (200.75, 'UAH', 'ACC34567', 3),
  (2500.00, 'GBP', 'ACC45678', 1),
  (500.00, 'CHF', 'ACC56789', 2);

-- Insert sample employers
INSERT INTO employers (name, address)
VALUES
  ('Tech Corp', '123 Tech Street, San Francisco, CA'),
  ('Finance Inc', '456 Finance Avenue, New York, NY'),
  ('Health Plus', '789 Health Boulevard, Los Angeles, CA');

-- Insert sample customer-employer relationships
INSERT INTO customer_employer (customer_id, employer_id)
VALUES
  (1, 1), -- John Doe works for Tech Corp
  (2, 2), -- Jane Smith works for Finance Inc
  (3, 3); -- Alex Brown works for Health Plus
