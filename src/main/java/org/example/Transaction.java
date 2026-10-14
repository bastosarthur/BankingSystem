package org.example;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "transactions", schema = "banking")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type", nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "sender", nullable = false)
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver", nullable = false)
    private Account receiver;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "status", nullable = false)
    private boolean status;

    protected Transaction() {}

    public Transaction(int id, String type, Account sender, Account receiver, BigDecimal amount, boolean status) {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.status = status;
    }

    @Override
        public String toString() {
            return "\nTransaction ID " + id +
                    "\nType: " + type +
                    "\nSender: " + sender.getAccountNumber() +
                    "\nReceiver: " + receiver.getAccountNumber() +
                    "\nStatus: " + (status ? "Confirmed" : "Canceled") +
                    "\nAmount: %.2f".formatted(amount) + "\n";
    }


}
