package org.example;

import org.hibernate.Session;
import java.util.List;
import java.math.BigDecimal;

public class AccountDAO {

    public int persistAccount(Session session, Account account) {
        session.persist(account);
        return account.getAccountNumber();
    }

    public int persistPassword(Session session, int accountNumber, String password) {
        return session.createNativeMutationQuery(
                "INSERT INTO banking.account_password (account, password) VALUES (:account, :password)")
                .setParameter("account", accountNumber)
                .setParameter("password", password)
                .executeUpdate();

    }

    public Account findAccount(Session session, int accountNumber) {
        return session.find(Account.class, accountNumber);
    }

    public boolean credentialsMatch(Session session, int accountNumber, String tryPassword) {
        return session.createNativeQuery("""
                SELECT EXISTS (
                SELECT 1                                    
                FROM banking.account_password
                WHERE account = :account
                AND password = :password
                )
                """, Boolean.class)
                .setParameter("account", accountNumber)
                .setParameter("password", tryPassword)
                .getSingleResult();
    }
}
