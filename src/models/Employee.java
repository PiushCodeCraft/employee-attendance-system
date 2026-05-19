package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Employee {
    private int       empId;
    private String    name;
    private String    email;
    private String    phone;
    private int       deptId;
    private String    department;
    private String    designation;
    private String    role;
    private String    password;
    private LocalDate joinDate;

    // ── NEW ──────────────────────────────────────────
    private String    shift;       // "Morning" | "Evening" | "Night"
    private LocalTime shiftStart;  // e.g. 09:00

    public Employee() {}

    public Employee(int empId, String name, String email, String phone,
                    String department, String designation,
                    String role, String password) {
        this.empId       = empId;
        this.name        = name;
        this.email       = email;
        this.phone       = phone;
        this.department  = department;
        this.designation = designation;
        this.role        = role;
        this.password    = password;
        this.joinDate    = LocalDate.now();
    }

    // existing getters / setters
    public int       getEmpId()                      { return empId; }
    public void      setEmpId(int empId)             { this.empId = empId; }
    public String    getName()                       { return name; }
    public void      setName(String name)            { this.name = name; }
    public String    getEmail()                      { return email; }
    public void      setEmail(String email)          { this.email = email; }
    public String    getPhone()                      { return phone; }
    public void      setPhone(String phone)          { this.phone = phone; }
    public int       getDeptId()                     { return deptId; }
    public void      setDeptId(int deptId)           { this.deptId = deptId; }
    public String    getDepartment()                 { return department; }
    public void      setDepartment(String d)         { this.department = d; }
    public String    getDesignation()                { return designation; }
    public void      setDesignation(String d)        { this.designation = d; }
    public String    getRole()                       { return role; }
    public void      setRole(String role)            { this.role = role; }
    public String    getPassword()                   { return password; }
    public void      setPassword(String password)    { this.password = password; }
    public LocalDate getJoinDate()                   { return joinDate; }
    public void      setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    // new shift getters / setters
    public String    getShift()                      { return shift; }
    public void      setShift(String shift)          { this.shift = shift; }
    public LocalTime getShiftStart()                 { return shiftStart; }
    public void      setShiftStart(LocalTime t)      { this.shiftStart = t; }
}