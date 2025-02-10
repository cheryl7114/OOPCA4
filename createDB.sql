DROP DATABASE IF EXISTS 'expenseTracker_CA4';
CREATE DATABASE 'expenseTracker_CA4';
USE 'expenseTracker_CA4';
DROP TABLE IF EXISTS 'Expense';
DROP TABLE IF EXISTS 'Income';

CREATE TABLE Expense (
    expenseID INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    amount DOUBLE NOT NULL,
    dateIncurred DATE NOT NULL
);

CREATE TABLE Income (
    incomeID INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    amount DOUBLE NOT NULL,
    dateEarned DATE NOT NULL
);
