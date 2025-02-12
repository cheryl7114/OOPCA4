package org.example.BusinessObjects;

import org.example.DAOs.MySqlExpenseDao;
import org.example.DAOs.ExpenseDaoInterface;
import org.example.DTOs.Expense;
import org.example.Exceptions.DaoException;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        ExpenseDaoInterface expenseDao = new MySqlExpenseDao();
        boolean exit = false;
        int choice = 0;
        Scanner scanner = new Scanner(System.in);

        try {
            do { // loop until user chooses to exit
                displayMenu(); // call displayMenu method
                choice = choiceInput(scanner, 1,4);

                if (choice == 1) {
                    expenseMenu();
                    choice = choiceInput(scanner, 1,3);

                    if (choice == 1) {
                        listAllExpensesAndSpent(expenseDao);
                    } else if (choice == 2) {

                    }
                } else if (choice == 2) {
                    incomeMenu();
                    choice = choiceInput(scanner, 1,3);
                } else if (choice == 3) {

                } else if (choice == 4) {
                    exit = true;
                    System.out.println("Bye!");
                }
            } while (!exit);
//            Expense expense = new Expense("Lunch at McDonald's","Food",22.5, LocalDate.of(2025,1,20));
//            expenseDao.addExpense(expense);

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
        System.out.println("\n4. Show All Income & Total Earned");
        System.out.println("5. Add Income");
        System.out.println("6. Delete Income");
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

    public static void listAllExpensesAndSpent(ExpenseDaoInterface expenseDao) throws DaoException {
        List <Expense> expenseList = expenseDao.listAllExpenses();
        if (expenseList.isEmpty()) {
            System.out.println("\nNo expenses found!");
        } else {
            System.out.println("\n");
            for (Expense exp : expenseList) {
                System.out.println(exp);
            }
            System.out.println("Total Spent: " + expenseDao.totalSpend());
        }
    }
}