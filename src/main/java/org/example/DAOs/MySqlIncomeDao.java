package org.example.DAOs;

import org.example.DTOs.Income;
import org.example.Exceptions.DaoException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

public class MySqlIncomeDao extends MySqlDao implements IncomeDaoInterface {
    @Override
    public List<Income> loadAllIncome() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Income> incomeList = new ArrayList<>();

        try {
            // get connection using getConnection() method from MySqlDao.java
            connection = this.getConnection();

            String query = "SELECT * FROM income";
            preparedStatement = connection.prepareStatement(query);

            // execute query to get the result set
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int incomeID = resultSet.getInt("incomeID");
                String title = resultSet.getString("title");
                double amount = resultSet.getDouble("amount");
                Date dateEarned = resultSet.getDate("dateEarned");

                // convert sql date to localDate before adding to expenseList
                Income income = new Income(incomeID, title, amount, dateEarned.toLocalDate());
                incomeList.add(income);
            }
        } catch (SQLException e) {
            throw new DaoException("loadAllIncomeResultSet() " + e.getMessage());
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
                throw new DaoException("loadAllIncome() " + e.getMessage());
            }
        }

        return incomeList;
    }

    @Override
    public double totalEarned() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        double total = 0;

        try {
            connection = this.getConnection();
            String query = "SELECT SUM(amount) AS total FROM income";
            preparedStatement = connection.prepareStatement(query);

            // execute query to get the result set
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getDouble("total");
            }
        } catch (SQLException e) {
            throw new DaoException("totalEarnedResultSet() " + e.getMessage());
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
                throw new DaoException("totalEarned() " + e.getMessage());
            }
        }
        return total;
    }

    @Override
    public void addIncome(Income income) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = this.getConnection();

            String query = "INSERT INTO Income (title, amount, dateEarned) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(query);

            // set parameter values
            preparedStatement.setString(1, income.getTitle());
            preparedStatement.setDouble(2, income.getAmount());
            // convert localDate to sql date before inserting data
            preparedStatement.setDate(3, Date.valueOf(income.getDateEarned()));

            // execute update
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("addIncome() " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    freeConnection(connection);
                }
            } catch (SQLException e) {
                throw new DaoException("addIncome() closing resources " + e.getMessage());
            }
        }
    }

}
