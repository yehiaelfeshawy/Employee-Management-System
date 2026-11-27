# Employee Management System

A Java Swing desktop app for managing employees and departments. Data is saved to local files, so it is still there the next time you open the app.

## Features

- Login screen
- Dashboard with a table of all employees
- Search by ID, Name or Department
- Add Full-Time and Part-Time employees
- View and update an employee
- Delete an employee
- Add departments
- Automatic saving to disk, with corrupt-file recovery

## Requirements

- Java JDK 11 or newer
- NetBeans (optional, the `.form` files are NetBeans GUI designs)

## How to Run

**NetBeans:** open the project, right-click `EmployeeManagementSystem.java`, then **Run File**.

**Terminal:** put all `.java` files in `com/mycompany/employeemanagementsystem/`, then:

```
javac com/mycompany/employeemanagementsystem/*.java
java com.mycompany.employeemanagementsystem.EmployeeManagementSystem
```

## Login

| Username | Password |
|----------|----------|
| `admin`  | `admin123` |

## How to Use

| Page | What to do |
|------|------------|
| **Dashboard** | Search with the dropdown (ID / Name / Department). **Show All** resets the table. Select a row, then **Delete Selected** to remove it. |
| **Add Employee** | Enter ID (unique, positive whole number), Full Name, Age, Department, Position and Employee Type. Full-Time needs Basic Salary and Bonus. Part-Time needs Hourly Rate and Hours Worked. |
| **View / Update** | Click an employee row to load it into the form, edit the fields, then click **Update Employee**. The pay fields only appear after a row is selected. |
| **Departments** | Enter a new unique Department ID and Name, then **Add Department**. |

Default departments: IT, Human Resources, Finance, Marketing, Sales.

## Salary Calculation

- **Full-Time:** Basic Salary + Bonus
- **Part-Time:** Hourly Rate x Hours Worked

## Input Rules

- ID and Age: positive whole numbers
- Salary, Bonus, Rate and Hours: numbers of 0 or more
- Name and Position cannot be empty
- Employee IDs must be unique
- Department IDs and names must be unique (names are case-insensitive)

## Project Structure

| File | Purpose |
|------|---------|
| `EmployeeManagementSystem.java` | Entry point |
| `MainFrame.java` | Main window, switches between panels |
| `LoginPanel.java` | Login screen |
| `DashboardPanel.java` | Employee table, search, delete |
| `AddEmployeePanel.java` | Add employee form |
| `ViewUpdatePanel.java` | View and edit employees |
| `DepartmentPanel.java` | Add departments |
| `EmployeeManager.java` | Business logic (add, update, delete, search) |
| `FileHandler.java` | Reads and writes data files |
| `Employee.java` | Abstract base class |
| `FullTimeEmployee.java` / `PartTimeEmployee.java` | Employee types |
| `Department.java` | Department model |

## Data Files

Created in the folder where the app is run:

- `employees.dat`
- `departments.dat`

If a file is unreadable, it is renamed with a `.corrupt` extension and the app starts with fresh data.
