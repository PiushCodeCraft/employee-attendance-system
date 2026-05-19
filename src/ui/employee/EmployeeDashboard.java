package ui.employee;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.Employee;
import models.Holiday;
import models.LeaveBalance;
import services.AttendanceService;
import services.HolidayService;
import services.LeaveService;
import ui.LoginFrame;
import ui.SidebarPanel;
import utils.Theme;

public class EmployeeDashboard extends JFrame {

    private final Employee          employee;
    private final AttendanceService attService    = new AttendanceService();
    private final LeaveService      leaveService  = new LeaveService();
    private final HolidayService    holidayService= new HolidayService();   // ← NEW

    private JPanel       contentArea;
    private SidebarPanel sidebar;

    public EmployeeDashboard(Employee employee) {
        this.employee = employee;
        setTitle("EAS — " + employee.getName());
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 550));
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Theme.BG_DARK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.add(buildTopBar(), BorderLayout.NORTH);

        sidebar = new SidebarPanel();
        sidebar.addItem("⊞", "My Dashboard",    () -> showPanel(buildHomePanel()));
        sidebar.addItem("🕐","Mark Attendance",  () -> showPanel(new MarkAttendance(employee, attService)));
        sidebar.addItem("📋","My Attendance",    () -> showPanel(new ViewMyAttendance(employee, attService)));
        sidebar.addItem("🗓","Apply Leave",      () -> showPanel(new ApplyLeave(employee, leaveService)));
        sidebar.addItem("⏻", "Logout",           this::logout);
        sidebar.select(0);
        root.add(sidebar, BorderLayout.WEST);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setOpaque(false);
        contentArea.setBorder(new EmptyBorder(20, 20, 20, 20));
        showPanel(buildHomePanel());
        root.add(contentArea, BorderLayout.CENTER);
        setContentPane(root);
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Theme.BG_SIDEBAR);
                g.fillRect(0, 0, getWidth(), getHeight());
                ((Graphics2D) g).setColor(Theme.BORDER_COLOR);
                ((Graphics2D) g).drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
            }
        };
        bar.setOpaque(false);
        bar.setPreferredSize(new Dimension(0, 56));
        bar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        left.setOpaque(false);
        left.add(Theme.label("EAS",              Theme.bold(20),  Theme.ACCENT_BLUE));
        left.add(Box.createHorizontalStrut(8));
        left.add(Theme.label("Employee Portal",  Theme.plain(13), Theme.TEXT_SECONDARY));
        bar.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(Theme.label(
            LocalDate.now().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")),
            Theme.plain(12), Theme.TEXT_MUTED));
        right.add(Theme.label(employee.getName(), Theme.bold(13), Theme.TEXT_PRIMARY));
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildHomePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setOpaque(false);
        p.add(Theme.label("Hello, " + employee.getName() + " 👋",
              Theme.bold(22), Theme.TEXT_PRIMARY), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 16));
        body.setOpaque(false);

        // ── leave balance cards ───────────────────────────────────────────────
        LeaveBalance bal = leaveService.getLeaveBalance(employee.getEmpId());
        JPanel cards = new JPanel(new GridLayout(1, 3, 16, 0));
        cards.setOpaque(false);
        if (bal != null) {
            cards.add(buildLeaveCard("Sick Leave",   bal.getSickLeave(),   Theme.ACCENT_BLUE,   "🤒"));
            cards.add(buildLeaveCard("Casual Leave", bal.getCasualLeave(), Theme.ACCENT_PURPLE, "☀"));
            cards.add(buildLeaveCard("Earned Leave", bal.getEarnedLeave(), Theme.ACCENT_GREEN,  "🌟"));
        }
        body.add(cards, BorderLayout.NORTH);

        // ── middle row: upcoming holidays (NEW) + recent attendance ──────────
        JPanel midRow = new JPanel(new GridLayout(1, 2, 16, 0));
        midRow.setOpaque(false);

        // upcoming holidays panel
        JPanel holCard = Theme.card();
        holCard.setLayout(new BorderLayout(0, 8));
        holCard.setBorder(new EmptyBorder(16, 16, 16, 16));
        holCard.add(Theme.label("Upcoming Holidays", Theme.bold(14), Theme.TEXT_PRIMARY),
                    BorderLayout.NORTH);
        JPanel holList = new JPanel();
        holList.setLayout(new BoxLayout(holList, BoxLayout.Y_AXIS));
        holList.setOpaque(false);
        List<Holiday> upcoming = holidayService.getUpcoming();
        if (upcoming.isEmpty()) {
            holList.add(Theme.label("No upcoming holidays.", Theme.plain(12), Theme.TEXT_MUTED));
        } else {
            for (Holiday h : upcoming.stream().limit(4).toList()) {
                JPanel row = new JPanel(new BorderLayout());
                row.setOpaque(false);
                row.setBorder(new EmptyBorder(4, 0, 4, 0));
                row.add(Theme.label("🗓  " + h.getName(),
                        Theme.bold(12), Theme.TEXT_PRIMARY), BorderLayout.WEST);
                row.add(Theme.label(h.getDate().toString(),
                        Theme.plain(11), Theme.ACCENT_ORANGE), BorderLayout.EAST);
                holList.add(row);
            }
        }
        holCard.add(holList, BorderLayout.CENTER);
        midRow.add(holCard);

        // recent attendance mini-table
        String[]   cols = {"Date","Check In","Check Out","Status","Hours"};
        Object[][] data = attService.getMyAttendance(employee.getEmpId())
            .stream().limit(5).map(a -> new Object[]{
                utils.DateTimeUtil.formatDate(a.getDate()),
                utils.DateTimeUtil.formatTime(a.getCheckIn()),
                utils.DateTimeUtil.formatTime(a.getCheckOut()),
                a.getStatus(),
                String.format("%.2f hrs", a.getWorkingHours())
            }).toArray(Object[][]::new);

        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        Theme.styleTable(table);
        table.getColumnModel().getColumn(3).setCellRenderer(
            (t, v, s, f, row, col) -> Theme.badge(v != null ? v.toString() : ""));
        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        JPanel attCard = Theme.card();
        attCard.setLayout(new BorderLayout());
        attCard.add(sp, BorderLayout.CENTER);
        midRow.add(attCard);

        body.add(midRow, BorderLayout.CENTER);
        p.add(body, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildLeaveCard(String title, int value, Color accent, String icon) {
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(Theme.label(title, Theme.plain(13), Theme.TEXT_SECONDARY), BorderLayout.WEST);
        top.add(Theme.label(icon,  Theme.plain(18), accent),               BorderLayout.EAST);
        JLabel val = Theme.label(String.valueOf(value), Theme.bold(40), accent);
        JLabel sub = Theme.label("days remaining",      Theme.plain(11), Theme.TEXT_MUTED);
        card.add(top, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        card.add(sub, BorderLayout.SOUTH);
        return card;
    }

    private void showPanel(JPanel panel) {
        contentArea.removeAll();
        contentArea.add(panel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void logout() {
        dispose();
        new LoginFrame().setVisible(true);
    }
}