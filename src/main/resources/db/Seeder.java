package main.resources.db;

import main.java.com.javastock.utils.DatabaseSeeder;

public class Seeder {
    public static void main(String[] args) {
        DatabaseSeeder seeder = new DatabaseSeeder();
        seeder.seedUsers();  // Seed the users table
    }
}
