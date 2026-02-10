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