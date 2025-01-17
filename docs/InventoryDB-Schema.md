# InventoryDB Schema Documentation
## Overview
This documentation provides a comprehensive guide to the schema design for the `InventoryDB` database used in small business operations. The schema focuses on data integrity, scalability, and flexibility while preserving historical data.

---

## Database Structure

### Tables

#### 1. Roles Table
- **Table Name**: `roles`
- **Columns**:
    - `role_id` (INT, Primary Key, Auto-Increment)
    - `role_name` (VARCHAR(50), Not Null, Unique)
    - `description` (TEXT)

---

#### 2. Users Table
- **Table Name**: `users`
- **Columns**:
    - `user_id` (INT, Primary Key, Auto-Increment)
    - `username` (VARCHAR(50), Not Null, Unique)
    - `password_hash` (VARCHAR(255), Not Null)
    - `role_id` (INT, Foreign Key referencing `roles(role_id)`)
    - `is_active` (BOOLEAN, Default TRUE)

---

#### 3. Customers Table
- **Table Name**: `customers`
- **Columns**:
    - `customer_id` (INT, Primary Key, Auto-Increment)
    - `first_name` (VARCHAR(50), Not Null)
    - `last_name` (VARCHAR(50), Not Null)
    - `is_active` (BOOLEAN, Default TRUE)

---

#### 4. Products Table
- **Table Name**: `products`
- **Columns**:
    - `product_id` (INT, Primary Key, Auto-Increment)
    - `product_name` (VARCHAR(255), Not Null)
    - `description` (TEXT)
    - `unit_of_measure` (VARCHAR(50))
    - `category_id` (INT, Foreign Key referencing `categories(category_id)` ON DELETE SET NULL)
    - `supplier_id` (INT, Foreign Key referencing `suppliers(supplier_id)` ON DELETE SET NULL)
    - `reorder_level` (INT, Default 0)
    - `unit_price` (DECIMAL(10, 2))
    - `image_path` (VARCHAR(255))
    - `is_active` (BOOLEAN, Default TRUE)

---

#### 5. Categories Table
- **Table Name**: `categories`
- **Columns**:
    - `category_id` (INT, Primary Key, Auto-Increment)
    - `category_name` (VARCHAR(255), Not Null, Unique)
    - `is_active` (BOOLEAN, Default TRUE)

---

#### 6. Suppliers Table
- **Table Name**: `suppliers`
- **Columns**:
    - `supplier_id` (INT, Primary Key, Auto-Increment)
    - `supplier_name` (VARCHAR(255), Not Null)
    - `contact_name` (VARCHAR(255))
    - `phone` (VARCHAR(20))
    - `email` (VARCHAR(255))
    - `address` (TEXT)
    - `is_active` (BOOLEAN, Default TRUE)

---

#### 7. Inventory Table
- **Table Name**: `inventory`
- **Columns**:
    - `inventory_id` (INT, Primary Key, Auto-Increment)
    - `product_id` (INT, Foreign Key referencing `products(product_id)` ON DELETE SET NULL)
    - `warehouse_id` (INT, Foreign Key referencing `warehouses(warehouse_id)` ON DELETE SET NULL)
    - `quantity` (INT, Not Null)

---

#### 8. Warehouses Table
- **Table Name**: `warehouses`
- **Columns**:
    - `warehouse_id` (INT, Primary Key, Auto-Increment)
    - `warehouse_name` (VARCHAR(255), Not Null)
    - `location` (TEXT)
    - `is_active` (BOOLEAN, Default TRUE)

---

#### 9. Purchase Orders Table
- **Table Name**: `purchase_orders`
- **Columns**:
    - `purchase_order_id` (INT, Primary Key, Auto-Increment)
    - `supplier_id` (INT, Foreign Key referencing `suppliers(supplier_id)` ON DELETE SET NULL)
    - `order_date` (DATE, Not Null)
    - `expected_arrival_date` (DATE)
    - `status` (ENUM('Pending', 'Ordered', 'Received', 'Cancelled'), Default 'Pending')

