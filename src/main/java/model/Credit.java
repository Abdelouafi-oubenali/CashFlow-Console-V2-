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
    private int duree;
    private BigDecimal taux;
    private InterestType interestType;  // Enum: SIMPLE, COMPOUND
    private FeeRule feeRule;
    private BigDecimal revenu_mensuel ;
    private CreditStatus status;        // Enum: ACTIVE, LATE, CLOSED
    private Account account;
    private LocalDateTime createdAt;

    public Credit(UUID id, BigDecimal montant, int duree, BigDecimal taux, InterestType interestType, FeeRule feeRule, CreditStatus status, Account account, LocalDateTime createdAt , BigDecimal revenu_mensuel) {
        this.id = id;
        this.montant = montant;
        this.duree = duree;
        this.taux = taux;
        this.interestType = interestType;
        this.feeRule = feeRule;
        this.status = status;
        this.account = account;
        this.createdAt = createdAt;
        this.revenu_mensuel = revenu_mensuel ;
    }

    // --- Getters ---
    public UUID getId() {
        return id;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public int tDuree() {
        return duree;
    }

    public BigDecimal getTaux() {
        return taux;
    }

    public InterestType getInterestType() {
        return interestType;
    }

    public FeeRule getFeeRule() {
        return feeRule;
    }

    public CreditStatus getStatus() {
        return status;
    }

    public Account getAccount() {
        return account;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
}

    public BigDecimal getRevenu_mensuel() {
        return revenu_mensuel;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id=" + id +
                ", montant=" + montant +
                ", duree=" + duree +
                ", taux=" + taux +
                ", interestType=" + interestType +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }

}

