package org.example;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts", schema = "banking")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_number")
    private int accountNumber;

    @Column(name = "cpf", nullable = false, length = 11)
    private String cpf;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Transient
    private boolean isLogged = false;

    @Transient
    private long loginTime;

    protected Account() {}

    public Account(int accountNumber, String cpf, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.cpf = cpf;
        this.balance = balance;
    }

    public Account(String cpf) {
        this.cpf = cpf;
    }

    @Override
        public String toString() {
            return "\nAccount Number: " + accountNumber +
                    "\nCpf: " + cpf +
                    "\nBalance: %.2f".formatted(balance) + "\n";
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getCpf() {
        return cpf;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean newIsLogged) {
        this.isLogged = newIsLogged;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long newLoginTime) {
        this.loginTime = newLoginTime;
    }

}
