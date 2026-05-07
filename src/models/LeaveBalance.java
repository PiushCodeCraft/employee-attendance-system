package models;

public class LeaveBalance {
    private int balanceId;
    private int empId;
    private int sickLeave;
    private int casualLeave;
    private int earnedLeave;

    public LeaveBalance() {}

    public LeaveBalance(int balanceId, int empId, int sickLeave, int casualLeave, int earnedLeave) {
        this.balanceId = balanceId;
        this.empId = empId;
        this.sickLeave = sickLeave;
        this.casualLeave = casualLeave;
        this.earnedLeave = earnedLeave;
    }

    public int getBalanceId() { return balanceId; }
    public void setBalanceId(int balanceId) { this.balanceId = balanceId; }
    public int getEmpId() { return empId; }
    public void setEmpId(int empId) { this.empId = empId; }
    public int getSickLeave() { return sickLeave; }
    public void setSickLeave(int sickLeave) { this.sickLeave = sickLeave; }
    public int getCasualLeave() { return casualLeave; }
    public void setCasualLeave(int casualLeave) { this.casualLeave = casualLeave; }
    public int getEarnedLeave() { return earnedLeave; }
    public void setEarnedLeave(int earnedLeave) { this.earnedLeave = earnedLeave; }
}
