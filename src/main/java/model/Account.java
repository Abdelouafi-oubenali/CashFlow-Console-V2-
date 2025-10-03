package main.java.model;

import main.java.enums.AccountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Currency;

public class Account {
    private UUID id;
    private AccountType type;
    private BigDecimal balance;
    private Currency currency;
    private UUID client_id;
    private User createdBy;
    private LocalDateTime createdAt;

    public Account(AccountType accountType, BigDecimal balance) {
        this.type = accountType;
        this.balance = balance;
        this.currency = Currency.getInstance("EUR");
        this.createdAt = LocalDateTime.now();
    }
    public Account(UUID id ,AccountType accountType, BigDecimal balance) {
        this.id = id ;
        this.type = accountType;
        this.balance = balance;
        this.currency = Currency.getInstance("EUR");
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getClient_id() {
        return client_id;
    }

    public AccountType getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public UUID getClient() {
        return client_id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", type=" + type +
                ", balance=" + balance +
                ", currency=" + currency +
                ", client_id=" + client_id +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                '}';
    }
}
