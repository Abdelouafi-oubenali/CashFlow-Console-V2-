package main.java.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

import main.java.enums.CreditStatus;
import main.java.model.Credit;
import main.java.repository.CreditRepository;
import main.java.repository.impl.DatabaseCreditRepository;

public class CriditService {
    CreditRepository creditRepository ;
    Scanner sc = new Scanner(System.in) ;

    public CriditService(CreditRepository creditRepository)
    {
        this.creditRepository = creditRepository ;

    }

    public void crediteDommonde(BigDecimal montant, int dureeMois, BigDecimal taux, String interestType, BigDecimal revenuMensuel, String accountId)
    {
        creditRepository.saveCridit(montant , dureeMois , taux , interestType ,revenuMensuel , accountId) ;
    }

    public boolean creditCheck(BigDecimal montant, BigDecimal revenuMensuel, int mois) {
        BigDecimal quarantePourcent = new BigDecimal("0.40");
        BigDecimal numberCheck = revenuMensuel.multiply(quarantePourcent);
        BigDecimal moisBig = new BigDecimal(mois);

        BigDecimal mensualite = montant.divide(moisBig, 2, RoundingMode.HALF_UP);

        System.out.println("Montant demandé : " + montant +
                " | Revenu mensuel : " + revenuMensuel +
                " | 40% du revenu = " + numberCheck +
                " | Mensualité = " + mensualite);

        return mensualite.compareTo(numberCheck) <= 0;
    }
    public boolean checkCreditIfExist(UUID idAccount) throws SQLException {
        Optional<Credit> credit = creditRepository.getCreditById(idAccount);
        if (credit.isPresent()) {
            CreditStatus status = credit.get().getStatus();
            if (status == CreditStatus.LATE || status == CreditStatus.ACTIVE) {
                return false;
            } else {
                return true;
            }
        } else {
            System.out.println("No credit demande for this account.");
            return false;
        }
    }

    public Optional<Credit> listDemande() throws SQLException {
        creditRepository.getCreditDemande() ;
        System.out.println("entre Id de credit pour check : ");
        UUID id = UUID.fromString(sc.nextLine());
        return creditRepository.getCreditById(id);
    }

    public void accepterDemande(UUID id) throws SQLException {
        creditRepository.updateStatusCredit(id , "ACTIVE") ;
    }

    public void refusDomande(UUID id) throws SQLException {
        creditRepository.updateStatusCredit(id , "REFUSED") ;
    }



}
