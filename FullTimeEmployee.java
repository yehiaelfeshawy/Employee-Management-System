public class FullTimeEmployee extends Employee {
    private static final long serialVersionUID = 1L;

    private double basicSalary;
    private double bonus;

    public FullTimeEmployee(int employeeId, String fullName, int age, Department department, String position,double basicSalary, double bonus) {
        super(employeeId, fullName, age, department, position);
        this.basicSalary = basicSalary;
        this.bonus = bonus;
    }

    @Override
    public double calculateSalary() {
        return basicSalary + bonus;
    }

    @Override
    public String getEmployeeType() {
        return "Full-Time";
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
}
