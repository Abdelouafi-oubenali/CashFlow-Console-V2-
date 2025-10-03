package main.java.service;

import main.java.enums.AccountType;
import main.java.model.Account;
import main.java.model.Bank;
import main.java.model.Client;
import main.java.model.User;
import main.java.repository.AccountRepository;
import main.java.service.SessionService;
import main.java.enums.Role;
import main.com.example.myapp.Main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner ;
import java.util.UUID;

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

    public void bankService(BigDecimal montant, Integer action) {
        // Récupérer la banque
        Bank bank = accountRepository.getInfoBank("550e8400-e29b-41d4-a716-446655440000");
        if (bank == null) {
            System.out.println("Banque non trouvée !");
            return;
        }
        BigDecimal totalCapital;
        if (action > 0) { // dépôt
            totalCapital = bank.getCapital().add(montant);
        } else { // retrait
            totalCapital = bank.getCapital().subtract(montant);
        }

        Bank updatedBank = new Bank(totalCapital, bank.getTotal_fees(), bank.getTotal_gains());
        accountRepository.updateBankBalance(updatedBank, "550e8400-e29b-41d4-a716-446655440000");

        System.out.println("Capital mis à jour : " + totalCapital);
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
        bankService(newBalance , 1) ; // 1 == deposer
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
        bankService(montantRetrait , 0) ; // 0 == retrait

        BigDecimal total = account.getBalance().subtract(montantRetrait);
        accountRepository.updateBalanceAccount(accountId, total);

        System.out.println("Retrait effectué avec succès !");
        accountRepository.listAccountsByClient(client_id);

    }



    public void transactionInterne(String envoyeur) {
        try {
            accountRepository.listAccountsByClient(envoyeur);
            System.out.println("Sélectionnez l'ID du compte envoyeur : ");
            String accountEnvoyeur = sc.next();

            get_list_clients();
            System.out.println("Entrez l'ID du client destinataire : ");
            String idClientDestinataire = sc.next();

            accountRepository.listAccountsByClient(idClientDestinataire);
            System.out.println("Sélectionnez l'ID du compte destinataire : ");
            String accountDestinataire = sc.next();

            System.out.println("Entrez le montant à transférer : ");
            BigDecimal montantTransfert = sc.nextBigDecimal();

            Account accountEnvoiyer = checkAccountInfo(accountEnvoyeur);
            Account accountTransfert = checkAccountInfo(accountDestinataire);
            if (accountEnvoiyer.getBalance().compareTo(montantTransfert) < 0) {
                System.out.println("Solde insuffisant pour effectuer le transfert.");
                return;
            }
            BigDecimal nouveauSoldeEnvoyeur = accountEnvoiyer.getBalance().subtract(montantTransfert);
            BigDecimal nouveauSoldeDestinataire = accountTransfert.getBalance().add(montantTransfert);

            accountRepository.updateBalanceAccount(accountEnvoyeur, nouveauSoldeEnvoyeur);
            accountRepository.updateBalanceAccount(accountDestinataire, nouveauSoldeDestinataire);

            System.out.println(" Transaction effectuée avec succès !");
            System.out.println("Nouveau solde de l'envoyeur : " + nouveauSoldeEnvoyeur);
            System.out.println("Nouveau solde du destinataire : " + nouveauSoldeDestinataire);

        } catch (Exception e) {
            System.out.println("Erreur lors de la transaction : " + e.getMessage());
        }
    }

    public void transactionExterne(String envoyeur) {
        accountRepository.listAccountsByClient(envoyeur);
        System.out.println("Sélectionnez l'ID du compte envoyeur : ");
        String accountEnvoyeur = sc.next();

        System.out.println("Entre id de client extern : ") ;
        String accountextenr  = sc.next();
        System.out.println("Entrez le montant à transférer : ");
        BigDecimal montantTransfert = sc.nextBigDecimal();

        Account compteEnvoyeur = checkAccountInfo(accountEnvoyeur);

        BigDecimal frais = montantTransfert.multiply(BigDecimal.valueOf(0.02));
        BigDecimal totalDebit = montantTransfert.add(frais);

        if (compteEnvoyeur.getBalance().compareTo(totalDebit) < 0) {
            System.out.println("Solde insuffisant pour effectuer le transfert.");
        }

        BigDecimal nouveauSoldeEnvoyeur = compteEnvoyeur.getBalance().subtract(totalDebit);
        accountRepository.updateBalanceAccount(accountEnvoyeur, nouveauSoldeEnvoyeur);

        System.out.println("Transaction externe envoyée avec succès.\n" +
                "Montant transféré : " + montantTransfert + "\n" +
                "Frais appliqués : " + frais + "\n" +
                "Nouveau solde envoyeur : " + nouveauSoldeEnvoyeur + "\n");
    }

    public void updateAcoountSaller(UUID idAccount , BigDecimal montone)
    {

    }



}
