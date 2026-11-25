import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

/**
 * Handles reading and writing of employee and department data using
 * Java Object Serialization. Employee subclasses (FullTimeEmployee,
 * PartTimeEmployee) are stored as full objects, so their type-specific
 * salary fields are restored exactly on startup.
 */
public class FileHandler {

    private String empFile;
    private String deptFile;

    public FileHandler() {
        this.empFile = "employees.dat";
        this.deptFile = "departments.dat";
    }

    // ---------------- Employees ----------------

    public void saveEmployees(ArrayList<Employee> list) {
        writeList(list, empFile);
    }

    public ArrayList<Employee> loadEmployees() {
        ArrayList<Employee> result = new ArrayList<>();
        for (Object o : readList(empFile)) {
            if (o instanceof Employee) {
                result.add((Employee) o);
            }
        }
        return result;
    }

    // ---------------- Departments ----------------

    public void saveDepartments(ArrayList<Department> list) {
        writeList(list, deptFile);
    }

    public ArrayList<Department> loadDepartments() {
        ArrayList<Department> result = new ArrayList<>();
        for (Object o : readList(deptFile)) {
            if (o instanceof Department) {
                result.add((Department) o);
            }
        }
        return result;
    }

    // ---------------- Helpers ----------------

    /**
     * Writes to a temporary file first, then replaces the real file, so a
     * failure mid-write can never corrupt the existing data.
     */
    private void writeList(ArrayList<?> list, String fileName) {
        File target = new File(fileName);
        File temp = new File(fileName + ".tmp");
        try {
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(new FileOutputStream(temp)))) {
                out.writeObject(list);
            }
            Files.move(temp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error saving to " + fileName + ": " + e.getMessage());
            temp.delete();
        }
    }

    /** Returns an empty list if the file is missing, unreadable or invalid. */
    private ArrayList<?> readList(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return new ArrayList<>(); // first launch
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {
            Object obj = in.readObject();
            if (obj instanceof ArrayList) {
                return (ArrayList<?>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading " + fileName + ": " + e.getMessage());
        }
        return new ArrayList<>();
    }
}
