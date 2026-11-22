package com.mycompany.employeemanagementsystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DepartmentPanel extends JPanel {

    private final EmployeeManager manager;
    private final MainFrame mainFrame;

    private final JTextField idField = new JTextField(10);
    private final JTextField nameField = new JTextField(15);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Department ID", "Department Name"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable deptTable = new JTable(tableModel);

    public DepartmentPanel(EmployeeManager manager, MainFrame mainFrame) {
        this.manager = manager;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        createFormPanel();
        createTablePanel();
        createBottomPanel();

        refreshData();
    }

    private void createFormPanel() {
        JPanel formPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Department");

        formPanel.add(new JLabel("Department ID:"));
        formPanel.add(idField);

        formPanel.add(new JLabel("Department Name:"));
        formPanel.add(nameField);

        formPanel.add(addButton);

        add(formPanel, BorderLayout.NORTH);

        addButton.addActionListener(e -> handleAddDepartment());
        idField.addActionListener(e -> handleAddDepartment());
        nameField.addActionListener(e -> handleAddDepartment());
    }

    private void createTablePanel() {
        deptTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(deptTable), BorderLayout.CENTER);
    }

    private void createBottomPanel() {
        if (mainFrame == null) {
            return;
        }

        JButton backButton = new JButton("Back to Dashboard");
        backButton.addActionListener(e -> mainFrame.showPanel("DASHBOARD"));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void handleAddDepartment() {
        String idText = idField.getText().trim();
        String name = nameField.getText().trim();

        if (idText.isEmpty() || name.isEmpty()) {
            showError("Please enter both Department ID and Department Name.");
            return;
        }

        int departmentId;
        try {
            departmentId = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            showError("Department ID must be a valid integer.");
            return;
        }

        if (departmentId <= 0) {
            showError("Department ID must be a positive integer.");
            return;
        }

        if (manager == null) {
            showError("Employee Manager is not available.");
            return;
        }

        Department department = new Department(departmentId, name);
        if (!manager.addDepartment(department)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Duplicate Department ID or Department Name (case-insensitive).",
                    "Duplicate Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        idField.setText("");
        nameField.setText("");

        refreshData();

        JOptionPane.showMessageDialog(
                this,
                "Department added successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        idField.requestFocusInWindow();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    public void refreshData() {
        tableModel.setRowCount(0);

        if (manager == null) {
            return;
        }

        List<Department> departments = manager.getDepartments();
        if (departments == null) {
            return;
        }

        for (Department department : departments) {
            tableModel.addRow(new Object[]{
                    department.getDepartmentId(),
                    department.getDepartmentName()
            });
        }
    }
}