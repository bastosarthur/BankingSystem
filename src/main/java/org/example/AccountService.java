package org.example;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AccountService {
    AccountDAO accDAO = new AccountDAO();
    TransactionDAO transactionDAO = new TransactionDAO();

    public int createAccount(Account account, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int accountNumber = accDAO.persistAccount(session, account);
                int password_success = accDAO.persistPassword(session, account.getAccountNumber(), password);
                if (password_success != 1) {
                    transaction.rollback();
                    return 0;
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
            boolean success = accDAO.credentialsMatch(session, account.getAccountNumber(), tryPassword);
            if (success) {
                account.setLogged(true);
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
                bankTransaction bankTransaction = new bankTransaction("deposit", null,
                        managedAccount, amount, true);
                int transactionId = transactionDAO.persistTransaction(session, bankTransaction);
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
                bankTransaction bankTransaction = new bankTransaction("withdraw", managedAccount,
                                null, amount, true);
                int transactionId = transactionDAO.persistTransaction(session, bankTransaction);
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

    public bankTransaction getTransaction(int transactionId) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return transactionDAO.findTransaction(session, transactionId);
        }
    }

    public List<bankTransaction> getAccountTransactions(Account account) {
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Account managedSender = accDAO.findAccount(session, account.getAccountNumber());
                //Account managedReceiver = accDAO.findAccount(session, recipientAccount);
                if (managedSender == null) {
                    transaction.rollback();
                    return -2; // SENDER ACCOUNT NOT FOUND ON THE DATABASE
                }
                if (managedSender.getBalance().compareTo(amount) < 0) {
                    transaction.rollback();
                    return -3; // NOT ENOUGH BALANCE
                }
                /*if (managedReceiver == null) {
                    transaction.rollback();
                    return -4; // RECIPIENT ACCOUNT NOT FOUND ON THE DATABASE
                }*/
                managedSender.setBalance(managedSender.getBalance().subtract(amount));
                int successReceiver = transactionDAO.updateRecipient(session, recipientAccount, amount);
                if (successReceiver != 1) {
                    transaction.rollback();
                    return -4; // RECIPIENT ACCOUNT NOT FOUND ON THE DATABASE
                }
                //managedReceiver.setBalance(managedReceiver.getBalance().add(amount));
                Account receiver = session.getReference(Account.class, recipientAccount);
                bankTransaction newTransaction = new bankTransaction("transfer", managedSender, receiver,
                        amount, true);
                int transactionId = transactionDAO.persistTransaction(session, newTransaction);
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

    public List<bankTransaction> searchTransactionsByDate(Account account, LocalDate date) {
        if (!account.isLogged()) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return transactionDAO.findAccountTransactionsByDate(session, account.getAccountNumber(), date);
        }
    }

    public List<bankTransaction> depositList(Account account) {
        if (!account.isLogged()) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return transactionDAO.depositTransactions(session, account.getAccountNumber());
        }
    }

    public List<bankTransaction> withdrawList(Account account) {
        if (!account.isLogged()) {
            return null;
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return transactionDAO.withdrawTransactions(session, account.getAccountNumber());
        }
    }
}
