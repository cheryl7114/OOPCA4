package org.example.DAOs;

import org.example.DTOs.Income;
import org.example.Exceptions.DaoException;

import java.util.List;

public interface IncomeDaoInterface {
     List<Income> loadAllIncome() throws DaoException;
     List<Income> loadAllIncome(int year, int month) throws DaoException;
     double totalEarned() throws DaoException;
     double totalEarned(int year, int month) throws DaoException;
     void addIncome(Income income) throws DaoException;
     void deleteIncome(int id) throws DaoException;
}
