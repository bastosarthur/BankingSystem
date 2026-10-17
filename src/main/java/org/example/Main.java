package org.example;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;
import java.math.BigDecimal;


public class Main {

    final InputValidation validation = new InputValidation();
    final AccountService accService = new AccountService();

    private void createAccount(Scanner input) {
        System.out.print("\n\nRegister now!\nCPF number: ");
        String cpf = validation.getCpf(input);
        System.out.print("8-digit numeric password: ");
        String password = validation.getPassword(input);

        try {
            Account account = new Account(cpf);
            int accountNumber = accService.createAccount(account, password);
            if (accountNumber == 0) {
                System.out.println("Error. Please try again.");
                return;
            }
            System.out.println("Account registered.\nYour account number: " + accountNumber);
        } catch (RuntimeException exception) {
            System.out.println("Error. Please try again.\n" + exception.getMessage());
        }
    }

    private Account login(Scanner input) {
        System.out.print("\n\nLogin to your account.\nAccount Number: ");
        int accountNumber = validation.getAccountNumber(input);
        System.out.print("Password: ");
        String tryPassword = validation.getPassword(input);

        try {
            Account account = accService.accountLogin(accountNumber, tryPassword);
            if (account == null) {
                System.out.println("\nWrong account or password.");
                return null;
            }
            System.out.print("\nYou have logged in.\n" + account);
            return account;
        } catch (RuntimeException exception) {
            System.out.println("\nError. Please try again");
            return null;
        }
    }

    public void deposit(Scanner input, Account account) {
        System.out.print("\n\nDepositing.\nAmount: ");
        BigDecimal amount = validation.getAmount(input);
        try {
            int transactionId = accService.deposit(account, amount);
            if (transactionId == -1) {
                System.out.println("\nYou are not logged in.");
                return;
            }
            if (transactionId == -2) {
                System.out.println("\nAccount not found");
                return;
            }
            if (transactionId == -3) {
                System.out.println("\nFailed to confirm transaction.");
                return;
            }

            System.out.println("\nDeposit confirmed.\nTransaction ID: " + transactionId);
        } catch (RuntimeException exception) {
            System.out.println("\nError. Please try again\n" + exception.getMessage());
        }
    }

    public void searchTransaction(Scanner input) {
        System.out.print("\n\nSearching a transaction.\nTransaction ID: ");
        int transactionId = validation.getTransactionId(input);
        try {
            bankTransaction bankTransaction  = accService.getTransaction(transactionId);
            if (bankTransaction == null) {
                System.out.println("\nTransaction not found.");
                return;
            }
            System.out.println(bankTransaction);
        } catch (RuntimeException exception) {
            System.out.println("\nError. Please try again\n" + exception.getMessage());
        }
    }

    public void accountTransactions(Scanner input, Account account) {
        try {
            List<bankTransaction> transactionList = accService.getAccountTransactions(account);
            if (transactionList == null) {
                System.out.println("\nPlease login to continue.");
                return;
            }
            if (transactionList.isEmpty()) {
                System.out.println("\nNo transactions found.");
                return;
            }
            transactionList.forEach(System.out::println);
        } catch (RuntimeException exception) {
            System.out.println("Error. Please try again.\n" + exception.getMessage());
        }
    }

    public void getBalance(Scanner input, Account account) {
        try {
            BigDecimal balance = accService.getBalance(account);
            if (balance == null) {
                System.out.println("\n\nError. Account not found.");
                return;
            }
            System.out.println("\n\nYour account balance: " + balance);
        } catch (RuntimeException exception) {
            System.out.println("\n\nError. Please try again.");
        }
    }

    public void withdraw(Scanner input, Account account) {
        System.out.print("\n\nWithdrawing.\nWithdrawn amount: ");
        BigDecimal amount = validation.getAmount(input);
        try {
            int transactionId = accService.withdraw(account, amount);
            if (transactionId == -1) {
                System.out.println("\nYou are not logged in. Please log in and try again.");
                return;
            }
            if (transactionId == -2) {
                System.out.println("\nAccount not found");
                return;
            }
            if (transactionId == -3) {
                System.out.println("\nNot enough balance.");
                return;
            }
            if (transactionId == -4) {
                System.out.println("\nError. Please try again.");
                return;
            }
            System.out.println("\nWithdraw successful.\nTransaction ID: " + transactionId);
        } catch (RuntimeException exception) {
            System.out.println("\nError. Please try again.");
            return;
        }
    }

    public void transfer(Scanner input, Account account) {
        System.out.print("\n\nTransfers.\nRecipient Account: ");
        int recipientAccount = validation.getAccountNumber(input);
        System.out.print("Amount: ");
        BigDecimal amount = validation.getAmount(input);
        try {
            int transactionId = accService.transfer(account, recipientAccount, amount);
            switch (transactionId) {
                case -1:
                    System.out.println("\nPlease login to proceed.");
                    break;
                case -2:
                    System.out.println("\nSender account not found. Please create an account to proceed.");
                    break;
                case -3:
                    System.out.println("\nInsuficient balance, YOU BROKE FUCK.");
                    break;
                case -4:
                    System.out.println("\nRecipient account not found.");
                    break;
                case 0:
                    System.out.println("\nError. Please try again.");
                    break;
                default:
                    System.out.println("\nTransfer successful.\nTransfer ID: " + transactionId);
                    break;
            }
        } catch (RuntimeException exception) {
            System.out.println("\nError. Please try again.");
        }
    }

    public void searchTransactionsByDate(Scanner input, Account account) {
        System.out.print("\n\nSearching Transactions by date.\nDate: ");
        LocalDate date = validation.getDate(input);
        try {
            List<bankTransaction> transactionsByDate = accService
                    .searchTransactionsByDate(account, date);
            if (transactionsByDate == null) {
                System.out.println("\nPlease login to continue.");
                return;
            }
            if (transactionsByDate.isEmpty()) {
                System.out.println("No transactions were found on this date.");
                return;
            }
            transactionsByDate.forEach(System.out::println);
        } catch (RuntimeException exception) {
            System.out.println("Error. Please try again.\n" + exception.getMessage());
        }
    }

    public void listAllDeposits(Scanner input, Account account) {
        System.out.println("\n\nYour DEPOSIT transaction list: ");
        try {
            List<bankTransaction> depositList = accService.depositList(account);
            if (depositList == null) {
                System.out.println("\nPlease login to continue.");
                return;
            }
            if (depositList.isEmpty()) {
                System.out.println("\nNo deposits found.");
                return;
            }
            depositList.forEach(System.out::println);
        } catch (RuntimeException exception) {
            System.out.println("\nError. Please try again.\n" + exception.getMessage());
        }
    }

    public void listAllWithdraws(Scanner input, Account account) {
        System.out.println("\n\nYour WITHDRAWN list: ");
        try {
            List<bankTransaction> withdrawList = accService.withdrawList(account);
            if (withdrawList == null) {
                System.out.println("\nPlease login to continue");
                return;
            }
            if (withdrawList.isEmpty()) {
                System.out.println("\nNo withdraws found.");
                return;
            }
            withdrawList.forEach(System.out::println);
        } catch (RuntimeException exception) {
            System.out.println("\nError. Please try again");
        }
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Main main = new Main();


        //main.createAccount(input);
        Account account = main.login(input);
        //while (true) {
        //main.deposit(input, account);
        //main.getBalance(input, account);
        //main.withdraw(input, account);
        //main.accountTransactions(input, account);
        //main.transfer(input, account);
        //main.searchTransactionsByDate(input, account);
        //main.listAllDeposits(input, account);
        //main.listAllWithdraws(input, account);


    }



}