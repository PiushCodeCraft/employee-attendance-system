package ui.employee;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.Employee;
import services.AttendanceService;
import utils.DateTimeUtil;
import utils.Theme;

public class MarkAttendance extends JPanel {

    private final Employee          employee;
    private final AttendanceService attService;
    private JLabel resultLabel;
    private JLabel timeLabel;
    private Timer  clockTimer;

    public MarkAttendance(Employee employee, AttendanceService attService) {
        this.employee   = employee;
        this.attService = attService;
        setOpaque(false);
        setLayout(new BorderLayout(0, 20));
        initUI();
    }

    private void initUI() {
        add(Theme.label("Mark Attendance", Theme.bold(22), Theme.TEXT_PRIMARY),
            BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);

        JPanel card = Theme.card();
        card.setLayout(new GridBagLayout());
        card.setBorder(new EmptyBorder(40, 60, 40, 60));
        card.setPreferredSize(new Dimension(520, 380));

        GridBagConstraints g = new GridBagConstraints();
        g.insets    = new Insets(10, 0, 10, 0);
        g.fill      = GridBagConstraints.HORIZONTAL;
        g.gridwidth = 2;

        // Date
        g.gridy = 0;
        card.add(Theme.label("📅  " + DateTimeUtil.formatDate(DateTimeUtil.today()),
                 Theme.bold(15), Theme.TEXT_SECONDARY), g);

        // Live clock
        timeLabel = Theme.label("", Theme.bold(42), Theme.TEXT_PRIMARY);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        updateClock();
        clockTimer = new Timer(1000, e -> updateClock());
        clockTimer.start();
        g.gridy = 1;
        card.add(timeLabel, g);

        // Status/result message
        resultLabel = Theme.label("", Theme.plain(14), Theme.ACCENT_GREEN);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridy = 2;
        card.add(resultLabel, g);

        // ── Check In / Check Out row ──────────────────────────────────────────
        g.gridwidth = 1; g.gridy = 3;
        JButton checkIn  = Theme.button("✔  Check In",  Theme.ACCENT_GREEN);
        JButton checkOut = Theme.button("✖  Check Out", Theme.ACCENT_RED);
        checkIn .setPreferredSize(new Dimension(180, 48));
        checkOut.setPreferredSize(new Dimension(180, 48));
        g.gridx = 0; g.insets = new Insets(20, 0, 0, 8);
        card.add(checkIn,  g);
        g.gridx = 1; g.insets = new Insets(20, 8, 0, 0);
        card.add(checkOut, g);

        checkIn.addActionListener(e -> {
            String res = attService.checkIn(employee.getEmpId());
            boolean ok = res.contains("successful");
            resultLabel.setForeground(ok ? Theme.ACCENT_GREEN : Theme.ACCENT_RED);
            resultLabel.setText(res);
        });
        checkOut.addActionListener(e -> {
            String res = attService.checkOut(employee.getEmpId());
            boolean ok = res.contains("successful");
            resultLabel.setForeground(ok ? Theme.ACCENT_GREEN : Theme.ACCENT_RED);
            resultLabel.setText(res);
        });

        // ── NEW: Work-from-Home button ────────────────────────────────────────
        g.gridwidth = 2; g.gridy = 4; g.gridx = 0;
        g.insets = new Insets(10, 0, 0, 0);
        JButton wfhBtn = Theme.button("🏠  Work from Home", Theme.ACCENT_PURPLE);
        wfhBtn.setPreferredSize(new Dimension(380, 46));
        card.add(wfhBtn, g);

        wfhBtn.addActionListener(e -> {
            String res = attService.markWFH(employee.getEmpId());
            boolean ok = res.contains("marked");
            resultLabel.setForeground(ok ? Theme.ACCENT_PURPLE : Theme.ACCENT_RED);
            resultLabel.setText(res);
        });

        center.add(card);
        add(center, BorderLayout.CENTER);
    }

    private void updateClock() {
        timeLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
    }

    @Override public void removeNotify() {
        super.removeNotify();
        if (clockTimer != null) clockTimer.stop();
    }
}