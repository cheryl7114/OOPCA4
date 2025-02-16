package org.example.DAOs;

import org.example.DTOs.Expense;
import org.example.Exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class MySqlExpenseDao extends MySqlDao implements ExpenseDaoInterface {
    @Override
    public List<Expense> loadAllExpenses() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Expense> expenseList = new ArrayList<>();

        try {
            // get connection using getConnection() method from MySqlDao.java
            connection = this.getConnection();

            String query = "SELECT * FROM expense";
            preparedStatement = connection.prepareStatement(query);

            // execute query to get the result set
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int expenseID = resultSet.getInt("expenseID");
                String title = resultSet.getString("title");
                String category = resultSet.getString("category");
                double amount = resultSet.getDouble("amount");
                Date dateIncurred = resultSet.getDate("dateIncurred");

                // convert sql date to localDate before adding to expenseList
                Expense expense = new Expense(expenseID, title, category, amount, dateIncurred.toLocalDate());
                expenseList.add(expense);
            }
        } catch (SQLException e) {
            throw new DaoException("loadAllExpensesResultSet() " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("loadAllExpenses() " + e.getMessage());
            }
        }

        return expenseList;
    }

    @Override
    public List<Expense> loadAllExpenses(int year, int month) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Expense> expenseList = new ArrayList<>();

        try {
            // get connection using getConnection() method from MySqlDao.java
            connection = this.getConnection();

            String query = "SELECT * FROM expense WHERE YEAR(dateIncurred) = ? AND MONTH(dateIncurred) = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);


            // execute query to get the result set
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int expenseID = resultSet.getInt("expenseID");
                String title = resultSet.getString("title");
                String category = resultSet.getString("category");
                double amount = resultSet.getDouble("amount");
                Date dateIncurred = resultSet.getDate("dateIncurred");

                // convert sql date to localDate before adding to expenseList
                Expense expense = new Expense(expenseID, title, category, amount, dateIncurred.toLocalDate());
                expenseList.add(expense);
            }
        } catch (SQLException e) {
            throw new DaoException("loadAllExpensesResultSet(year,month) " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("loadAllExpenses(year,month) " + e.getMessage());
            }
        }

        return expenseList;
    }

    @Override
    public double totalSpend() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double total = 0;

        try {
            connection = this.getConnection();
            String query = "SELECT SUM(amount) AS total FROM expense";
            preparedStatement = connection.prepareStatement(query);

            // execute query to get the result set
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            throw new DaoException("totalSpendResultSet() " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("totalSpend() " + e.getMessage());
            }
        }
        return total;
    }

    @Override
    public void addExpense(Expense expense) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.getConnection();

            String query = "INSERT INTO Expense (title, category, amount, dateIncurred) VALUES (?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);

            // set parameter values
            preparedStatement.setString(1, expense.getTitle());
            preparedStatement.setString(2, expense.getCategory());
            preparedStatement.setDouble(3, expense.getAmount());
            // convert localDate to sql date before inserting data
            preparedStatement.setDate(4, Date.valueOf(expense.getDateIncurred()));

            // execute update
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("addExpense() " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("addExpense() closing resources " + e.getMessage());
            }
        }
    }

    @Override
    public void deleteExpense(int id) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.getConnection();

            String query = "DELETE FROM expense WHERE expenseID = ?";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id); // set id based on id to delete

            // execute update
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("deleteExpense() " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("deleteExpense() closing resources " + e.getMessage());
            }
        }
    }
}
