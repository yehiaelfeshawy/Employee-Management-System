import java.io.Serializable;

public abstract class Employee implements Serializable {
    private static final long serialVersionUID = 1L;

    private int employeeId;
    private String fullName;
    private int age;
    private Department department;
    private String position;

    public Employee(int employeeId, String fullName, int age, Department department, String position) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.age = age;
        this.department = department;
        this.position = position;
    }

    public abstract double calculateSalary();

    public abstract String getEmployeeType();

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}