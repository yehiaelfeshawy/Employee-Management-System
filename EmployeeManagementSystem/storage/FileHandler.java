package EmployeeManagementSystem.storage;

import EmployeeManagementSystem.model.Department;
import EmployeeManagementSystem.model.Employee;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.AtomicMoveNotSupportedException;
import java.util.ArrayList;

/**
 * Handles reading and writing employee and department data
 * using Java Object Serialization.
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

        for (Object o : readList(empFile, Employee.class)) {
            result.add((Employee) o);
        }

        return result;
    }

    // ---------------- Departments ----------------

    public void saveDepartments(ArrayList<Department> list) {
        writeList(list, deptFile);
    }

    public ArrayList<Department> loadDepartments() {
        ArrayList<Department> result = new ArrayList<>();

        for (Object o : readList(deptFile, Department.class)) {
            result.add((Department) o);
        }

        return result;
    }

    // ---------------- File Checking ----------------

    public boolean departmentsFileExists() {
        return new File(deptFile).exists();
    }

    // ---------------- Saving Helper ----------------

    /**
     * Writes to a temporary file first.
     * Replaces the original file only after successful writing.
     */
    private void writeList(ArrayList<?> list, String fileName) {

        File target = new File(fileName);
        File temp = new File(fileName + ".tmp");

        try {

            try (ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(temp)))) {

                out.writeObject(list);
            }

            try {
                Files.move(
                        temp.toPath(),
                        target.toPath(),
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE
                );

            } catch (AtomicMoveNotSupportedException e) {

                Files.move(
                        temp.toPath(),
                        target.toPath(),
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

        } catch (IOException e) {

            System.err.println(
                    "Error saving to " + fileName + ": " + e.getMessage()
            );

            try {
                Files.deleteIfExists(temp.toPath());
            } catch (IOException cleanupError) {
                System.err.println("Could not delete temporary file.");
            }

            throw new UncheckedIOException(
                    "Could not save " + fileName, e
            );
        }
    }

    // ---------------- Loading Helper ----------------

    /**
     * Returns an empty list only if the file does not exist.
     *
     * If the file exists but is corrupted or invalid,
     * it is backed up and an exception is thrown.
     */
    private ArrayList<?> readList(String fileName, Class<?> expectedType) {

        File file = new File(fileName);

        // First launch: file does not exist.
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(file)))) {

            Object obj = in.readObject();

            if (!(obj instanceof ArrayList)) {
                throw new IOException("Invalid file format.");
            }

            ArrayList<?> list = (ArrayList<?>) obj;

            // Validate every stored object.
            for (Object item : list) {
                if (item == null || !expectedType.isInstance(item)) {
                    throw new IOException(
                            "Invalid object in " + fileName
                    );
                }
            }

            return list;

        } catch (IOException | ClassNotFoundException e) {

            System.err.println(
                    "Error loading " + fileName + ": " + e.getMessage()
            );

            backupCorruptFile(file);

            throw new UncheckedIOException(
                    "Could not load " + fileName
                    + ". Original file was preserved if backup succeeded.",
                    e instanceof IOException
                            ? (IOException) e
                            : new IOException(e)
            );
        }
    }

    // ---------------- Backup Helper ----------------

    private void backupCorruptFile(File file) {

        File backup = new File(file.getPath() + ".corrupt");

        try {

            Files.move(
                    file.toPath(),
                    backup.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.err.println(
                    file.getName() + " was preserved as "
                    + backup.getName()
            );

        } catch (IOException e) {

            System.err.println(
                    "Could not back up " + file.getName()
                    + ": " + e.getMessage()
            );
        }
    }
}
