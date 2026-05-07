# Employee Attendance System — UI Only
## Java Swing | No Database Required

---

## HOW TO RUN

### Option A — IntelliJ IDEA (Recommended)
1. Open IntelliJ → Open → select this folder
2. Right-click `src` → Mark Directory as → Sources Root
3. Open `src/main/Main.java`
4. Click the green ▶ Run button

### Option B — VS Code
1. Install "Extension Pack for Java" extension
2. Open this folder in VS Code
3. Open `src/main/Main.java`
4. Click **Run** above the main method

### Option C — Command Prompt
```cmd
cd "path\to\EAS_UI\src"
dir /s /b *.java > sources.txt
javac -d ..\out @sources.txt
java -cp ..\out main.Main
```

---

## LOGIN ACCOUNTS

| Role     | Email                 | Password  |
|----------|-----------------------|-----------|
| Admin    | admin@company.com     | admin123  |
| Employee | john@company.com      | john123   |
| Employee | jane@company.com      | jane123   |
| Employee | ravi@company.com      | ravi123   |
| Employee | priya@company.com     | priya123  |

---

## FEATURES

### Admin
- Dashboard with live stats (total staff, present, absent, pending leaves)
- Manage Employees — add and delete employees
- View Attendance — filter by date
- Leave Approvals — approve or reject with one click
- Reports — attendance percentage per employee with color coding

### Employee
- Dashboard with leave balance cards
- Mark Attendance — live clock, Check In / Check Out
- My Attendance — history with color-coded status badges
- Apply Leave — form + balance overview side by side

---

## TECH STACK
- Java 17+
- Java Swing (GUI)
- No database — all data is in-memory

## NOTES
- Data resets when you restart the app (no database)
- To add a real database later, replace the service files with DAO versions
