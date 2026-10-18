package org.example;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "transactions", schema = "banking")
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "type", nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "sender_account")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "recipient_account")
    private Account recipient;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "transaction_date", nullable = false, insertable = false, updatable = false)
    private LocalDateTime transactionDate;

    protected BankTransaction() {}

    public BankTransaction(String type, Account sender, Account recipient, BigDecimal amount, boolean status) {
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.status = status;
        this.transactionDate = LocalDateTime.now();
    }

    public BankTransaction(int id, String type, Account sender, Account recipient, BigDecimal amount, boolean status,
                           LocalDateTime transactionDate) {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.status = status;
        this.transactionDate = transactionDate;
    }

    @Override
        public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/uuuu HH:mm");

            return "\nTransaction ID " + id +
                    "\nDate/Time: " + transactionDate.format(formatter) +
                    "\nType: " + type.toUpperCase() +
                    "\nSender: " + (sender != null ? sender.getAccountNumber() : "N/A") +
                    "\nReceiver: " + (recipient != null ? recipient.getAccountNumber() : "N/A") +
                    "\nStatus: " + (status ? "CONFIRMED" : "CANCELED") +
                    "\nAmount: %.2f".formatted(amount) + "\n";
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Account getSender() {
        return sender;
    }

    public Account getRecipient() {
        return recipient;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean newStatus) {
        this.status = newStatus;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }


}
