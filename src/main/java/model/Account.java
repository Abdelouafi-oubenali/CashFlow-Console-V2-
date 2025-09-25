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
    private Client client;
    private User createdBy;
    private LocalDateTime createdAt;
}
