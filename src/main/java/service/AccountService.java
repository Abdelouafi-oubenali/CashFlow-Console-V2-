package main.java.service;

import main.java.model.Account;
import main.java.model.Client;
import main.java.model.User;
import main.java.repository.AccountRepository;
import main.java.service.SessionService;
import main.java.enums.Role;

public class AccountService {
    private AccountRepository accountRepository;

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
}
