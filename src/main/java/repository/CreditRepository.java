package main.java.repository;

import main.java.model.Credit;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface CreditRepository {
    void saveCridit(BigDecimal montant, int dureeMois, BigDecimal taux, String interestType, BigDecimal revenuMensuel, String accountId);
    void getCreditDemande() ;
    Optional<Credit> getCreditById(UUID id) throws SQLException;
    void updateStatusCredit(UUID id , String acction) throws SQLException ;
}
