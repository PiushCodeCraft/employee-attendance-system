package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Attendance {
    private int       attId;
    private int       empId;
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private String    status;
    private double    workingHours;

    // ── NEW ──────────────────────────────────────────
    private double    overtimeHours;

    public Attendance() {}

    public Attendance(int attId, int empId, LocalDate date,
                      LocalTime checkIn, LocalTime checkOut,
                      String status, double workingHours) {
        this.attId        = attId;
        this.empId        = empId;
        this.date         = date;
        this.checkIn      = checkIn;
        this.checkOut     = checkOut;
        this.status       = status;
        this.workingHours = workingHours;
    }

    public int       getAttId()                         { return attId; }
    public void      setAttId(int attId)                { this.attId = attId; }
    public int       getEmpId()                         { return empId; }
    public void      setEmpId(int empId)                { this.empId = empId; }
    public LocalDate getDate()                          { return date; }
    public void      setDate(LocalDate date)            { this.date = date; }
    public LocalTime getCheckIn()                       { return checkIn; }
    public void      setCheckIn(LocalTime checkIn)      { this.checkIn = checkIn; }
    public LocalTime getCheckOut()                      { return checkOut; }
    public void      setCheckOut(LocalTime checkOut)    { this.checkOut = checkOut; }
    public String    getStatus()                        { return status; }
    public void      setStatus(String status)           { this.status = status; }
    public double    getWorkingHours()                  { return workingHours; }
    public void      setWorkingHours(double h)          { this.workingHours = h; }

    // new overtime getters / setters
    public double    getOvertimeHours()                 { return overtimeHours; }
    public void      setOvertimeHours(double h)         { this.overtimeHours = h; }
}