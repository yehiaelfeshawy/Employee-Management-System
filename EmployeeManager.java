import java.util.ArrayList;
import java.util.List;

public class EmployeeManager {
    private List<Department> departments = new ArrayList<>();
    private List<Employee> employees = new ArrayList<>();

    public EmployeeManager() {
        departments.add(new Department(1, "Engineering"));
        departments.add(new Department(2, "HR"));
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void addEmployee(Employee emp) {
        employees.add(emp);
    }
}

