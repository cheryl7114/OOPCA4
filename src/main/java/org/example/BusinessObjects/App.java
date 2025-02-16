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
        List<Expense> expenseList = expenseDao.loadAllExpenses();
        List<Income> incomeList = incomeDao.loadAllIncome();
        final int EXPENSE_MENU = 1;
        final int INCOME_MENU = 2;
        final int SUMMARY_MENU = 3;
        final int EXIT_MENU = 4;
        Scanner scanner = new Scanner(System.in);

        try {
            do { // loop until user chooses to exit
                displayMenu(); // call displayMenu method
                choice = choiceInput(scanner, 1,4);

                if (choice == EXPENSE_MENU) { // go to menu for expenses
                    expenseMenu();
                    choice = choiceInput(scanner, 1,3);

                    if (choice == 1) {
                        displayExpenses(expenseList);
                        listTotalSpend(expenseDao);
                    } else if (choice == 2) {
                        addAnExpense(expenseDao, scanner);
                        expenseList = expenseDao.loadAllExpenses(); // update expenseList after modification
                    } else if (choice == 3) {
                        deleteAnExpense(expenseDao, scanner, expenseList);
                        expenseList = expenseDao.loadAllExpenses(); // update expenseList after modification
                    }
                } else if (choice == INCOME_MENU) { // go to menu for income
                    incomeMenu();
                    choice = choiceInput(scanner, 1,3);

                    if (choice == 1) {
                        displayIncome(incomeList);
                        listTotalEarned(incomeDao);
                    } else if (choice == 2) {
                        addAnIncome(incomeDao, scanner);
                        incomeList = incomeDao.loadAllIncome(); // update incomeList after modification
                    } else if (choice == 3) {
                        deleteAnIncome(incomeDao, scanner, incomeList);
                        incomeList = incomeDao.loadAllIncome(); // update incomeList after modification
                    }
                } else if (choice == SUMMARY_MENU) {
                    monthlySummary(incomeDao, expenseDao, scanner);
                } else if (choice == EXIT_MENU) {
                    exit = true;
                    System.out.println("Bye!");
                }
            } while (!exit);
        } catch (DaoException e) {
            e.printStackTrace();
        }
        scanner.close();
    }

    public static void displayMenu() {
        System.out.println("\nWelcome to my expense tracker!");
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
            choice = getValidInt(scanner);

            if (choice < down || choice > up) {
                System.out.print("Please choose a valid option (" + down + "-" + up + ")! Try again: ");
            }
        } while (choice<down || choice>up);

        return choice;
    }

    public static void displayExpenses(List<Expense> expenseList) throws DaoException {
        System.out.println("\nExpense List:");
        if (expenseList.isEmpty()) {
            System.out.println("No expenses found!");
        } else {
            for (Expense exp : expenseList) {
                System.out.println(exp);
            }
        }
    }

    public static void listTotalSpend(ExpenseDaoInterface expenseDao) throws DaoException {
        System.out.println("Total Spent: " + String.format("%.2f", expenseDao.totalSpend()));
    }

    public static void addAnExpense(ExpenseDaoInterface expenseDao, Scanner scanner) throws DaoException {
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
        displayExpenses(expenseList);
        System.out.print("Enter Expense ID to delete: ");
        while (true) {
            int id = getValidInt(scanner);
            boolean validID = checkValidExpenseID(id,expenseList);

            if (validID) {
                expenseDao.deleteExpense(id);
                System.out.println("Expense deleted successfully!");
                break;
            } else {
                System.out.print("The entered ID does not exist. Try again: ");
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

    public static void displayIncome(List<Income> incomeList) throws DaoException {
        System.out.println("\nIncome List:");
        if (incomeList.isEmpty()) {
            System.out.println("No income found!");
        } else {
            for (Income inc : incomeList) {
                System.out.println(inc);
            }
        }
    }

    public static void listTotalEarned(IncomeDaoInterface incomeDao) throws DaoException {
        System.out.println("Total Earned: " + String.format("%.2f", incomeDao.totalEarned()));
    }

    public static void addAnIncome(IncomeDaoInterface incomeDao, Scanner scanner) throws DaoException {
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

    public static void deleteAnIncome(IncomeDaoInterface incomeDao, Scanner scanner, List<Income> incomeList) throws DaoException {
        displayIncome(incomeList);
        System.out.print("Enter Income ID to delete: ");
        while (true) {
            int id = getValidInt(scanner);
            boolean validID = checkValidIncomeID(id,incomeList);

            if (validID) {
                incomeDao.deleteIncome(id);
                System.out.println("Income deleted successfully!");
                break;
            } else {
                System.out.print("The entered ID does not exist. Try again: ");
            }
        }
    }

    public static boolean checkValidIncomeID(int id, List<Income> incomeList) {
        for (Income income : incomeList) {
            if (income.getIncomeID() == id) {
                return true;
            }
        }
        return false;
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
        LocalDate date;
        LocalDate currentDate = LocalDate.now();

        while (true) {
            System.out.print(message);
            String dateStr = scanner.nextLine().trim();
            try {
                date = LocalDate.parse(dateStr, formatter);

                if (date.isAfter(currentDate)) {  // check if date is in the future
                    System.out.println("The entered date cannot be in the future. Try again.");
                } else {
                    return date;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }
    }

    public static int getValidInt(Scanner scanner) {
        int num;
        while (true) {
            if (scanner.hasNextInt()) {
                num = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (num > 0) {
                    return num;
                } else {
                    System.out.print("Input must be a positive number. Try again: ");
                }
            } else {
                System.out.print("Invalid input. Please enter a valid number: ");
                scanner.next(); // consume invalid input
            }
        }
    }

    public static int getValidYear(Scanner scanner) {
        // referenced https://javabeat.net/get-current-year-java/#:~:text=In%20the%20main()%20method%2C%20first%2C%20we%20create%20an%20object,the%20current%20year%20in%20Java.
        int currentYear = LocalDate.now().getYear();
        int year;
        while (true) {
            if (scanner.hasNextInt()) {
                year = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (year > 2000 && year <= currentYear) {  // ensure it's an acceptable year
                    return year;
                } else {
                    System.out.print("Invalid year. Enter a year between 2000 and " + currentYear + ": ");
                }
            } else {
                System.out.print("Invalid input. Please enter a valid year: ");
                scanner.next(); // consume invalid input
            }
        }
    }

    public static int getValidMonth(Scanner scanner) {
        int inputMonth;
        int currentMonth = LocalDate.now().getMonthValue();
        while (true) {
            if (scanner.hasNextInt()) {
                inputMonth = scanner.nextInt();
                scanner.nextLine(); // consume newline
                if (inputMonth < 1 || inputMonth > 12) {
                    System.out.print("Invalid month. Please enter a number between 1 and 12: ");
                } else if (inputMonth > currentMonth) {
                    System.out.print("Input month exceeds current month. Try again: ");
                } else {
                    return inputMonth;
                }
            } else {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next(); // consume invalid input
            }
        }
    }

    public static void monthlySummary(IncomeDaoInterface incomeDao, ExpenseDaoInterface expenseDao, Scanner scanner) throws DaoException {
        System.out.print("Enter year to check (e.g. 2025): ");
        int year = getValidYear(scanner);
        System.out.print("Enter month (1-12): ");
        int month = getValidMonth(scanner);

        List<Income> incomes = incomeDao.loadAllIncome(year, month);
        List<Expense> expenses = expenseDao.loadAllExpenses(year, month);
        displayIncome(incomes);
        displayExpenses(expenses);

        double totalIncome = incomeDao.totalEarned(year, month);
        double totalExpenditure = expenseDao.totalSpend(year, month);
        double totalLeftOver = totalIncome - totalExpenditure;

        System.out.println("\nMonthly Report for " + year + "-" + month);
        System.out.println("--------------------------------------");
        System.out.println("Total Income: " + totalIncome);
        System.out.println("Total Expenses: " + totalExpenditure);
        System.out.println("Remaining Balance: " + totalLeftOver);
    }
}