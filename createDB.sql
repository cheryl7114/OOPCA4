DROP DATABASE IF EXISTS expenseTracker_CA4;
CREATE DATABASE expenseTracker_CA4;
USE expenseTracker_CA4;

DROP TABLE IF EXISTS Expense;
DROP TABLE IF EXISTS Income;

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

INSERT INTO Expense (title, category, amount, dateIncurred) VALUES
('Groceries at Tesco', 'Food', 120.5, '2024-12-05'),
('Electricity bill', 'Utilities', 65.75, '2025-12-15'),
('Netflix subscription', 'Entertainment', 15.99, '2025-01-15'),
('Lunch at KFC', 'Food', 50, '2025-01-18'),
('Trip to Belfast', 'Travel', 107.25, '2025-02-03'),
('Gym membership', 'Health', 40, '2025-02-10'),
('Valentines date in Dublin', 'Entertainment', 56.8, '2025-02-14');

INSERT INTO Income (title, amount, dateEarned) VALUES
('Salary', 2500, '2024-12-01'),
('Bonus', 300.00, '2025-01-01'),
('Babysitting for Aunt Marie', 100, '2025-01-08'),
('Freelance website project', 500, '2025-02-12');