package main.java.service;

import java.math.BigDecimal;
import main.java.repository.CreditRepository;

public class CriditService {
    CreditRepository creditRepository ;

    public CriditService(CreditRepository creditRepository)
    {
        this.creditRepository = creditRepository ;

    }
    public void crediteDommonde(BigDecimal montant, int dureeMois, BigDecimal taux, String interestType, BigDecimal revenuMensuel, String accountId)
    {
        creditRepository.saveCridit(montant , dureeMois , taux , interestType ,revenuMensuel , accountId) ;
    }
    public boolean creditCheck(BigDecimal montant, BigDecimal revenuMensuel) {
        BigDecimal quarantePourcent = new BigDecimal("0.40");
        BigDecimal calcul = revenuMensuel.multiply(quarantePourcent);
        if (calcul.compareTo(montant) >= 0) {
            return false;
        } else {
            return true;
        }
    }

}
