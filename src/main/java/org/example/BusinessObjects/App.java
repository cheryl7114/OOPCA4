package org.example.BusinessObjects;

import org.example.DAOs.MySqlExpenseDao;
import org.example.DAOs.ExpenseDaoInterface;
import org.example.DTOs.Expense;
import org.example.Exceptions.DaoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws DaoException {
        ExpenseDaoInterface expenseDao = new MySqlExpenseDao();
        boolean exit = false;
        int choice;
        Scanner scanner = new Scanner(System.in);

        try {
            do { // loop until user chooses to exit
                List <Expense> expenseList = expenseDao.loadAllExpenses();
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
        String title;
        do {
            System.out.print("Expense title: ");
            title = scanner.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("Title cannot be empty. Please enter a valid title.");
            }
        } while (title.isEmpty());

        String category;
        do {
            System.out.print("Expense category: ");
            category = scanner.nextLine().trim();
            if (category.isEmpty()) {
                System.out.println("Category cannot be empty. Please enter a valid category.");
            }
        } while (category.isEmpty());

        double amount;
        while (true) {
            System.out.print("Amount: ");
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                scanner.nextLine(); // consume newline
                if (amount > 0) {
                    break;
                } else {
                    System.out.println("Amount must be a positive number. Try again.");
                }
            } else {
                System.out.println("Invalid amount. Please enter a valid number.");
                scanner.next(); // consume invalid input
            }
        }

        // referenced https://www.tpointtech.com/how-to-accept-date-in-java
        LocalDate dateIncurred = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (dateIncurred == null) {
            System.out.print("Date incurred (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine().trim();

            try {
                dateIncurred = LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please enter the date in YYYY-MM-DD format.");
            }
        }

        // create Expense object
        Expense expense = new Expense(0, title, category, amount, dateIncurred);

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
}