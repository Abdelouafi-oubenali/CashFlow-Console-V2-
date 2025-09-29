package main.java.controller;

import java.math.BigDecimal;
import main.java.service.CriditService ;

public class CriditController {

    CriditService criditService  ;
    public  CriditController(CriditService criditService)
    {
        this.criditService = criditService ;
    }
    public void creditDomonde(BigDecimal montant, int dureeMois, BigDecimal taux, String interestType, BigDecimal revenuMensuel, String accountId)
    {
        criditService.crediteDommonde(montant, dureeMois,taux,interestType,revenuMensuel,accountId);
    }

}
