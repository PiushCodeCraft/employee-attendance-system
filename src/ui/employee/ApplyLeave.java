package ui.employee;

import models.Employee;
import models.LeaveBalance;
import models.LeaveRequest;
import services.LeaveService;
import utils.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

public class ApplyLeave extends JPanel {

    private final Employee employee;
    private final LeaveService leaveService;

    public ApplyLeave(Employee employee, LeaveService leaveService) {
        this.employee     = employee;
        this.leaveService = leaveService;
        setOpaque(false);
        setLayout(new BorderLayout(0, 20));
        initUI();
    }

    private void initUI() {
        add(Theme.label("Apply for Leave", Theme.bold(22), Theme.TEXT_PRIMARY), BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(1, 2, 20, 0));
        body.setOpaque(false);

        // Form card
        JPanel formCard = Theme.card();
        formCard.setLayout(new GridBagLayout());
        formCard.setBorder(new EmptyBorder(28, 28, 28, 28));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 4, 8, 4);
        g.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> typeCombo = Theme.comboBox(new String[]{"Sick", "Casual", "Earned"});
        JTextField fromField = Theme.textField(14);
        JTextField toField   = Theme.textField(14);
        JTextField reasonField = Theme.textField(14);

        Object[][] rows = {
            {"Leave Type",             typeCombo},
            {"From Date (yyyy-MM-dd)", fromField},
            {"To Date (yyyy-MM-dd)",   toField},
            {"Reason",                 reasonField}
        };
        for (int i = 0; i < rows.length; i++) {
            g.gridx = 0; g.gridy = i; g.weightx = 0.35;
            formCard.add(Theme.label(rows[i][0].toString(), Theme.bold(12), Theme.TEXT_SECONDARY), g);
            g.gridx = 1; g.weightx = 0.65;
            formCard.add((Component) rows[i][1], g);
        }

        JLabel resultLbl = Theme.label("", Theme.plain(12), Theme.ACCENT_GREEN);
        resultLbl.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridx = 0; g.gridy = rows.length; g.gridwidth = 2; g.insets = new Insets(6, 0, 6, 0);
        formCard.add(resultLbl, g);

        JButton submitBtn = Theme.button("Submit Request", Theme.ACCENT_BLUE);
        submitBtn.setPreferredSize(new Dimension(200, 44));
        g.gridy = rows.length + 1; g.insets = new Insets(12, 0, 0, 0);
        formCard.add(submitBtn, g);

        submitBtn.addActionListener(e -> {
            try {
                LocalDate from = LocalDate.parse(fromField.getText().trim());
                LocalDate to   = LocalDate.parse(toField.getText().trim());
                if (to.isBefore(from)) { resultLbl.setForeground(Theme.ACCENT_RED); resultLbl.setText("To date must be after From date."); return; }
                LeaveRequest lr = new LeaveRequest();
                lr.setEmpId(employee.getEmpId());
                lr.setEmpName(employee.getName());
                lr.setLeaveType((String) typeCombo.getSelectedItem());
                lr.setFromDate(from);
                lr.setToDate(to);
                lr.setReason(reasonField.getText().trim());
                String result = leaveService.applyLeave(lr);
                boolean ok = result.contains("successfully");
                resultLbl.setForeground(ok ? Theme.ACCENT_GREEN : Theme.ACCENT_RED);
                resultLbl.setText(ok ? "✔  " + result : "✕  " + result);
                if (ok) { fromField.setText(""); toField.setText(""); reasonField.setText(""); }
            } catch (Exception ex) {
                resultLbl.setForeground(Theme.ACCENT_RED);
                resultLbl.setText("✕  Invalid date. Use yyyy-MM-dd");
            }
        });

        body.add(formCard);

        // Balance card
        JPanel balCard = Theme.card();
        balCard.setLayout(new GridBagLayout());
        balCard.setBorder(new EmptyBorder(28, 28, 28, 28));
        GridBagConstraints gb = new GridBagConstraints();
        gb.insets = new Insets(8, 0, 8, 0);
        gb.fill = GridBagConstraints.HORIZONTAL;
        gb.gridwidth = 1;

        gb.gridy = 0; gb.gridwidth = 2;
        balCard.add(Theme.label("Your Leave Balance", Theme.bold(16), Theme.TEXT_PRIMARY), gb);

        LeaveBalance bal = leaveService.getLeaveBalance(employee.getEmpId());
        if (bal != null) {
            String[][] balRows = {
                {"🤒  Sick Leave",   String.valueOf(bal.getSickLeave())   + " days"},
                {"☀  Casual Leave",  String.valueOf(bal.getCasualLeave()) + " days"},
                {"🌟  Earned Leave", String.valueOf(bal.getEarnedLeave()) + " days"}
            };
            for (int i = 0; i < balRows.length; i++) {
                gb.gridy = i + 1; gb.gridwidth = 1;
                gb.gridx = 0; gb.weightx = 0.6;
                balCard.add(Theme.label(balRows[i][0], Theme.plain(13), Theme.TEXT_SECONDARY), gb);
                gb.gridx = 1; gb.weightx = 0.4;
                JLabel v = Theme.label(balRows[i][1], Theme.bold(14), Theme.ACCENT_GREEN);
                v.setHorizontalAlignment(SwingConstants.RIGHT);
                balCard.add(v, gb);
            }
        }

        body.add(balCard);
        add(body, BorderLayout.CENTER);
    }
}
