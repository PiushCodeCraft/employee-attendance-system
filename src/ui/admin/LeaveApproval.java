package ui.admin;

import java.awt.*;
import java.time.temporal.ChronoUnit;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.LeaveRequest;
import services.LeaveService;
import utils.Theme;

public class LeaveApproval extends JPanel {

    private final LeaveService    leaveService;
    private DefaultTableModel     tableModel;
    private List<LeaveRequest>    leaveList;
    private JTable                table;

    public LeaveApproval(LeaveService leaveService) {
        this.leaveService = leaveService;
        setOpaque(false);
        setLayout(new BorderLayout(0, 16));
        initUI();
        loadPending();
    }

    private void initUI() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(Theme.label("Leave Approvals", Theme.bold(22), Theme.TEXT_PRIMARY),
                   BorderLayout.WEST);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        JButton approveBtn = Theme.button("✔  Approve",  Theme.ACCENT_GREEN);
        JButton rejectBtn  = Theme.button("✖  Reject",   Theme.ACCENT_RED);
        JButton pendingBtn = Theme.button("Pending",     Theme.ACCENT_ORANGE);
        JButton allBtn     = Theme.button("All Leaves",  new Color(55, 65, 90));
        approveBtn.addActionListener(e -> handleAction("Approved"));
        rejectBtn.addActionListener(e  -> handleAction("Rejected"));
        pendingBtn.addActionListener(e -> loadPending());
        allBtn.addActionListener(e     -> loadAll());
        btnRow.add(pendingBtn); btnRow.add(allBtn);
        btnRow.add(rejectBtn);  btnRow.add(approveBtn);
        header.add(btnRow, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        String[] cols = {"ID","Employee","Type","From","To","Days","Reason","Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getColumnModel().getColumn(7).setCellRenderer(
            (t, v, s, f, row, col) -> Theme.badge(v != null ? v.toString() : ""));

        JScrollPane sp = new JScrollPane(table);
        Theme.styleScrollPane(sp);
        JPanel card = Theme.card();
        card.setLayout(new BorderLayout());
        card.add(sp, BorderLayout.CENTER);
        add(card, BorderLayout.CENTER);
    }

    private void loadPending() { leaveList = leaveService.getPendingLeaves(); populate(); }
    private void loadAll()     { leaveList = leaveService.getAllLeaves();     populate(); }

    private void populate() {
        tableModel.setRowCount(0);
        for (LeaveRequest lr : leaveList) {
            long days = ChronoUnit.DAYS.between(lr.getFromDate(), lr.getToDate()) + 1;
            tableModel.addRow(new Object[]{
                lr.getLeaveId(),
                lr.getEmpName() != null ? lr.getEmpName() : "Emp #" + lr.getEmpId(),
                lr.getLeaveType(),
                lr.getFromDate(), lr.getToDate(), days,
                lr.getReason(), lr.getStatus()
            });
        }
    }

    private void handleAction(String action) {
        int row = table.getSelectedRow();
        if (row < 0 || row >= leaveList.size()) {
            JOptionPane.showMessageDialog(this, "Select a leave request.");
            return;
        }
        LeaveRequest lr   = leaveList.get(row);
        long         days = ChronoUnit.DAYS.between(lr.getFromDate(), lr.getToDate()) + 1;
        String result = "Approved".equals(action)
            ? leaveService.approveLeave(lr.getLeaveId(), lr.getEmpId(), lr.getLeaveType(), days)
            : leaveService.rejectLeave(lr.getLeaveId());
        JOptionPane.showMessageDialog(this, result);
        loadPending();
    }
}