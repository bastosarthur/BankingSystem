package org.example;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.List;
import java.math.BigDecimal;
import org.hibernate.Session;

import org.mindrot.jbcrypt.BCrypt; // JCrypt library


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
        String tryPassword = validation.tryPassword(input);

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
        long elapsedTime = System.currentTimeMillis() - account.getLoginTime();
        if (elapsedTime >= 300_000) {
            System.out.println("\nSession expired. Please login.");
            account.setLogged(false);
            return;
        }
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
        long elapsedTime = System.currentTimeMillis() - account.getLoginTime();
        if (elapsedTime >= 300_000) {
            System.out.println("\nSession expired. Please login.");
            account.setLogged(false);
            return;
        }
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
        long elapsedTime = System.currentTimeMillis() - account.getLoginTime();
        if (elapsedTime >= 300_000) {
            System.out.println("\nSession expired. Please login.");
            account.setLogged(false);
            return;
        }
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
        long elapsedTime = System.currentTimeMillis() - account.getLoginTime();
        if (elapsedTime >= 300_000) {
            System.out.println("\nSession expired. Please login.");
            account.setLogged(false);
            return;
        }
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
        long elapsedTime = System.currentTimeMillis() - account.getLoginTime();
        if (elapsedTime >= 300_000) {
            System.out.println("\nSession expired. Please login.");
            account.setLogged(false);
            return;
        }
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
        long elapsedTime = System.currentTimeMillis() - account.getLoginTime();
        if (elapsedTime >= 300_000) {
            System.out.println("\nSession expired. Please login.");
            account.setLogged(false);
            return;
        }
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
        long elapsedTime = System.currentTimeMillis() - account.getLoginTime();
        if (elapsedTime >= 300_000) {
            System.out.println("\nSession expired. Please login.");
            account.setLogged(false);
            return;
        }
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
        long elapsedTime = System.currentTimeMillis() - account.getLoginTime();
        if (elapsedTime >= 300_000) {
            System.out.println("\nSession expired. Please login.");
            account.setLogged(false);
            return;
        }
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

    private void loginRegister() {
        System.out.print("\n\nBankingSystem.\n\n1. Login\n2. Register\n0. Exit.\n\nOption: ");
    }

    private void showMenu() {
        System.out.println("""
            
            ===== BANKING MENU =====
            
            1. Check Balance
            2. Deposit
            3. Withdraw
            4. Transfer Money
            5. View All Transactions
            6. Find Transaction by ID
            7. Search Transactions by Date
            8. View All Deposits
            9. View All Withdrawals
            0. Log Out
            
            ========================
            """);
        System.out.print("Choose an option: ");
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Main main = new Main();

        Account account = null;
        boolean exit = false;
        while (!exit) {
            main.loginRegister();
            int option = input.nextInt();
            input.nextLine();

            if (option == 1) {
                while (account == null) {
                    account = main.login(input);
                }
                account.setLoginTime(System.currentTimeMillis());
                while (account.isLogged()) {
                    main.showMenu();
                    int menuOption = input.nextInt();
                    switch (menuOption) {
                        case 1:
                            main.getBalance(input, account);
                            break;
                        case 2:
                            main.deposit(input, account);
                            break;
                        case 3:
                            main.withdraw(input, account);
                            break;
                        case 4:
                            main.transfer(input, account);
                            break;
                        case 5:
                            main.accountTransactions(input, account);
                            break;
                        case 6:
                            main.searchTransaction(input);
                            break;
                        case 7:
                            main.searchTransactionsByDate(input, account);
                            break;
                        case 8:
                            main.listAllDeposits(input, account);
                            break;
                        case 9:
                            main.listAllWithdraws(input, account);
                            break;
                        case 0:
                            account.setLogged(false);
                            break;
                        default:
                            System.out.print("Please choose a valid option: ");
                            break;
                    }
                }
            }
            else if (option == 2) {
                main.createAccount(input);
            }
            else if (option == 0) {
                exit = true;
            } else {
                System.out.print("\nPlease choose a valid option: ");
            }
        }

        input.close();
        HibernateUtil.getSessionFactory().close();



            //main.createAccount(input);
            //Account account = main.login(input);
            //main.deposit(input, account);
            //main.getBalance(input, account);
            //main.withdraw(input, account);
            //main.accountTransactions(input, account);
            //main.transfer(input, account);
            //main.searchTransactionsByDate(input, account);
            //main.listAllDeposits(input, account);
            //main.listAllWithdraws(input, account);

        /*
        String password = "C1g4n06620";
        String password1 = "Mar3s1a6620";

        String storedHash = BCrypt.hashpw(password, BCrypt.gensalt());

        boolean correctPassword = BCrypt.checkpw(password1, storedHash);
        System.out.println(correctPassword);
        */





    }



}