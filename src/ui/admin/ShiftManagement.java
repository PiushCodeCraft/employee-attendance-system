package ui.admin;

import models.Employee;
import services.EmployeeService;
import utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalTime;

public class ShiftManagement extends JPanel {

    private final EmployeeService  empService;
    private DefaultTableModel      tableModel;
    private JTable                 table;

    public ShiftManagement(EmployeeService empService) {
        this.empService = empService;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        initUI();
    }

    private void initUI() {
        // ── header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.label("Shift Management", Theme.bold(22), Theme.TEXT_PRIMARY),
                   BorderLayout.WEST);

        // ── table ─────────────────────────────────────────────────────────────
        String[] cols = {"ID", "Name", "Department", "Designation", "Current Shift", "Shift Start"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);

        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.add(sp, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);
        add(card,   BorderLayout.CENTER);

        // ── bottom: shift assignment controls ─────────────────────────────────
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        bottom.setOpaque(false);

        JComboBox<String> shiftBox = Theme.comboBox(
                new String[]{"Morning (9:00)", "Evening (14:00)", "Night (22:00)"});

        JButton assignBtn = Theme.button("✔  Assign Shift", Theme.ACCENT_BLUE);
        assignBtn.addActionListener(e -> assignShift(
                (String) shiftBox.getSelectedItem()));

        bottom.add(Theme.label("Select Shift:", Theme.bold(13), Theme.TEXT_SECONDARY));
        bottom.add(shiftBox);
        bottom.add(assignBtn);
        add(bottom, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Employee e : empService.getAllEmployees()) {
            tableModel.addRow(new Object[]{
                e.getEmpId(),
                e.getName(),
                e.getDepartment(),
                e.getDesignation(),
                e.getShift()      != null ? e.getShift()      : "Not Assigned",
                e.getShiftStart() != null ? e.getShiftStart() : "—"
            });
        }
    }

    private void assignShift(String shiftLabel) {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an employee first.");
            return;
        }
        int empId = (int) tableModel.getValueAt(row, 0);

        String    shiftName;
        LocalTime shiftStart;
        switch (shiftLabel) {
            case "Evening (14:00)" -> { shiftName = "Evening"; shiftStart = LocalTime.of(14, 0); }
            case "Night (22:00)"   -> { shiftName = "Night";   shiftStart = LocalTime.of(22, 0); }
            default                -> { shiftName = "Morning"; shiftStart = LocalTime.of(9,  0); }
        }

        empService.assignShift(empId, shiftName, shiftStart);
        loadData();
        JOptionPane.showMessageDialog(this,
            "Shift \"" + shiftName + "\" assigned successfully!");
    }
}