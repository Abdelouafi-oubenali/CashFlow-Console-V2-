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
//    this.currency = currency;
//    this.client_id = client;
//    this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now(); // date de création automatique
    }


    public UUID getId() {
        return id;
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

    public UUID getClient() {
        return client_id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
