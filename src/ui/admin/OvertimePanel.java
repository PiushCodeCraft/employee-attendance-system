package ui.admin;

import models.Employee;
import services.AttendanceService;
import services.EmployeeService;
import utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class OvertimePanel extends JPanel {

    private final EmployeeService   empService;
    private final AttendanceService attService;
    private DefaultTableModel       tableModel;

    public OvertimePanel(EmployeeService empService, AttendanceService attService) {
        this.empService = empService;
        this.attService = attService;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        initUI();
    }

    private void initUI() {
        // ── header ────────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.label("Overtime Summary", Theme.bold(22), Theme.TEXT_PRIMARY),
                   BorderLayout.WEST);

        JButton refresh = Theme.button("↻  Refresh", Theme.ACCENT_BLUE);
        refresh.addActionListener(e -> loadData());
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(refresh);
        header.add(right, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── table ─────────────────────────────────────────────────────────────
        String[] cols = {"Emp ID", "Name", "Department", "Total OT (hrs)", "Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        Theme.styleTable(table);

        // colour code the Status column
        table.getColumnModel().getColumn(4).setCellRenderer(
            (t, v, s, f, row, col) -> Theme.badge(v != null ? v.toString() : ""));

        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.add(sp, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);

        // ── note at bottom ────────────────────────────────────────────────────
        JPanel note = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        note.setOpaque(false);
        note.add(Theme.label("Standard workday = 9 hrs  |  Hours beyond 9 count as overtime.",
                 Theme.plain(12), Theme.TEXT_MUTED));
        add(note, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        Map<Integer, Double> otMap = attService.getOvertimeSummary();

        for (Employee e : empService.getAllEmployees()) {
            double ot     = otMap.getOrDefault(e.getEmpId(), 0.0);
            String status = ot >= 5 ? "Present" : ot > 0 ? "Late" : "Absent";
            // reuse badge colours: green ≥5h, orange 0-5h, red = no OT
            tableModel.addRow(new Object[]{
                e.getEmpId(),
                e.getName(),
                e.getDepartment(),
                String.format("%.2f", ot),
                status
            });
        }
    }
}