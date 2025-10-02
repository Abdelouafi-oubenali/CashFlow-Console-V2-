package main.java.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import main.java.model.Credit;
import main.java.service.CriditService ;

public class CriditController {

    CriditService criditService;

    public CriditController(CriditService criditService) {
        this.criditService = criditService;
    }

    public void creditDomonde(BigDecimal montant, int dureeMois, BigDecimal taux, String interestType, BigDecimal revenuMensuel, String accountId) {
        criditService.crediteDommonde(montant, dureeMois, taux, interestType, revenuMensuel, accountId);
    }

    public Optional<Credit> listDemande() throws SQLException {
        return criditService.listDemande();
    }

    public boolean checkCridit(BigDecimal montonDommonde, BigDecimal revenuMensuel, UUID idAccount) throws SQLException {
        boolean chekBalonce = criditService.creditCheck(montonDommonde, revenuMensuel);
        boolean chekIfexict = criditService.checkCreditIfExist(idAccount);
        if (chekBalonce && chekIfexict) {
            return true;
        } else {
            return false;
        }
    }

    public void accepterDemande(UUID idDommonde) throws SQLException {
        criditService.accepterDemande(idDommonde);
    }

    public void refuserDemande(UUID idDomonde) throws SQLException {
        criditService.refusDomande(idDomonde);
    }
}
