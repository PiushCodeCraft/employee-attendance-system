package services;

import models.LeaveBalance;
import models.LeaveRequest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class LeaveService {

    private static Map<Integer, LeaveBalance> balances = new HashMap<>();
    private static List<LeaveRequest> leaveRequests = new ArrayList<>();
    private static int nextId = 1;

    static {
        balances.put(1, new LeaveBalance(1, 1, 10, 10, 15));
        balances.put(2, new LeaveBalance(2, 2, 8,  10, 12));
        balances.put(3, new LeaveBalance(3, 3, 10, 7,  15));
        balances.put(4, new LeaveBalance(4, 4, 10, 10, 13));
        balances.put(5, new LeaveBalance(5, 5, 9,  10, 15));

        leaveRequests.add(new LeaveRequest(nextId++, 2, "John Doe",   "Sick",   LocalDate.now().minusDays(10), LocalDate.now().minusDays(9),  "Fever",         "Approved", LocalDate.now().minusDays(11)));
        leaveRequests.add(new LeaveRequest(nextId++, 3, "Jane Smith", "Casual", LocalDate.now().plusDays(3),   LocalDate.now().plusDays(4),   "Family event",  "Pending",  LocalDate.now().minusDays(1)));
        leaveRequests.add(new LeaveRequest(nextId++, 4, "Ravi Kumar", "Earned", LocalDate.now().plusDays(7),   LocalDate.now().plusDays(10),  "Vacation",      "Pending",  LocalDate.now()));
        leaveRequests.add(new LeaveRequest(nextId++, 5, "Priya Nair", "Sick",   LocalDate.now().minusDays(3),  LocalDate.now().minusDays(2),  "Cold",          "Rejected", LocalDate.now().minusDays(4)));
    }

    public String applyLeave(LeaveRequest lr) {
        LeaveBalance bal = balances.get(lr.getEmpId());
        if (bal == null) return "Leave balance not found!";
        long days = ChronoUnit.DAYS.between(lr.getFromDate(), lr.getToDate()) + 1;
        int available = switch (lr.getLeaveType()) {
            case "Sick" -> bal.getSickLeave();
            case "Casual" -> bal.getCasualLeave();
            case "Earned" -> bal.getEarnedLeave();
            default -> 0;
        };
        if (days > available) return "Not enough " + lr.getLeaveType() + " leave! Available: " + available;
        lr.setLeaveId(nextId++);
        lr.setStatus("Pending");
        lr.setAppliedOn(LocalDate.now());
        leaveRequests.add(lr);
        return "Leave applied successfully!";
    }

    public String approveLeave(int leaveId, int empId, String leaveType, long days) {
        for (LeaveRequest lr : leaveRequests) {
            if (lr.getLeaveId() == leaveId) {
                lr.setStatus("Approved");
                LeaveBalance bal = balances.get(empId);
                if (bal != null) {
                    switch (leaveType) {
                        case "Sick" -> bal.setSickLeave(bal.getSickLeave() - (int) days);
                        case "Casual" -> bal.setCasualLeave(bal.getCasualLeave() - (int) days);
                        case "Earned" -> bal.setEarnedLeave(bal.getEarnedLeave() - (int) days);
                    }
                }
                return "Leave approved!";
            }
        }
        return "Leave not found.";
    }

    public String rejectLeave(int leaveId) {
        for (LeaveRequest lr : leaveRequests) {
            if (lr.getLeaveId() == leaveId) { lr.setStatus("Rejected"); return "Leave rejected."; }
        }
        return "Leave not found.";
    }

    public List<LeaveRequest> getMyLeaves(int empId) {
        List<LeaveRequest> list = new ArrayList<>();
        for (LeaveRequest lr : leaveRequests) if (lr.getEmpId() == empId) list.add(lr);
        return list;
    }

    public List<LeaveRequest> getPendingLeaves() {
        List<LeaveRequest> list = new ArrayList<>();
        for (LeaveRequest lr : leaveRequests) if ("Pending".equals(lr.getStatus())) list.add(lr);
        return list;
    }

    public List<LeaveRequest> getAllLeaves() { return new ArrayList<>(leaveRequests); }

    public LeaveBalance getLeaveBalance(int empId) {
        return balances.getOrDefault(empId, new LeaveBalance(0, empId, 10, 10, 15));
    }
}
