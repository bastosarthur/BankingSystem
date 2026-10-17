package org.example;

import org.hibernate.Session;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

public class TransactionDAO {

    public int persistTransaction(Session session, bankTransaction bankTransaction) {
        session.persist(bankTransaction);
        return bankTransaction.getId();
    }

    public bankTransaction findTransaction(Session session, int transactionId) {
        return session.find(bankTransaction.class, transactionId);
    }

    public List<bankTransaction> findAccountTransactions(Session session, int accountNumber) {
        return session.createQuery("""
                        FROM bankTransaction
                        WHERE sender.accountNumber = :accountNumber
                        OR recipient.accountNumber = :accountNumber
                        ORDER BY transactionDate DESC
                        """, bankTransaction.class)
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

    public List<bankTransaction> findAccountTransactionsByDate(Session session, int accountNumber, LocalDate date) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return session.createQuery("""
                        FROM bankTransaction
                        WHERE
                        (
                            sender.accountNumber = :accountNumber
                            OR recipient.accountNumber = :accountNumber
                        )
                        AND transactionDate >= :start
                        AND transactionDate < :end
                        ORDER BY transactionDate DESC
                        """, bankTransaction.class)
                        .setParameter("accountNumber", accountNumber)
                        .setParameter("start", start)
                        .setParameter("end", end)
                        .getResultList();
    }

    public List<bankTransaction> depositTransactions(Session session, int accountNumber) {
        return session.createQuery("""
                        FROM bankTransaction
                        WHERE type = 'deposit'
                        AND recipient.accountNumber = :account_number
                        ORDER BY transactionDate DESC
                        """, bankTransaction.class)
                .setParameter("account_number", accountNumber)
                .getResultList();
    }

    public List<bankTransaction> withdrawTransactions(Session session, int accountNumber) {
        return session.createQuery("""
                        FROM bankTransaction
                        WHERE type = 'withdraw'
                        AND sender.accountNumber = :account_number
                        ORDER BY transactionDate DESC
                        """, bankTransaction.class)
                .setParameter("account_number", accountNumber)
                .getResultList();
    }
}
