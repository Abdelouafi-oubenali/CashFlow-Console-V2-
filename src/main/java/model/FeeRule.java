package main.java.model;

import main.java.enums.FeeMode;
import main.java.enums.OperationType;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.Currency;

public class FeeRule {
    private UUID id;
    private OperationType operationType; // Enum: TRANSFER_EXTERNAL, WITHDRAW_FOREIGN_CURRENCY, DEPOSIT, WITHDRAW
    private FeeMode mode;                // Enum: FIX, PERCENT
    private BigDecimal value;
    private Currency currency;
    private boolean active;
}
