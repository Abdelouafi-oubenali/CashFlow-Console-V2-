package main.java.service;

import main.java.enums.AccountType;
import main.java.model.Account;
import main.java.model.Client;
import main.java.model.User;
import main.java.repository.AccountRepository;
import main.java.service.SessionService;
import main.java.enums.Role;
import main.com.example.myapp.Main;

import java.math.BigDecimal;
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

    public Account checkAccountInfo(String accont_id) {
        Account account = accountRepository.getAccountById(accont_id);

        if (account != null) {
            return account ;
        } else {
            System.out.println("No account found for account ID: " + accont_id);
            return null ;
        }
    }

    public void depositToAccount(String client_id) {
        accountRepository.listAccountsByClient(client_id);

        System.out.println("Entrez l'ID du compte pour déposer : ");
        String accountId = sc.next();

        System.out.println("Entrez le montant à déposer : ");
        BigDecimal newBalance = sc.nextBigDecimal();

        Account account = checkAccountInfo(accountId);
        if (account == null) {
            System.out.println("Impossible de déposer : le compte n'existe pas.");
            return;
        }

        BigDecimal total = account.getBalance().add(newBalance);
        accountRepository.updateBalanceAccount(accountId, total);

        System.out.println("Dépôt effectué avec succès !");
        accountRepository.listAccountsByClient(client_id);
    }

    public void retirerToAccount(String client_id) {
        accountRepository.listAccountsByClient(client_id);

        System.out.println("Entrez l'ID du compte pour retirer : ");
        String accountId = sc.next();

        System.out.println("Entrez le montant à retirer : ");
        BigDecimal montantRetrait = sc.nextBigDecimal();

        Account account = checkAccountInfo(accountId);
        if (account == null || account.getBalance().compareTo(montantRetrait) < 0) {
            System.out.println("Impossible de retirer : le compte n'existe pas ou solde insuffisant.");
            return;
        }

        BigDecimal total = account.getBalance().subtract(montantRetrait);
        accountRepository.updateBalanceAccount(accountId, total);

        System.out.println("Retrait effectué avec succès !");
        accountRepository.listAccountsByClient(client_id);
    }


}
