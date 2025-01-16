# JAVASTOCK-IMS

**JAVASTOCK-IMS** (Inventory Management System) is a Java-based application designed to streamline inventory management processes. Built using the **MVVM architecture** and Java Swing, the system allows users to manage stock efficiently with a robust, user-friendly interface.

---

## **Features**
- Add, update, and remove inventory items.
- Track stock levels in real-time.
- Generate detailed inventory reports.
- Secure integration with a MySQL database.

---

## **File Structure**
The project follows the **MVVM architecture**:

```plaintext
JAVASTOCK-IMS/
│
├── docs/
│   ├── requirements.md
│   ├── architecture.md
│   ├── project-plan.md
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── javastock/
│   │   │           ├── model/           # Business logic and database models
│   │   │           ├── view/            # Swing-based UI components
│   │   │           ├── viewmodel/       # MVVM bridge layer
│   │   │           ├── App.java         # Application entry point
│   │   │           └── Router.java      # Navigation manager (optional, for now)
│   │   │
│   │   ├── resources/
│   │   │   ├── application.properties   # Configuration (e.g., database connection)
│   │   │   └── db/
│   │   │       ├── schema.sql           # Database schema script
│   │   │       └── seed.sql             # Sample data for testing
│   │
│   ├── test/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── javastock/            # for testing purposes
│
├── .gitignore                            # Files and folders to ignore in version control
├── README.md                             # Project overview and documentation
└── pom.xml or build.gradle               # Build tool configuration
```

## **Technologies**
- **Language**: Java (Swing for GUI)
- **Database**: MySQL
- **Architecture**: MVVM (Model-View-ViewModel)

## **Setup Instructions**
1. Clone the repository:
```bash
git clone https://github.com/<your-username>/JAVASTOCK-IMS.git
```
2. Configure the database:
- Update src/resources/application.properties with the MySQL database credentials.
Build and run the project:

## **License**
This project is licensed under the **MIT License**.

