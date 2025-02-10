package org.example.DTOs;
import java.time.LocalDate;

public class Expense {
    private int expenseId;
    private String title;
    private String category;
    private double amount;
    private LocalDate dateIncurred;

    public Expense(int expenseId, String title, String category, double amount, LocalDate dateIncurred) {
        this.expenseId = expenseId;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.dateIncurred = dateIncurred;
    }

    public Expense(String title, String category, double amount, LocalDate dateIncurred) {
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.dateIncurred = dateIncurred;
    }

    public Expense() {}

    public int getExpenseId() {
        return expenseId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDateIncurred() {
        return dateIncurred;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDateIncurred(LocalDate dateIncurred) {
        this.dateIncurred = dateIncurred;
    }

    @Override
    public String toString() {
        return "Expense { ID: " + expenseId + ", Title: " + title + ", Category: "
                + category + ", Amount: " + amount + ", Date: " + dateIncurred + " }";
    }
}
