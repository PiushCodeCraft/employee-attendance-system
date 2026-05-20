package ui.admin;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.Employee;
import services.AttendanceService;
import services.EmployeeService;
import services.HolidayService;
import services.LeaveService;
import ui.LoginFrame;
import ui.SidebarPanel;
import utils.Theme;

public class AdminDashboard extends JFrame {

    private final Employee         admin;
    private final EmployeeService  empService    = new EmployeeService();
    private final AttendanceService attService   = new AttendanceService();
    private final LeaveService     leaveService  = new LeaveService();
    private final HolidayService   holidayService= new HolidayService();  // ← NEW

    private JPanel      contentArea;
    private SidebarPanel sidebar;

    public AdminDashboard(Employee admin) {
        this.admin = admin;
        setTitle("EAS — Admin");
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
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
        sidebar.addItem("⊞",  "Dashboard",      () -> showPanel(buildHomePanel()));
        sidebar.addItem("👥", "Employees",       () -> showPanel(new ManageEmployees(empService)));
        sidebar.addItem("📋", "Attendance",      () -> showPanel(new ViewAttendance(attService)));
        sidebar.addItem("🗓", "Leave Approvals", () -> showPanel(new LeaveApproval(leaveService)));
        sidebar.addItem("📊", "Reports",         () -> showPanel(new ReportsPanel(empService, attService)));
        // ── NEW sidebar items ─────────────────────────────────────────────────
        sidebar.addItem("🕒", "Shift Management", () -> showPanel(new ShiftManagement(empService)));
        sidebar.addItem("📅", "Holidays",         () -> showPanel(new HolidayPanel(holidayService)));
        sidebar.addItem("⏱",  "Overtime",         () -> showPanel(new OvertimePanel(empService, attService)));
        // ─────────────────────────────────────────────────────────────────────
        sidebar.addItem("⏻",  "Logout",           this::logout);
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
        left.add(Theme.label("EAS",         Theme.bold(20),  Theme.ACCENT_BLUE));
        left.add(Box.createHorizontalStrut(8));
        left.add(Theme.label("Admin Panel", Theme.plain(13), Theme.TEXT_SECONDARY));
        bar.add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
        right.add(Theme.label("● " + LocalDate.now(), Theme.plain(12), Theme.TEXT_MUTED));
        right.add(Theme.label(admin.getName(),         Theme.bold(13),  Theme.TEXT_PRIMARY));
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildHomePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 20));
        p.setOpaque(false);
        p.add(Theme.label("Dashboard Overview", Theme.bold(22), Theme.TEXT_PRIMARY), BorderLayout.NORTH);

        int total   = empService.getAllEmployees().size();
        int present = attService.getAttendanceByDate(LocalDate.now()).size();
        int pending = leaveService.getPendingLeaves().size();
        int absent  = total - present;

        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 0));
        stats.setOpaque(false);
        stats.add(buildStatCard("Total Staff",    String.valueOf(total),   Theme.ACCENT_BLUE,   "👥"));
        stats.add(buildStatCard("Present Today",  String.valueOf(present), Theme.ACCENT_GREEN,  "✔"));
        stats.add(buildStatCard("Absent Today",   String.valueOf(absent),  Theme.ACCENT_RED,    "✖"));
        stats.add(buildStatCard("Pending Leaves", String.valueOf(pending), Theme.ACCENT_ORANGE, "🗓"));

        JPanel body = new JPanel(new BorderLayout(0, 16));
        body.setOpaque(false);
        body.add(stats, BorderLayout.NORTH);

        JLabel recTitle = Theme.label("Recent Attendance", Theme.bold(16), Theme.TEXT_PRIMARY);
        body.add(recTitle, BorderLayout.CENTER);

        String[]   cols = {"Emp ID", "Date", "Check In", "Check Out", "Status"};
        Object[][] data = attService.getAllAttendance().stream().limit(8).map(a -> new Object[]{
            a.getEmpId(),
            utils.DateTimeUtil.formatDate(a.getDate()),
            utils.DateTimeUtil.formatTime(a.getCheckIn()),
            utils.DateTimeUtil.formatTime(a.getCheckOut()),
            a.getStatus()
        }).toArray(Object[][]::new);

        JTable table = new JTable(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        Theme.styleTable(table);
        table.getColumnModel().getColumn(4).setCellRenderer(
            (t,v,s,f,row,col) -> Theme.badge(v!=null?v.toString():""));

        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        body.add(sp, BorderLayout.SOUTH);

        p.add(body, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildStatCard(String title, String value, Color accent, String icon) {
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(Theme.label(title, Theme.plain(13), Theme.TEXT_SECONDARY), BorderLayout.WEST);
        top.add(Theme.label(icon,  Theme.plain(18), accent),               BorderLayout.EAST);
        card.add(top,                                                     BorderLayout.NORTH);
        card.add(Theme.label(value, Theme.bold(36), accent),              BorderLayout.CENTER);
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