package org.example;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts", schema = "banking")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountNumber;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "balance")
    private BigDecimal balance;

    protected Account() {}

    public Account(int accountNumber, String cpf, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.cpf = cpf;
        this.balance = balance;
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

}
