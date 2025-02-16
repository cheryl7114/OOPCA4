package org.example.DAOs;

import org.example.DTOs.Expense;

import org.example.Exceptions.DaoException;

import java.util.List;

public interface ExpenseDaoInterface {
    List<Expense> loadAllExpenses() throws DaoException;
    List<Expense> loadAllExpenses(int year, int month) throws DaoException;
    double totalSpend() throws DaoException;
    void addExpense(Expense expense) throws DaoException;
    void deleteExpense(int id) throws DaoException;
}
