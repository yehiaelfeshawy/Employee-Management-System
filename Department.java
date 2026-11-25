

import java.io.Serializable;
import java.util.Objects;

public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    private int departmentId;
    private String departmentName;

    public Department(int departmentId, String departmentName) {

        if (departmentId <= 0)
            throw new IllegalArgumentException("Invalid department ID");

        if (departmentName == null || departmentName.trim().isEmpty())
            throw new IllegalArgumentException("Department name cannot be empty");

        this.departmentId = departmentId;
        this.departmentName = departmentName.trim();
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        if (departmentName == null || departmentName.trim().isEmpty())
            throw new IllegalArgumentException("Department name cannot be empty");

        this.departmentName = departmentName.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department)) return false;
        Department that = (Department) o;
        return departmentId == that.departmentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId);
    }

    @Override
    public String toString() {
        return departmentName;
    }
}

