# Inventory Management System
A Java-based Inventory Management System using MySQL to efficiently track products, suppliers, and orders. This console-based application allows businesses to manage stock levels, suppliers, and sales, while generating alerts for low-stock items.

### Features

Product Management – Add, update, delete, list, and search products.

Supplier Management – Add and manage suppliers linked to products.

Order Management – Place orders and automatically update stock quantities.

Low-Stock Alerts – Display products below a defined stock threshold.

Reports – List all products, search by name, and view stock details.

### Technologies Used

Programming Language: Java (Core Java, JDBC)

Database: MySQL

JDBC Driver: MySQL Connector/J

### Installation & Setup

Clone the repository

Install Java JDK and MySQL.

### Create the database and tables in MySQL:

CREATE DATABASE inventorydb;
USE inventorydb;

CREATE TABLE suppliers (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    contact VARCHAR(100)
);

CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE NOT NULL,
    supplier_id INT,
    FOREIGN KEY (supplier_id) REFERENCES suppliers(id)
);

CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    quantity INT,
    total_price DOUBLE,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(id)
);


Place the MySQL Connector/J JAR in your project folder.

Update database credentials in InventoryManagementSystem.java if needed.

### How to Run

Compile:

javac -cp .;mysql-connector-j-9.4.0.jar InventoryManagementSystem.java

Run:

java -cp .;mysql-connector-j-9.4.0.jar InventoryManagementSystem

### Usage

Start the program, and a menu will appear for managing suppliers, products, orders, and stock alerts.

Follow on-screen instructions to perform CRUD operations and place orders.
