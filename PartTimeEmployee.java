public class PartTimeEmployee extends Employee {
    private static final long serialVersionUID = 1L;

    private double hourlyRate;
    private double hoursWorked;

    public PartTimeEmployee(int employeeId, String fullName, int age, Department department, String position,
                            double hourlyRate, double hoursWorked) {
        super(employeeId, fullName, age, department, position);
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public String getEmployeeType() {
        return "Part-Time";
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }
}