package main.resources.db;

import main.java.com.javastock.utils.DatabaseSeeder;
import main.java.com.javastock.utils.InventorySeeder;
import main.java.com.javastock.utils.UserSeeder;

public class Seeder {
    public static void main(String[] args) {
        DatabaseSeeder seeder = new DatabaseSeeder();
        //seeder.seedRoles();
        seeder.seedUsers();
        seeder.seedCustomers();
        seeder.seedCategories();
        seeder.seedSuppliers();
        seeder.seedWarehouses();
        seeder.seedProducts();
        seeder.seedInventory();
    }
}
