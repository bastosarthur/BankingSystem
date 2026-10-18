package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class AccountService {
    AccountDAO accDAO = new AccountDAO();
    TransactionDAO transactionDAO = new TransactionDAO();

    public int createAccount(Account account, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            boolean cpfExists = accDAO.findAccountByCpf(session, account.getCpf());
            if (cpfExists) {
                return -1; // CPF ALREADY REGISTERED.
            }
            Transaction transaction = session.beginTransaction();
            try {
                int accountNumber = accDAO.persistAccount(session, account);
                int password_success = accDAO.persistPassword(session, accountNumber, password);
                if (password_success != 1) {
                    transaction.rollback();
                    return -2; // ACCOUNT_PASSWORD NOT INSERTED WITH NEW VALUE.
                }
                transaction.commit();
                return accountNumber;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }

    public Account accountLogin(int accountNumber, String tryPassword) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Account account = accDAO.findAccount(session, accountNumber);
            if (account == null) {
                return null;
            }
            String hashedPassword = accDAO.getPasswordHash(session, account.getAccountNumber());
            if (BCrypt.checkpw(tryPassword, hashedPassword)) {
                account.setLogged(true);
                account.setLoginTime(System.currentTimeMillis());
                return account;
            }
            return null;
        }
    }

    public int deposit(Account account, BigDecimal amount) {
        if (!account.isLogged()) {
            return -1; // ACCOUNT NOT LOGGED IN
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Account managedAccount = accDAO.findAccount(session, account.getAccountNumber());
                if (managedAccount == null) {
                    transaction.rollback();
                    return -2; // ACCOUNT NOT FOUND ON THE DATABASE
                }
                managedAccount.setBalance(managedAccount.getBalance().add(amount));
                BankTransaction BankTransaction = new BankTransaction("deposit", null,
                        managedAccount, amount, true);
                int transactionId = transactionDAO.persistTransaction(session, BankTransaction);
                if (transactionId == 0) {
                    transaction.rollback();
                    return -3; // FAILED TO PERSIST TRANSACTION
                }

                transaction.commit();
                return transactionId;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }

        }
    }

    public int withdraw(Account account, BigDecimal amount) {
        if (!account.isLogged()) {
            return -1; // ACCOUNT NOT LOGGED IN
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Account managedAccount = accDAO.findAccount(session, account.getAccountNumber());
                if (managedAccount == null) {
                    transaction.rollback();
                    return -2; // ACCOUNT NOT FOUND ON THE DATABASE
                }
                if (managedAccount.getBalance().compareTo(amount) < 0) {
                    transaction.rollback();
                    return -3; // NOT ENOUGH BALANCE.
                }
                managedAccount.setBalance(managedAccount.getBalance().subtract(amount));
                BankTransaction BankTransaction = new BankTransaction("withdraw", managedAccount,
                                null, amount, true);
                int transactionId = transactionDAO.persistTransaction(session, BankTransaction);
                if (transactionId == 0) {
                    transaction.rollback();
                    return -4; // ERROR PERSISTING THE TRANSACTION;
                }
                transaction.commit();
                return transactionId;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }

    public BankTransaction getTransaction(Account account, int transactionId) {
        if (!account.isLogged()) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return transactionDAO.findTransaction(session, account.getAccountNumber(), transactionId);
        }
    }

    public List<BankTransaction> getAccountTransactions(Account account) {
        if (!account.isLogged()) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return transactionDAO.findAccountTransactions(session, account.getAccountNumber());
        }
    }

    public BigDecimal getBalance(Account account) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Account managedAccount = accDAO.findAccount(session, account.getAccountNumber());
            if (managedAccount == null) {
                return null;
            }
            return managedAccount.getBalance();
        }
    }

    public int transfer(Account account, int recipientAccount, BigDecimal amount) {
        if (!account.isLogged()) {
            return -1; // ACCOUNT NOT LOGGED IN
        }
        if (account.getAccountNumber() == recipientAccount) {
            return -5; // TRANSFER TO SAME ACCOUNT
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Account managedSender = accDAO.findAccount(session, account.getAccountNumber());
                if (managedSender == null) {
                    transaction.rollback();
                    return -2; // SENDER ACCOUNT NOT FOUND ON THE DATABASE
                }
                if (managedSender.getBalance().compareTo(amount) < 0) {
                    transaction.rollback();
                    return -3; // NOT ENOUGH BALANCE
                }
                managedSender.setBalance(managedSender.getBalance().subtract(amount));
                int successReceiver = transactionDAO.updateRecipient(session, recipientAccount, amount);
                if (successReceiver != 1) {
                    transaction.rollback();
                    return -4; // RECIPIENT ACCOUNT NOT FOUND ON THE DATABASE
                }
                Account receiver = session.getReference(Account.class, recipientAccount);
                BankTransaction newTransaction = new BankTransaction("transfer", managedSender, receiver,
                        amount, true);
                int transactionId = transactionDAO.persistTransaction(session, newTransaction);
                if (transactionId == 0) {
                    transaction.rollback();
                    return 0; // FAILED TO PERSIST TRANSACTION
                }
                transaction.commit();
                return transactionId;
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw exception;
            }
        }
    }

    public List<BankTransaction> searchTransactionsByDate(Account account, LocalDate date) {
        if (!account.isLogged()) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return transactionDAO.findAccountTransactionsByDate(session, account.getAccountNumber(), date);
        }
    }

    public List<BankTransaction> depositList(Account account) {
        if (!account.isLogged()) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return transactionDAO.depositTransactions(session, account.getAccountNumber());
        }
    }

    public List<BankTransaction> withdrawList(Account account) {
        if (!account.isLogged()) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return transactionDAO.withdrawTransactions(session, account.getAccountNumber());
        }
    }
}
