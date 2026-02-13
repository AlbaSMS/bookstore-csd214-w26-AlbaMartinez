CREATE TABLE IF NOT EXISTS electronics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255),
    type VARCHAR(50),
    screenSizeInches DOUBLE,
    warrantyMonths DOUBLE,
    supports5G BIT,
    price DOUBLE,
    copies INT
);

CREATE TABLE IF NOT EXISTS laptops (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255),
    screenSizeInches DOUBLE,
    warrantyMonths DOUBLE
);

CREATE TABLE IF NOT EXISTS phones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255),
    supports5G BIT,
    price DOUBLE,
    copies INT
    );

CREATE TABLE IF NOT EXISTS stationery (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255),
    type VARCHAR(50),
    price DOUBLE,
    color VARCHAR(255),
    pageCount INT
);

CREATE TABLE IF NOT EXISTS pens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255),
    color VARCHAR(255),
    price DOUBLE
);

CREATE TABLE IF NOT EXISTS notebooks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    brand VARCHAR(255),
    pageCount INT,
    price DOUBLE
);