---

#### 10. Purchase Order Items Table
- **Table Name**: `purchase_order_items`
- **Columns**:
    - `purchase_order_item_id` (INT, Primary Key, Auto-Increment)
    - `purchase_order_id` (INT, Foreign Key referencing `purchase_orders(purchase_order_id)` ON DELETE CASCADE)
    - `product_id` (INT, Foreign Key referencing `products(product_id)` ON DELETE RESTRICT)
    - `quantity` (INT, Not Null)
    - `unit_price` (DECIMAL(10, 2))

---

#### 11. Sales Orders Table
- **Table Name**: `sales_orders`
- **Columns**:
    - `sales_order_id` (INT, Primary Key, Auto-Increment)
    - `customer_id` (INT, Foreign Key referencing `customers(customer_id)` ON DELETE SET NULL)
    - `order_date` (DATE, Not Null)
    - `status` (ENUM('Pending', 'Processed', 'Shipped', 'Delivered', 'Cancelled'), Default 'Pending')

---

#### 12. Sales Order Items Table
- **Table Name**: `sales_order_items`
- **Columns**:
    - `sales_order_item_id` (INT, Primary Key, Auto-Increment)
    - `sales_order_id` (INT, Foreign Key referencing `sales_orders(sales_order_id)` ON DELETE CASCADE)
    - `product_id` (INT, Foreign Key referencing `products(product_id)` ON DELETE RESTRICT)
    - `quantity` (INT, Not Null)
    - `unit_price` (DECIMAL(10, 2))

---

## Relationships

- **One-to-Many Relationships**:
    - `Products` to `Categories`
    - `Products` to `Suppliers`
    - `Inventory` to `Products` and `Warehouses`
    - `PurchaseOrders` to `Suppliers`
    - `SalesOrders` to `Customers`

- **Foreign Key Constraints**:
    - `SET NULL` used where historical data preservation is important.
    - `CASCADE` used in cases where related data must be deleted when the parent data is removed.

---

## Additions in the Schema:
1. **Soft Deletes (`is_active` Column):**
- Soft delete functionality was added by including an `is_active` column in major tables like `products`, `suppliers`, `categories`, and others to allow logical deletion without permanently removing records. This was not present in the initial schema.

2. **Avoided `ON DELETE CASCADE` in Critical Tables:**
- Changed `ON DELETE CASCADE` to `ON DELETE SET NULL` for `product_id` and `warehouse_id` in the `inventory` table to preserve historical data, preventing accidental loss of inventory data when a product or warehouse is deleted.

3. **Normalization and Relationships:**
- Ensured normalization by maintaining key relationships such as `One-to-Many` relationships for `Products to Categories`, `Products to Suppliers`, `Inventory to Warehouses`, etc., to reflect accurate business logic.

4. **Use of `ENUMs` for Statuses:**
- The status fields in `PurchaseOrders` and `SalesOrders` were normalized using `ENUMs` for clarity and consistency (e.g., `'Pending', 'Ordered', 'Received', 'Cancelled'`).

5. **Preservation of Historical Data:**
- Avoided foreign key constraints that could lead to data loss, opting instead for `SET NULL` and incorporating `ON DELETE RESTRICT` in critical relationships.

---

## Summary of Changes:
- Added `is_active` columns to enable soft deletes.
- Changed `ON DELETE CASCADE` to `ON DELETE SET NULL` for inventory management with products and warehouses.
- Improved normalization by adding `ENUMs` for statuses and ensuring proper relationships between entities.
- Focused on preserving historical data and flexibility for small business operations.

---

## Key Considerations

- **Normalization**: Adherence to normalization principles minimizes redundancy.
- **Soft Deletes**: Implemented using the `is_active` column for non-critical tables.
- **Scalability**: Schema designed to be adaptable to future data volume growth.

---