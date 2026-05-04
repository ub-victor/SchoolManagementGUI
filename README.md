# School Management GUI

A Java Swing-based school management application using MVC architecture and MySQL for persistence.

## Features

- Manage students: add, update, delete, view student list
- Manage teachers: add, update, delete, view teacher list
- Manage courses: add, update, delete, assign teachers
- Enroll students into courses with capacity checks
- Record fee payments and display payment history
- View student fee status (total, paid, remaining)

## Prerequisites

- Java JDK 17 or later
- MySQL server
- MySQL JDBC driver `mysql-connector-j-9.6.0.jar`
- A database named `school_db` with required tables

## Database Setup

1. Create the database and tables using `database-schema.sql`.
2. Update `src/com/school/model/DatabaseConnection.java` if your MySQL credentials differ.

Example SQL execution:

```bash
mysql -u root -p < database-schema.sql
```

## Build and Run

From the project root:

```bash
cd /home/victoire/Desktop/My-Project/SchoolManagementGUI
find src -name '*.java' | sort | xargs javac -cp "src:lib/mysql-connector-j-9.6.0.jar"
java -cp "src:lib/mysql-connector-j-9.6.0.jar" com.school.Main
```
Run Directly
```bash
cd /home/victoire/Desktop/My-Project/SchoolManagementGUI
java -cp "src:lib/mysql-connector-j-9.6.0.jar" com.school.Main
```

## Project Structure

- `src/com/school/Main.java` - application entry point
- `src/com/school/view/MainFrame.java` - main GUI window with tabs
- `src/com/school/view/*Panel.java` - UI panels for students, teachers, courses, enrollments, payments
- `src/com/school/controller/SchoolController.java` - business and service coordination
- `src/com/school/dao/*.java` - data access layer for MySQL
- `src/com/school/model/*.java` - domain models and database connection

## Configuration

- `src/com/school/model/DatabaseConnection.java` contains the database URL, username, and password.
- Ensure the JDBC driver JAR is present in `lib/mysql-connector-j-9.6.0.jar`.

## Notes

- If you see missing Swing classes in VS Code, install the `Extension Pack for Java`.
- Use `Java: Clean Java Language Server Workspace` after installing extensions.

