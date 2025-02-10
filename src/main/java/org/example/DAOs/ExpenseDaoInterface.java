package org.example.DAOs;

import java.util.Date;
import org.example.DTOs.Expense;
import org.example.Exceptions.DaoException;

import java.util.List;

public interface ExpenseDaoInterface {
    List<Expense> listAllExpenses() throws DaoException;
    double totalSpend() throws DaoException;
    void addExpense(Expense expense) throws DaoException;
}
