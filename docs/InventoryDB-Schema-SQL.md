```sql
-- Database
CREATE DATABASE IF NOT EXISTS InventoryDB;
USE InventoryDB;

-- Roles Table
CREATE TABLE roles (
role_id INT AUTO_INCREMENT PRIMARY KEY,
role_name VARCHAR(50) NOT NULL UNIQUE,
description TEXT
);

-- Users Table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(60) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role_id INT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE RESTRICT
);

-- Customers Table
CREATE TABLE customers (
customer_id INT AUTO_INCREMENT PRIMARY KEY,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
is_active BOOLEAN DEFAULT TRUE
);

-- Categories Table
CREATE TABLE categories (
category_id INT AUTO_INCREMENT PRIMARY KEY,
category_name VARCHAR(255) NOT NULL UNIQUE,
is_active BOOLEAN DEFAULT TRUE
);

-- Suppliers Table
CREATE TABLE suppliers (
supplier_id INT AUTO_INCREMENT PRIMARY KEY,
supplier_name VARCHAR(255) NOT NULL,
contact_name VARCHAR(255),
phone VARCHAR(20),
email VARCHAR(255),
address TEXT,
is_active BOOLEAN DEFAULT TRUE
);

-- Warehouses Table
CREATE TABLE warehouses (
warehouse_id INT AUTO_INCREMENT PRIMARY KEY,
warehouse_name VARCHAR(255) NOT NULL,
location TEXT,
is_active BOOLEAN DEFAULT TRUE
);

-- Products Table
CREATE TABLE products (
product_id INT AUTO_INCREMENT PRIMARY KEY,
product_name VARCHAR(255) NOT NULL,
description TEXT,
unit_of_measure VARCHAR(50),
category_id INT,
supplier_id INT,
reorder_level INT DEFAULT 0,
unit_price DECIMAL(10, 2),
image_path VARCHAR(255),
is_active BOOLEAN DEFAULT TRUE,
FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL,
FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE SET NULL
);

-- Inventory Table
CREATE TABLE inventory (
inventory_id INT AUTO_INCREMENT PRIMARY KEY,
product_id INT,
warehouse_id INT,
quantity INT NOT NULL,
FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE SET NULL,
FOREIGN KEY (warehouse_id) REFERENCES warehouses(warehouse_id) ON DELETE SET NULL
);

-- Purchase Orders Table
CREATE TABLE purchase_orders (
purchase_order_id INT AUTO_INCREMENT PRIMARY KEY,
supplier_id INT,
order_date DATE NOT NULL,
expected_arrival_date DATE,
status ENUM('Pending', 'Ordered', 'Received', 'Cancelled') DEFAULT 'Pending',
FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE SET NULL
);

-- Purchase Order Items Table
CREATE TABLE purchase_order_items (
purchase_order_item_id INT AUTO_INCREMENT PRIMARY KEY,
purchase_order_id INT NOT NULL,
product_id INT NOT NULL,
quantity INT NOT NULL,
unit_price DECIMAL(10, 2),
FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(purchase_order_id) ON DELETE CASCADE,
FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT
);

-- Sales Orders Table
CREATE TABLE sales_orders (
sales_order_id INT AUTO_INCREMENT PRIMARY KEY,
customer_id INT,
order_date DATE NOT NULL,
status ENUM('Pending', 'Processed', 'Shipped', 'Delivered', 'Cancelled') DEFAULT 'Pending',
FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE SET NULL
);

-- Sales Order Items Table
CREATE TABLE sales_order_items (
sales_order_item_id INT AUTO_INCREMENT PRIMARY KEY,
sales_order_id INT NOT NULL,
product_id INT NOT NULL,
quantity INT NOT NULL,
unit_price DECIMAL(10, 2),
FOREIGN KEY (sales_order_id) REFERENCES sales_orders(sales_order_id) ON DELETE CASCADE,
FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT
);

-- Inserting initial roles into the roles table
INSERT INTO roles (role_name, description) VALUES
('Moderator', 'Responsible for managing user content and maintaining community standards'),
('Admin', 'Has full access to all system features and settings'),
('Manager', 'Oversees operations within a specific department or team'),
('Staff', 'Regular staff member with limited administrative privileges'),
('Viewer', 'Can view content but has no editing or administrative access');
```
