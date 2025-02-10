package org.example.BusinessObjects;

import org.example.DAOs.MySqlExpenseDao;
import org.example.DAOs.ExpenseDaoInterface;
import org.example.DTOs.Expense;
import org.example.Exceptions.DaoException;

import java.time.LocalDate;
import java.util.List;

public class App {
    public static void main(String[] args) {
        ExpenseDaoInterface expenseDao = new MySqlExpenseDao();

        try {
            System.out.println("Test add expense");
            Expense expense = new Expense("Lunch at McDonald's","Food",22.5, LocalDate.of(2025,1,20));
            expenseDao.addExpense(expense);

            System.out.println("Print all expenses: ");
            List <Expense> expenseList = expenseDao.listAllExpenses();

            if (expenseList.isEmpty()) {
                System.out.println("No expenses found");
            } else {
                for (Expense exp : expenseList) {
                    System.out.println(exp);
                }
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
