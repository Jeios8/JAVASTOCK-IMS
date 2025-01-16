# Architectural Design

**System Architecture**: MVVM (Model-View-ViewModel)

## Model:
- Represents data and business logic.
- Includes classes such as `User`, `Inventory`, `Supplier`, `Database`.

## View:
- Responsible for UI presentation.
- Contains UI components like `LoginUI`, `SupplierUI`, `InventoryUI`.

## ViewModel:
- Acts as a bridge between Model and View.
- Manages user interactions and updates View accordingly.
- Includes classes such as `LoginVM`, `InventoryVM`, `SupplierVM`.
