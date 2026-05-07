package ui.employee;

import models.Attendance;
import models.Employee;
import services.AttendanceService;
import utils.DateTimeUtil;
import utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewMyAttendance extends JPanel {

    private final Employee employee;
    private final AttendanceService attService;

    public ViewMyAttendance(Employee employee, AttendanceService attService) {
        this.employee   = employee;
        this.attService = attService;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        initUI();
    }

    private void initUI() {
        add(Theme.label("My Attendance History", Theme.bold(22), Theme.TEXT_PRIMARY), BorderLayout.NORTH);

        List<Attendance> list = attService.getMyAttendance(employee.getEmpId());

        // Summary bar
        long present = list.stream().filter(a -> "Present".equals(a.getStatus())).count();
        long late    = list.stream().filter(a -> "Late".equals(a.getStatus())).count();
        // long absent  = list.stream().filter(a -> "Absent".equals(a.getStatus())).count();
        double pct   = list.size() > 0 ? ((present + late) * 100.0 / list.size()) : 0;

        JPanel summaryRow = new JPanel(new GridLayout(1, 4, 12, 0));
        summaryRow.setOpaque(false);
        summaryRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        summaryRow.add(miniCard("Total",    String.valueOf(list.size()), Theme.ACCENT_BLUE));
        summaryRow.add(miniCard("Present",  String.valueOf(present),    Theme.ACCENT_GREEN));
        summaryRow.add(miniCard("Late",     String.valueOf(late),       Theme.ACCENT_ORANGE));
        summaryRow.add(miniCard("Rate",     String.format("%.0f%%", pct), pct >= 90 ? Theme.ACCENT_GREEN : Theme.ACCENT_ORANGE));

        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setOpaque(false);
        body.add(summaryRow, BorderLayout.NORTH);

        String[] cols = {"Date", "Check In", "Check Out", "Status", "Working Hours"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (Attendance a : list) {
            model.addRow(new Object[]{
                DateTimeUtil.formatDate(a.getDate()),
                DateTimeUtil.formatTime(a.getCheckIn()),
                DateTimeUtil.formatTime(a.getCheckOut()),
                a.getStatus(),
                String.format("%.2f hrs", a.getWorkingHours())
            });
        }
        JTable table = new JTable(model);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(3).setCellRenderer((tbl, val, sel, foc, row, col) ->
            Theme.badge(val != null ? val.toString() : ""));

        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.add(sp, BorderLayout.CENTER);
        body.add(card, BorderLayout.CENTER);
        add(body, BorderLayout.CENTER);
    }

    private JPanel miniCard(String title, String value, Color accent) {
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 16, 12, 16));
        card.add(Theme.label(value, Theme.bold(28), accent), BorderLayout.CENTER);
        card.add(Theme.label(title, Theme.plain(12), Theme.TEXT_SECONDARY), BorderLayout.SOUTH);
        return card;
    }
}
