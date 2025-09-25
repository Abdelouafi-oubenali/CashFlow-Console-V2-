package main.java.model;

import main.java.enums.CreditStatus;
import main.java.enums.InterestType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Credit {
    private UUID id;
    private BigDecimal montant;
    private LocalDate duree;
    private BigDecimal taux;
    private InterestType interestType;  // Enum: SIMPLE, COMPOUND
    private FeeRule feeRule;
    private CreditStatus status;        // Enum: ACTIVE, LATE, CLOSED
    private Account account;
    private LocalDateTime createdAt;
}
