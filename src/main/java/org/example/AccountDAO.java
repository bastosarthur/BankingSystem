package org.example;

import org.hibernate.Session;

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

    public String getPasswordHash(Session session, int accountNumber) {
        return session.createNativeQuery("""
                SELECT password
                FROM banking.account_password
                WHERE account = :account
                """, String.class)
                .setParameter("account", accountNumber)
                .getSingleResult();
    }

    public boolean findAccountByCpf(Session session, String cpf) {
        return session.createNativeQuery("""
                SELECT EXISTS
                (
                SELECT 1
                FROM banking.accounts
                WHERE cpf = :cpf
                )
                """, Boolean.class)
                .setParameter("cpf", cpf)
                .getSingleResult();
    }
}
