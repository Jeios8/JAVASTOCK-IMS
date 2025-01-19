import java.util.List;

public class InventoryApp {
    public static void main(String[] args) {
        InventoryManagement inventoryManager = new InventoryManagement();
        List<Product> products = inventoryManager.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products found.");
        } else {
            System.out.println("All Products:");
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }
}
