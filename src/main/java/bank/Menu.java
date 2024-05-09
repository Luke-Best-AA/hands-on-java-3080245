package bank;

import java.util.Scanner;

import javax.security.auth.login.LoginException;

import bank.exceptions.AmountException;

public class Menu {
  private Scanner scanner;

  public static void main(String[] args) {
    System.out.println("Welcome to the bank");

    Menu menu = new Menu();
    menu.scanner = new Scanner(System.in);

    Customer customer = menu.authenticateUser();

    if (customer != null) {
      Account account = DataSource.getAccount(customer.getAccountId());

      menu.showMenu(customer, account);
    }

    menu.scanner.close();
  }

  private Customer authenticateUser() {
    System.out.println("Enter your username:");
    String username = scanner.nextLine();
    System.out.println("Enter your password:");
    String password = scanner.nextLine();

    Customer customer = null;

    try {
      customer = Authenticator.login(username, password);
    } catch (LoginException e) {
      System.out.println(e.getMessage());
      customer = null;
    }

    return customer;
  }

  private void showMenu(Customer customer, Account account) {
    System.out.println("Welcome " + customer.getName());

    int selection = 0;

    while (selection != 4 && customer.isAuthenticated()) {
      System.out.println("=================");
      System.out.println("1. Deposit");
      System.out.println("2. Withdraw");
      System.out.println("3. Check Balance");
      System.out.println("4. Exit");
      System.out.println("=================");

      selection = scanner.nextInt();
      double amount = 0;

      switch (selection) {
        case 1:
          System.out.println("Enter amount to deposit:");
          amount = scanner.nextDouble();

          try {
            account.deposit(amount);
          } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Please try again");
          }

          break;
        case 2:
          System.out.println("Enter amount to withdraw:");
          amount = scanner.nextDouble();

          try {
            account.withdraw(amount);
          } catch (AmountException e) {
            System.out.println(e.getMessage());
            System.out.println("Please try again");
          }

          break;
        case 3:
          System.out.println("You have £" + account.getBalance());
          break;
        case 4:
          Authenticator.logout(customer);
          System.out.println("Goodbye");
          break;
        default:
          System.out.println("Invalid selection");
      }
    }
  }
}
