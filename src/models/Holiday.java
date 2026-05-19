package models;

import java.time.LocalDate;

public class Holiday {
    private int id;
    private String name;
    private LocalDate date;

    public Holiday() {}

    public Holiday(int id, String name, LocalDate date) {
        this.id   = id;
        this.name = name;
        this.date = date;
    }

    public int       getId()              { return id; }
    public void      setId(int id)        { this.id = id; }
    public String    getName()            { return name; }
    public void      setName(String n)    { this.name = n; }
    public LocalDate getDate()            { return date; }
    public void      setDate(LocalDate d) { this.date = d; }
}