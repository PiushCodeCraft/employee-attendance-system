package ui.admin;

import models.Attendance;
import services.AttendanceService;
import utils.DateTimeUtil;
import utils.Theme;

import javax.swing.*;
// import javax.swing.border.EmptyBorder;
// import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ViewAttendance extends JPanel {

    private final AttendanceService attService;
    private DefaultTableModel tableModel;

    public ViewAttendance(AttendanceService attService) {
        this.attService = attService;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        initUI();
        loadAll();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.label("Attendance Records", Theme.bold(22), Theme.TEXT_PRIMARY), BorderLayout.WEST);

        JPanel filterRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterRow.setOpaque(false);
        JTextField dateField = Theme.textField(12);
        dateField.putClientProperty("placeholder", "yyyy-MM-dd");
        JButton filterBtn = Theme.button("Filter Date", Theme.ACCENT_BLUE);
        JButton allBtn    = Theme.button("Show All",    new Color(55, 65, 90));
        filterBtn.addActionListener(e -> {
            try { loadByDate(LocalDate.parse(dateField.getText().trim())); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, "Use format: yyyy-MM-dd"); }
        });
        allBtn.addActionListener(e -> loadAll());
        filterRow.add(Theme.label("Date:", Theme.plain(13), Theme.TEXT_SECONDARY));
        filterRow.add(dateField);
        filterRow.add(filterBtn);
        filterRow.add(allBtn);
        header.add(filterRow, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        String[] cols = {"Emp ID", "Date", "Check In", "Check Out", "Status", "Hours"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        Theme.styleTable(table);

        // Status badge renderer
        table.getColumnModel().getColumn(4).setCellRenderer((tbl, val, sel, foc, row, col) ->
            Theme.badge(val != null ? val.toString() : ""));

        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.add(sp, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void loadAll() {
        populate(attService.getAllAttendance());
    }

    private void loadByDate(LocalDate date) {
        populate(attService.getAttendanceByDate(date));
    }

    private void populate(List<Attendance> list) {
        tableModel.setRowCount(0);
        for (Attendance a : list) {
            tableModel.addRow(new Object[]{
                a.getEmpId(),
                DateTimeUtil.formatDate(a.getDate()),
                DateTimeUtil.formatTime(a.getCheckIn()),
                DateTimeUtil.formatTime(a.getCheckOut()),
                a.getStatus(),
                String.format("%.2f hrs", a.getWorkingHours())
            });
        }
    }
}
