import java.util.ArrayList;
import java.util.List;

/**
 * Business logic layer: manages employees and departments in memory and
 * saves to disk (through FileHandler) after every change.
 */
public class EmployeeManager {

    private ArrayList<Employee> employees;
    private ArrayList<Department> departments;
    private FileHandler fileHandler;

    public EmployeeManager() {
        fileHandler = new FileHandler();
        employees = fileHandler.loadEmployees();
        departments = fileHandler.loadDepartments();

        // First launch: no departments saved yet, so start with the defaults.
        if (departments.isEmpty()) {
            departments.add(new Department(1, "IT"));
            departments.add(new Department(2, "Human Resources"));
            departments.add(new Department(3, "Finance"));
            departments.add(new Department(4, "Marketing"));
            departments.add(new Department(5, "Sales"));
            fileHandler.saveDepartments(departments);
        }
    }

    // ---------------- Employees ----------------

    /** Adds an employee. Returns false if null, duplicate ID or invalid department. */
    public boolean addEmployee(Employee e) {
        if (e == null || !isValidDepartment(e.getDepartment())) {
            return false;
        }
        if (searchById(e.getEmployeeId()) != null) {
            return false;
        }
        employees.add(e);
        fileHandler.saveEmployees(employees);
        return true;
    }

    /**
     * Saves changes made to an existing employee.
     * The employee is located by object reference first (the panel edits the
     * object it got from the list), then by ID (if the panel built a new object,
     * e.g. when the type changed). Returns false if not found, if the ID now
     * clashes with another employee, or if the department is invalid.
     */
    public boolean updateEmployee(Employee e) {
        if (e == null || !isValidDepartment(e.getDepartment())) {
            return false;
        }

        int index = -1;
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i) == e) {
                index = i;
                break;
            }
        }
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

        // ID must stay unique among all the OTHER employees.
        for (int i = 0; i < employees.size(); i++) {
            if (i != index && employees.get(i).getEmployeeId() == e.getEmployeeId()) {
                return false;
            }
        }

        employees.set(index, e);
        fileHandler.saveEmployees(employees);
        return true;
    }

    public boolean deleteEmployee(int id) {
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getEmployeeId() == id) {
                employees.remove(i);
                fileHandler.saveEmployees(employees);
                return true;
            }
        }
        return false;
    }

    /** Returns the employee with this ID, or null if not found. */
    public Employee searchById(int id) {
        for (Employee e : employees) {
            if (e.getEmployeeId() == id) {
                return e;
            }
        }
        return null;
    }

    /** Case-insensitive "contains" search on the full name. */
    public List<Employee> searchByName(String name) {
        List<Employee> result = new ArrayList<>();
        if (name == null) {
            return result;
        }
        String key = name.trim().toLowerCase();
        for (Employee e : employees) {
            if (e.getFullName().toLowerCase().contains(key)) {
                result.add(e);
            }
        }
        return result;
    }

    /** Case-insensitive match on the department name. */
    public List<Employee> filterByDepartment(String dept) {
        List<Employee> result = new ArrayList<>();
        if (dept == null) {
            return result;
        }
        String key = dept.trim();
        for (Employee e : employees) {
            Department d = e.getDepartment();
            if (d != null && d.getDepartmentName().equalsIgnoreCase(key)) {
                result.add(e);
            }
        }
        return result;
    }

    /** Returns a copy of the list (the employee objects themselves are the same). */
    public ArrayList<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    // ---------------- Departments ----------------

    /** Rejects null/empty name, duplicate ID, or duplicate name (ignoring case). */
    public boolean addDepartment(Department d) {
        if (d == null || d.getDepartmentName() == null
                || d.getDepartmentName().trim().isEmpty()) {
            return false;
        }
        String newName = d.getDepartmentName().trim();
        for (Department existing : departments) {
            if (existing.getDepartmentId() == d.getDepartmentId()
                    || existing.getDepartmentName().equalsIgnoreCase(newName)) {
                return false;
            }
        }
        departments.add(d);
        fileHandler.saveDepartments(departments);
        return true;
    }

    /** Returns a copy of the list. */
    public ArrayList<Department> getDepartments() {
        return new ArrayList<>(departments);
    }

    // ---------------- Helpers ----------------

    private boolean isValidDepartment(Department d) {
        return d != null && departments.contains(d); // equals() compares departmentId
    }
}
