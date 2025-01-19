package oldsnippets;

public class DashboardVM {
    // Handle menu actions
    public void handleMenuAction(String action) {
        switch (action) {
            case "dashboard":
                System.out.println("Navigating to Dashboard");
                break;
            case "inventory":
                System.out.println("Navigating to Inventory");
                break;
            case "reports":
                System.out.println("Navigating to Reports");
                break;
            case "suppliers":
                System.out.println("Navigating to Suppliers");
                break;
            case "orders":
                System.out.println("Navigating to Orders");
                break;
            case "manageStore":
                System.out.println("Navigating to Manage Store");
                break;
            case "logout":
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Unknown action: " + action);
        }
    }

    // Handle search
    public void searchAction(String query) {
        if (query.isBlank()) {
            System.out.println("Search query is empty.");
        } else {
            System.out.println("Searching for: " + query);
        }
    }
}
