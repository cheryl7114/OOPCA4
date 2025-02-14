package org.example.DAOs;

import org.example.DTOs.Income;
import org.example.Exceptions.DaoException;

import java.util.List;

public interface IncomeDaoInterface {
     List<Income> loadAllIncome() throws DaoException;
     double totalEarned() throws DaoException;
     void addIncome(Income income) throws DaoException;
     void deleteIncome(int id) throws DaoException;
}
