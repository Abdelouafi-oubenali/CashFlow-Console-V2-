package main.java.model;

import java.math.BigDecimal;

public class Bank {

    private BigDecimal capital ;
    private BigDecimal total_fees ;
    private BigDecimal total_gains ;
    public Bank(BigDecimal capital , BigDecimal total_fees , BigDecimal total_gains )
    {
          this.capital = capital ;
          this.total_fees = total_fees ;
          this.total_gains = total_gains ;
    }

    public BigDecimal getCapital() {
        return capital;
    }

    public BigDecimal getTotal_fees() {
        return total_fees;
    }

    public BigDecimal getTotal_gains() {
        return total_gains;
    }

}
