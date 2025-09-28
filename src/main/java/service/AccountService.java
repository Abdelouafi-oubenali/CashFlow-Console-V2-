package main.java.service;

import main.java.model.Account;
import main.java.model.Client;
import main.java.model.User;
import main.java.repository.AccountRepository;
import main.java.service.SessionService;
import main.java.enums.Role;
import main.com.example.myapp.Main;

import java.util.ArrayList;
import java.util.Scanner ;

public class AccountService {
    private AccountRepository accountRepository;
    private Main main = new Main();
   Scanner sc = new Scanner(System.in) ;
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean createAccount(Client client, Account account, User user) {
        if (SessionService.getUserRole(user.getEmail()) != Role.ADMIN) {
            System.out.println("Accès refusé : seul l'admin peut créer des utilisateurs.");
            return false;
        }

        if (!accountRepository.checkAccount(account)) {
            System.out.println("Ce user existe déjà.");
            return false;
        } else {
            accountRepository.save(client, account, user.getEmail());
            return true;
        }
    }

    public void updateAccount() {

        System.out.println("================== list des clients ===================== ") ;
        accountRepository.listClient();
        System.out.println("select Id users pour les modifacasion :");
        String client_id = sc.nextLine();
        Main.getUserViewInstance().updateAccount(client_id) ;

    }
    public void saveupdateAccount(Client client, Account account, String clientId)
    {
        accountRepository.saveUpdate(client,account,clientId);
    }
    public void get_list_clients()
    {
          accountRepository.listClient();
    }
    public void depositToaccount(String client_id)
    {
        accountRepository.listAccountsByClient(client_id) ;
    }
}
