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
├── docs/                   # Documentation for the project
├── src/
│   ├── main/               # Application source code
│   │   ├── model/          # Business logic and database models
│   │   ├── view/           # UI components and screens
│   │   ├── viewmodel/      # Connects models and views
│   │   ├── App.java        # Application entry point
│   │   └── Router.java     # Navigation between views
│   ├── test/               # Unit and integration tests
│   ├── resources/          # Configuration and database scripts
│   │   ├── application.properties  # DB connection details
│   │   └── db/                     # SQL scripts for schema and seed data
├── .gitignore              # Git ignored files and folders
├── README.md               # Project overview and documentation
└── pom.xml or build.gradle # Dependency management
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
- Update src/resources/application.properties with your MySQL database credentials.
Build and run the project:

## **License**
This project is licensed under the **MIT License**.

