package main.java.controller;

import main.java.model.Account;
import main.java.model.Client;
import main.java.model.User;
import main.java.service.AccountService;

public class AccountController {
    private AccountService accountService ;

    public AccountController (AccountService accountService)
    {
         this.accountService = accountService ;
    }

    public void createAccount(Client client , Account account , User user)
    {
         accountService.createAccount(client ,account , user) ;
    }


    public void UpdateAccount()
    {
        accountService.updateAccount() ;
    }
    

    public void saveupdateAccount(Client client, Account account, String clientId)
    {
        accountService.saveupdateAccount(client ,account , clientId) ;
    }

    public void get_list_clients()
    {
         accountService.get_list_clients() ;

    }
    public void deposit(String client_id)
    {
        accountService.depositToAccount(client_id) ;
    }
}
