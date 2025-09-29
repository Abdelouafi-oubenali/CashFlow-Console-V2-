package main.java.repository;

import java.math.BigDecimal;

public interface CreditRepository {
    void saveCridit(BigDecimal montant, int dureeMois, BigDecimal taux, String interestType, BigDecimal revenuMensuel, String accountId);
}
