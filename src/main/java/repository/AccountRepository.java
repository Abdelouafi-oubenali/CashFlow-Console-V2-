package main.java.repository;

import main.java.model.Account;
import main.java.model.Client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

public interface AccountRepository {
    boolean checkAccount (Account account) ;
    void save(Client client , Account account , String email) ;
    void listClient() ;
    void saveUpdate(Client client , Account account , String email);
    void listAccountsByClient(String client_id) ;
    void updateBalanceAccount(String client_id , BigDecimal balance) ;
    Account getAccountById(String client_id);

}
