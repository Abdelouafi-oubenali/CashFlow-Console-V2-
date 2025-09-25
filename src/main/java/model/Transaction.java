package main.java.model;

import main.java.enums.TransactionStatus;
import main.java.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private BigDecimal amount;
    private Account accountFrom;
    private Account accountTo;
    private FeeRule feeRule;
    private TransactionStatus status;   // Enum: PENDING, SETTLED, FAILED
    private TransactionType type;       // Enum: TRANSFER_EXTERNAL, WITHDRAW_FOREIGN_CURRENCY, DEPOSIT, WITHDRAW
    private LocalDateTime createdAt;
}
