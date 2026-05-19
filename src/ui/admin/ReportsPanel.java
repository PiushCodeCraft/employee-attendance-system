package ui.admin;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import models.Attendance;
import models.Employee;
import services.AttendanceService;
import services.EmployeeService;
import utils.Theme;

public class ReportsPanel extends JPanel {

    private final EmployeeService   empService;
    private final AttendanceService attService;
    private DefaultTableModel       tableModel;

    public ReportsPanel(EmployeeService empService, AttendanceService attService) {
        this.empService = empService;
        this.attService = attService;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        initUI();
        generateReport();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.label("Attendance Reports", Theme.bold(22), Theme.TEXT_PRIMARY),
                   BorderLayout.WEST);
        JButton refresh = Theme.button("↻  Refresh", Theme.ACCENT_BLUE);
        refresh.addActionListener(e -> generateReport());
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(refresh);
        header.add(right, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        String[] cols = {"Emp ID","Name","Total","Present","Late","Absent","Attendance %"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        Theme.styleTable(table);

        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                String s = val != null ? val.toString().replace("%","") : "0";
                try {
                    double v = Double.parseDouble(s);
                    lbl.setForeground(v >= 90 ? Theme.ACCENT_GREEN
                                    : v >= 75 ? Theme.ACCENT_ORANGE
                                    :           Theme.ACCENT_RED);
                } catch (Exception ignored) {}
                lbl.setFont(Theme.bold(13));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) lbl.setBackground(Theme.BG_CARD);
                return lbl;
            }
        });

        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.add(sp, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void generateReport() {
        tableModel.setRowCount(0);
        List<Employee>   emps = empService.getAllEmployees();
        List<Attendance> all  = attService.getAllAttendance();

        Map<Integer, List<Attendance>> map = new HashMap<>();
        for (Employee e : emps) map.put(e.getEmpId(), new ArrayList<>());
        for (Attendance a : all)
            if (map.containsKey(a.getEmpId())) map.get(a.getEmpId()).add(a);

        for (Employee e : emps) {
            List<Attendance> ea = map.get(e.getEmpId());
            long present = ea.stream().filter(a -> "Present".equals(a.getStatus())).count();
            long late    = ea.stream().filter(a -> "Late"   .equals(a.getStatus())).count();
            long absent  = ea.stream().filter(a -> "Absent" .equals(a.getStatus())).count();
            double pct   = ea.isEmpty() ? 0 : (present + late) * 100.0 / ea.size();
            tableModel.addRow(new Object[]{
                e.getEmpId(), e.getName(), ea.size(),
                present, late, absent,
                String.format("%.1f%%", pct)
            });
        }
    }
}