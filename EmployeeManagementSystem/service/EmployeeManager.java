package EmployeeManagementSystem.service;



import EmployeeManagementSystem.model.Department;
import EmployeeManagementSystem.model.Employee;
import EmployeeManagementSystem.model.FullTimeEmployee;
import EmployeeManagementSystem.model.PartTimeEmployee;
import EmployeeManagementSystem.storage.FileHandler;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Business logic layer: manages employees and departments in memory
 * and saves to disk through FileHandler after every change.
 */
public class EmployeeManager {

    private ArrayList<Employee> employees;
    private ArrayList<Department> departments;
    private FileHandler fileHandler;

    public EmployeeManager() {

        fileHandler = new FileHandler();

        employees = fileHandler.loadEmployees();
        departments = fileHandler.loadDepartments();

        // First launch: no departments file exists.
        if (!fileHandler.departmentsFileExists()) {

            departments.add(new Department(1, "IT"));
            departments.add(new Department(2, "Human Resources"));
            departments.add(new Department(3, "Finance"));
            departments.add(new Department(4, "Marketing"));
            departments.add(new Department(5, "Sales"));

            saveDepartments();
        }
    }

    // ---------------- Employees ----------------

    /** Adds an employee. Returns false if invalid data, duplicate ID or invalid department. */
    public boolean addEmployee(Employee e) {

        if (!isValidEmployee(e) || !isValidDepartment(e.getDepartment())) {
            return false;
        }

        if (searchById(e.getEmployeeId()) != null) {
            return false;
        }

        employees.add(e);

        if (!saveEmployees()) {
            employees.remove(e);
            return false;
        }

        return true;
    }

    /**
     * Updates an existing employee.
     * Finds the employee by object reference first, then by ID.
     */
    public boolean updateEmployee(Employee e) {

        if (!isValidEmployee(e) || !isValidDepartment(e.getDepartment())) {
            return false;
        }

        int index = -1;

        // Find by object reference first.
        for (int i = 0; i < employees.size(); i++) {

            if (employees.get(i) == e) {
                index = i;
                break;
            }
        }

        // If not found, find by employee ID.
        if (index == -1) {

            for (int i = 0; i < employees.size(); i++) {

                if (employees.get(i).getEmployeeId() == e.getEmployeeId()) {
                    index = i;
                    break;
                }
            }
        }

        if (index == -1) {
            return false;
        }

        // Check ID uniqueness among other employees.
        for (int i = 0; i < employees.size(); i++) {

            if (i != index
                    && employees.get(i).getEmployeeId() == e.getEmployeeId()) {
                return false;
            }
        }

        Employee old = employees.set(index, e);

        if (!saveEmployees()) {
            employees.set(index, old);
            return false;
        }

        return true;
    }

    public boolean deleteEmployee(int id) {

        for (int i = 0; i < employees.size(); i++) {

            if (employees.get(i).getEmployeeId() == id) {

                Employee removed = employees.remove(i);

                if (!saveEmployees()) {
                    employees.add(i, removed);
                    return false;
                }

                return true;
            }
        }

        return false;
    }

    /** Returns employee with this ID, or null if not found. */
    public Employee searchById(int id) {

        for (Employee e : employees) {

            if (e.getEmployeeId() == id) {
                return e;
            }
        }

        return null;
    }

    /** Case-insensitive contains search on full name. */
    public List<Employee> searchByName(String name) {

        List<Employee> result = new ArrayList<>();

        if (name == null) {
            return result;
        }

        String key = name.trim().toLowerCase(Locale.ROOT);

        for (Employee e : employees) {

            if (e.getFullName() != null
                    && e.getFullName().toLowerCase(Locale.ROOT).contains(key)) {
                result.add(e);
            }
        }

        return result;
    }

    /** Case-insensitive match on department name. */
    public List<Employee> filterByDepartment(String dept) {

        List<Employee> result = new ArrayList<>();

        if (dept == null) {
            return result;
        }

        String key = dept.trim();

        for (Employee e : employees) {

            Department d = e.getDepartment();

            if (d != null
                    && d.getDepartmentName().equalsIgnoreCase(key)) {
                result.add(e);
            }
        }

        return result;
    }

    /** Returns a copy of the employee list. */
    public ArrayList<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    // ---------------- Departments ----------------

    /**
     * Rejects null/empty name, invalid ID, duplicate ID,
     * or duplicate department name.
     */
    public boolean addDepartment(Department d) {

        if (d == null
                || d.getDepartmentId() <= 0
                || d.getDepartmentName() == null
                || d.getDepartmentName().trim().isEmpty()) {
            return false;
        }

        String newName = d.getDepartmentName().trim();

        // Remove unnecessary spaces before storing.
        d.setDepartmentName(newName);

        for (Department existing : departments) {

            if (existing.getDepartmentId() == d.getDepartmentId()
                    || existing.getDepartmentName().trim()
                            .equalsIgnoreCase(newName)) {
                return false;
            }
        }

        departments.add(d);

        if (!saveDepartments()) {
            departments.remove(d);
            return false;
        }

        return true;
    }

    /** Returns a copy of the department list. */
    public ArrayList<Department> getDepartments() {
        return new ArrayList<>(departments);
    }

    // ---------------- Helpers ----------------

    /** Saves employees; returns false if saving fails. */
    private boolean saveEmployees() {

        try {

            fileHandler.saveEmployees(employees);
            return true;

        } catch (UncheckedIOException ex) {

            System.err.println(
                    "Error saving employees: " + ex.getMessage()
            );

            return false;
        }
    }

    /** Saves departments; returns false if saving fails. */
    private boolean saveDepartments() {

        try {

            fileHandler.saveDepartments(departments);
            return true;

        } catch (UncheckedIOException ex) {

            System.err.println(
                    "Error saving departments: " + ex.getMessage()
            );

            return false;
        }
    }

    /** Checks whether the department belongs to the current list. */
    private boolean isValidDepartment(Department d) {

        return d != null && departments.contains(d);
        // Department.equals() compares department IDs.
    }

    private boolean isNonNegative(double v) {
        return !Double.isNaN(v) && !Double.isInfinite(v) && v >= 0;
    }

    /** Final guard: rejects invalid data even if a screen forgot to check it. */
    private boolean isValidEmployee(Employee e) {
        if (e == null) {
            return false;
        }
        if (e.getEmployeeId() <= 0 || e.getAge() <= 0) {
            return false;
        }
        if (e.getFullName() == null || e.getFullName().trim().isEmpty()) {
            return false;
        }
        if (e.getPosition() == null || e.getPosition().trim().isEmpty()) {
            return false;
        }
        if (e instanceof FullTimeEmployee) {
            FullTimeEmployee f = (FullTimeEmployee) e;
            return isNonNegative(f.getBasicSalary()) && isNonNegative(f.getBonus());
        }
        if (e instanceof PartTimeEmployee) {
            PartTimeEmployee p = (PartTimeEmployee) e;
            return isNonNegative(p.getHourlyRate()) && isNonNegative(p.getHoursWorked());
        }
        return false;
    }
}