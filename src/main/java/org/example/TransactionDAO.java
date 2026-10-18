package org.example;

import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

public class TransactionDAO {

    public int persistTransaction(Session session, BankTransaction BankTransaction) {
        session.persist(BankTransaction);
        return BankTransaction.getId();
    }

    public BankTransaction findTransaction(Session session, int accountNumber, int transactionId) {
        return session.createQuery("""
                        FROM BankTransaction
                        WHERE
                        (
                        sender.accountNumber = :accountNumber
                        OR recipient.accountNumber = :accountNumber
                        )
                        AND id = :transactionId
                        """, BankTransaction.class)
                        .setParameter("accountNumber", accountNumber)
                        .setParameter("transactionId", transactionId)
                        .getSingleResultOrNull();
    }

    public List<BankTransaction> findAccountTransactions(Session session, int accountNumber) {
        return session.createQuery("""
                        FROM BankTransaction
                        WHERE sender.accountNumber = :accountNumber
                        OR recipient.accountNumber = :accountNumber
                        ORDER BY transactionDate DESC
                        """, BankTransaction.class)
                        .setParameter("accountNumber", accountNumber)
                        .getResultList();
    }

    public int updateRecipient(Session session, int recipientAccount, BigDecimal amount) {
        return session.createMutationQuery("""
                UPDATE Account
                SET balance = balance + :amount
                WHERE accountNumber = :account"""
                )
                .setParameter("amount", amount)
                .setParameter("account", recipientAccount)
                .executeUpdate();
    }

    public List<BankTransaction> findAccountTransactionsByDate(Session session, int accountNumber, LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return session.createQuery("""
                        FROM BankTransaction
                        WHERE
                        (
                            sender.accountNumber = :accountNumber
                            OR recipient.accountNumber = :accountNumber
                        )
                        AND transactionDate >= :start
                        AND transactionDate < :end
                        ORDER BY transactionDate DESC
                        """, BankTransaction.class)
                        .setParameter("accountNumber", accountNumber)
                        .setParameter("start", start)
                        .setParameter("end", end)
                        .getResultList();
    }

    public List<BankTransaction> depositTransactions(Session session, int accountNumber) {
        return session.createQuery("""
                        FROM BankTransaction
                        WHERE type = 'deposit'
                        AND recipient.accountNumber = :account_number
                        ORDER BY transactionDate DESC
                        """, BankTransaction.class)
                .setParameter("account_number", accountNumber)
                .getResultList();
    }

    public List<BankTransaction> withdrawTransactions(Session session, int accountNumber) {
        return session.createQuery("""
                        FROM BankTransaction
                        WHERE type = 'withdraw'
                        AND sender.accountNumber = :account_number
                        ORDER BY transactionDate DESC
                        """, BankTransaction.class)
                .setParameter("account_number", accountNumber)
                .getResultList();
    }
}
