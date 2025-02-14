package org.example.BusinessObjects;

import org.example.DAOs.MySqlExpenseDao;
import org.example.DAOs.ExpenseDaoInterface;
import org.example.DTOs.Expense;

import org.example.DAOs.MySqlIncomeDao;
import org.example.DAOs.IncomeDaoInterface;
import org.example.DTOs.Income;

import org.example.Exceptions.DaoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws DaoException {
        ExpenseDaoInterface expenseDao = new MySqlExpenseDao();
        IncomeDaoInterface incomeDao = new MySqlIncomeDao();
        boolean exit = false;
        int choice;
        Scanner scanner = new Scanner(System.in);

        try {
            do { // loop until user chooses to exit
                // update income & expense list every loop
                List<Expense> expenseList = expenseDao.loadAllExpenses();
                List<Income> incomeList = incomeDao.loadAllIncome();
                displayMenu(); // call displayMenu method
                choice = choiceInput(scanner, 1,4);

                if (choice == 1) { // go to menu for expenses
                    expenseMenu();
                    choice = choiceInput(scanner, 1,3);

                    if (choice == 1) {
                        listAllExpenses(expenseList);
                        listTotalSpend(expenseDao);
                    } else if (choice == 2) {
                        addAnExpense(expenseDao, scanner);
                    } else if (choice == 3) {
                        deleteAnExpense(expenseDao, scanner, expenseList);
                    }
                } else if (choice == 2) { // go to menu for income
                    incomeMenu();
                    choice = choiceInput(scanner, 1,3);

                    if (choice == 1) {
                        listAllIncome(incomeList);
                        listTotalEarned(incomeDao);
                    } else if (choice == 2) {
                        addAnIncome(incomeDao, scanner);
                    }
                } else if (choice == 3) {

                } else if (choice == 4) {
                    exit = true;
                    System.out.println("Bye!");
                }
            } while (!exit);
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public static void displayMenu() {
        System.out.println("\nWelcome to my expense tracker!");
//        System.out.println("1. Show All Expenses & Total Spent");
//        System.out.println("2. Add Expense");
//        System.out.println("3. Delete Expense");
//        System.out.println("4. Show All Income & Total Earned");
//        System.out.println("5. Add Income");
//        System.out.println("6. Delete Income");
        System.out.println("1. Expense Menu");
        System.out.println("2. Income Menu");
        System.out.println("3. Monthly Summary");
        System.out.println("4. Exit");
        System.out.print("Please choose an option from the above (1-4): ");
    }

    public static void expenseMenu() {
        System.out.println("\n1. Show All Expenses & Total Spent");
        System.out.println("2. Add Expense");
        System.out.println("3. Delete Expense");
        System.out.print("Please choose an option from the above (1-3): ");
    }

    public static void incomeMenu() {
        System.out.println("\n1. Show All Income & Total Earned");
        System.out.println("2. Add Income");
        System.out.println("3. Delete Income");
        System.out.print("Please choose an option from the above (1-3): ");
    }

    public static int choiceInput(Scanner scanner, int down, int up) {
        int choice;
        do { // loop until a valid choice is chosen
            while (!scanner.hasNextInt()) { // check if input is not an integer
                System.out.print("Invalid input. Make sure your input is a number: ");
                scanner.next(); // consume invalid input
            }

            choice = scanner.nextInt();

            if (choice < down || choice > up) {
                System.out.print("Please choose a valid option! Try again: ");
            }
        } while (choice<down || choice>up);

        return choice;
    }

    public static void listAllExpenses(List<Expense> expenseList) throws DaoException {
        if (expenseList.isEmpty()) {
            System.out.println("\nNo expenses found!");
        } else {
            System.out.println();
            for (Expense exp : expenseList) {
                System.out.println(exp);
            }
        }
    }

    public static void listTotalSpend(ExpenseDaoInterface expenseDao) throws DaoException {
        System.out.println("Total Spent: " + expenseDao.totalSpend());
    }

    public static void addAnExpense(ExpenseDaoInterface expenseDao, Scanner scanner) throws DaoException {
        scanner.nextLine();

        // validate input before adding expense
        String title = getValidStringInput("Expense title: ",scanner);
        String category = getValidStringInput("Expense category: ",scanner);
        double amount = getValidDouble(scanner);
        LocalDate dateIncurred = getValidDate("Date incurred (YYYY-MM-DD): ", scanner);

        // create Expense object with input data
        Expense expense = new Expense(title, category, amount, dateIncurred);

        // add the expense to the database
        expenseDao.addExpense(expense);

        System.out.println("Expense added successfully!");
    }

    public static void deleteAnExpense(ExpenseDaoInterface expenseDao, Scanner scanner, List<Expense> expenseList) throws DaoException {
        listAllExpenses(expenseList);
        while (true) {
            System.out.print("Enter Expense ID to delete: ");
            if (scanner.hasNextInt()) {
                int id = scanner.nextInt();
//                scanner.nextLine(); // consume newline
                boolean validID = checkValidExpenseID(id,expenseList);

                if (validID) {
                    expenseDao.deleteExpense(id);
                    System.out.println("Expense deleted successfully!");
                    break;
                } else {
                    System.out.println("The entered ID does not exist. Try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
//                scanner.next(); // consume invalid input
            }
        }
    }

    public static boolean checkValidExpenseID(int id, List<Expense> expenseList) {
        for (Expense exp : expenseList) {
            if (exp.getExpenseId() == id) {
                return true;
            }
        }
        return false;
    }

    public static void listAllIncome(List<Income> incomeList) throws DaoException {
        if (incomeList.isEmpty()) {
            System.out.println("\nNo income found!");
        } else {
            System.out.println();
            for (Income inc : incomeList) {
                System.out.println(inc);
            }
        }
    }

    public static void listTotalEarned(IncomeDaoInterface incomeDao) throws DaoException {
        System.out.println("Total Earned: " + incomeDao.totalEarned());
    }

    public static void addAnIncome(IncomeDaoInterface incomeDao, Scanner scanner) throws DaoException {
        scanner.nextLine();

        // validate input before adding income
        String title = getValidStringInput("Income title: ", scanner);
        double amount = getValidDouble(scanner);
        LocalDate dateEarned = getValidDate("Date earned (YYYY-MM-DD): ", scanner);

        // create Income object with input data
        Income income = new Income(title, amount, dateEarned);

        // add the income to the database
        incomeDao.addIncome(income);

        System.out.println("Income added successfully!");
    }

    public static String getValidStringInput(String message, Scanner scanner) {
        String input;
        do {
            System.out.print(message);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid value.");
            }
        } while (input.isEmpty());
        return input;
    }

    public static double getValidDouble(Scanner scanner) {
        double amount;
        while (true) {
            System.out.print("Amount: ");
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                if (amount > 0) {
                    return amount;
                } else {
                    System.out.println("Amount must be a positive number. Try again.");
                }
            } else {
                System.out.println("Invalid amount. Please enter a valid number.");
                scanner.next(); // consume invalid input
            }
        }
    }

    public static LocalDate getValidDate(String message, Scanner scanner) {
        // referenced https://www.tpointtech.com/how-to-accept-date-in-java
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = null;
        while (date == null) {
            System.out.print(message);
            String dateStr = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }
        return date;
    }
}