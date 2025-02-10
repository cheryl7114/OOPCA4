package org.example.DTOs;

import java.util.Date;

public class Income {
    private int incomeID;
    private String title;
    private double amount;
    private Date dateEarned;

    public Income(int incomeID, String title, double amount, Date date) {
        this.incomeID = incomeID;
        this.title = title;
        this.amount = amount;
        this.dateEarned = date;
    }

    public Income() {}

    public int getIncomeID() {
        return incomeID;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDateEarned() {
        return dateEarned;
    }

    public void setIncomeID(int incomeID) {
        this.incomeID = incomeID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDateEarned(Date dateEarned) {
        this.dateEarned = dateEarned;
    }

    @Override
    public String toString() {
        return "Income { ID: " + incomeID + ", Title: " + title
                + ", Amount: " + amount + ", Date: " + dateEarned + " }";
    }
}
