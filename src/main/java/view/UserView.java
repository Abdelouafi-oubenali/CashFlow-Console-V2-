package main.java.view;

import main.java.controller.AccountController;
import main.java.controller.CriditController;

import main.java.controller.AuthController;
import main.java.enums.AccountType;
import main.java.enums.Role;
import main.java.model.Account;
import main.java.model.Client;
import main.java.model.Credit;
import main.java.model.User;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;


import main.java.repository.AccountRepository;
import main.java.service.SessionService;
import main.java.service.TasksAsynchrone;

public class UserView {
    private static String email_Login = null ;
    private AuthController userController;
    private AccountController accountController; // ajout
    private Scanner sc = new Scanner(System.in);
    private  CriditController criditController ;

    public UserView(AuthController authController, AccountController accountController ,CriditController criditController) {
        this.userController = authController;
        this.accountController = accountController;
        this.criditController = criditController ;
    }

    public void showMenu() throws SQLException {
        while (true) {
            System.out.println("\n=== MENU AUTH ===");
            System.out.println("1. Se connecter");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> login();
                case 0 -> { return; }
                default -> System.out.println("Choix invalide !");
            }
        }
    }

    public void showMenuAdmin() throws SQLException {
        int choix;
        do {
            System.out.println("\n===== Bienvenue dans l’espace administrateur =====");
            System.out.println("1. Créer un utilisateur pour la gestion");
            System.out.println("2. Créer un compte client");
            System.out.println("3. Gestion complète des comptes");
            System.out.println("4. Gérer les crédits");
            System.out.println("5. Gestion des profils");
            System.out.println("6. Gérer les demandes de crédits");
            System.out.println("7. Valider les virements");
            System.out.println("8. Superviser les transactions");
            System.out.println("9. Gestion des frais et commissions");
            System.out.println("10. Générer et consulter les rapports financiers");
            System.out.println("0. Quitter");
            System.out.print("➡ Votre choix : ");

            choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1 -> register();
                case 2 -> createAccount();
                case 3 -> AccountMenu();
                case 4 -> checkOperationCredit();
                case 5 -> System.out.println("Gestion des profils...");
                case 6 -> System.out.println("Traitement des demandes de crédits...");
                case 7 -> System.out.println("Validation des virements...");
                case 8 -> System.out.println("Supervision des transactions...");
                case 9 -> System.out.println("Gestion des frais et commissions...");
                case 10 -> System.out.println("Génération des rapports financiers...");
                case 0 -> System.out.println("Au revoir ");
                default -> System.out.println("⚠ Option invalide, réessayez !");
            }

        } while (choix != 0);
    }

    private void register() {
        if (email_Login == null || SessionService.getUserRole(email_Login) != Role.ADMIN) {
            System.out.println("Désolé, vous n'avez pas la permission !");
            return;
        }

        System.out.print("Prénom : ");
        String firstname = sc.nextLine();
        System.out.print("Nom : ");
        String lastname = sc.nextLine();
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("Mot de passe : ");
        String password = sc.nextLine();

        System.out.println("Entrez le rôle de l'utilisateur :");
        System.out.println("ADMIN, MANAGER, SELLER, AUDITOR");
        String roleInput = sc.nextLine();

        Role role;
        try {
            role = Role.valueOf(roleInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Rôle invalide !");
            return;
        }

        User user = new User(firstname, lastname, email, password, role);
        if (userController.register(user , email_Login)) {
            System.out.println("Inscription réussie.");
        } else {
            System.out.println("Email déjà utilisé.");
        }
    }

    private void login() throws SQLException {
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("Mot de passe : ");
        String password = sc.nextLine();

        if (userController.login(email, password)) {
            System.out.println("Bienvenue " + userController.getCurrentUser().getFirstname() + " !");
            email_Login = email ;
            showMenuAdmin();
        } else {
            System.out.println("Identifiants invalides.");
        }
    }

    private void logout() {
        userController.logout();
        System.out.println("Déconnexion réussie.");
    }

    private User whoAmI() {
        User currentUser = userController.getCurrentUser();
        if (currentUser != null) {
            return currentUser;
        } else {
            System.out.println("Aucun utilisateur connecté.");
            return null;
        }
    }

    private void createAccount() {
        if (email_Login == null || SessionService.getUserRole(email_Login) != Role.ADMIN) {
            System.out.println("Désolé, vous n'avez pas la permission !");
            return;
        }
        System.out.println("====== Création d’un compte client ========");
        System.out.print("Prénom : ");
        String firstname = sc.nextLine();
        System.out.print("Nom : ");
        String lastname = sc.nextLine();
        System.out.print("CIN : ");
        String cin = sc.nextLine();
        System.out.print("Téléphone : ");
        String phone = sc.nextLine();
        System.out.print("Email : ");
        String email = sc.nextLine();
        System.out.print("Adresse : ");
        String address = sc.nextLine();
        System.out.print("Type de compte (ex: COURANT, EPARGNE) : ");
        String typeInput = sc.nextLine();
        System.out.print("Solde initial : ");
        BigDecimal balance = sc.nextBigDecimal();
        sc.nextLine();

        Client client = new Client(firstname, lastname, cin, phone, email, address);

        AccountType accountType;
        try {
            accountType = AccountType.valueOf(typeInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Type de compte invalide !");
            return;
        }

        Account account = new Account(accountType, balance);

        accountController.createAccount(client, account, userController.getCurrentUser());

        System.out.println("Compte créé avec succès !");
    }

    public void updateAccount(String client_id) {
        if (email_Login == null || SessionService.getUserRole(email_Login) != Role.ADMIN) {
            System.out.println("Désolé, vous n'avez pas la permission !");
            return;
        }

        System.out.println("====== Update d’un compte client ========");
        sc.nextLine();
        System.out.print("Prénom : ");
        String firstname = sc.nextLine();

        System.out.print("Nom : ");
        String lastname = sc.nextLine();

        System.out.print("CIN : ");
        String cin = sc.nextLine();

        System.out.print("Téléphone : ");
        String phone = sc.nextLine();

        System.out.print("Email : ");
        String email = sc.nextLine();

        System.out.print("Adresse : ");
        String address = sc.nextLine();

        System.out.print("Type de compte (ex: COURANT, EPARGNE) : ");
        String typeInput = sc.nextLine();

        System.out.print("Solde initial : ");
        String balanceInput = sc.nextLine();
        BigDecimal balance;
        try {
            balance = new BigDecimal(balanceInput);
        } catch (NumberFormatException e) {
            System.out.println("Montant invalide !");
            return;
        }

        Client client = new Client(firstname, lastname, cin, phone, email, address);

        AccountType accountType;
        try {
            accountType = AccountType.valueOf(typeInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Type de compte invalide !");
            return;
        }

        Account account = new Account(accountType, balance);

        accountController.saveupdateAccount(client, account, client_id);

        System.out.println("Compte mis à jour avec succès !");
    }

    public void AccountMenu() {
        if (email_Login == null || SessionService.getUserRole(email_Login) != Role.ADMIN) {
            System.out.println("Désolé, vous n'avez pas la permission !");
            return;
        }

        System.out.println("=========== Bienvenue dans la partie gestion des comptes ==============");
        System.out.println("1. Mettre à jour un compte");
        System.out.println("2. Faire un dépôt (DEPOSIT)");
        System.out.println("3. Faire un retrait (WITHDRAW)");
        System.out.println("4. Faire une transaction");
        System.out.println("5. Fermer un compte");


        int choix = sc.nextInt();

        switch (choix) {
            case 1:
                UpdateAccount();
                break;
            case 2:
                deposit();
                break;
            case 3:
                retirer();
                break;
            case 4:
                transaction();
            default:
                System.out.println("Choix invalide !");
        }
    }

    public void UpdateAccount()
    {
        accountController.UpdateAccount();
    }


    public String get_list_clients()
    {
        accountController.get_list_clients() ;
        sc.nextLine();
        System.out.println("entre id de client : ") ;
        String client_id = sc.nextLine();
        return  client_id ;
    }

    public void deposit ()
    {
        accountController.deposit(get_list_clients());

    }

    public void retirer()
    {
        accountController.retirer(get_list_clients());
    }

    public void transaction()
    {
        System.out.println("============= Transaction Management ===============");
        System.out.println("1. Transaction interne");
        System.out.println("2. Transaction externe");
        System.out.print("Entrez votre choix : ");
        int choix = sc.nextInt();

        if(choix == 1 )
        {
            transactionInterne() ;
        } else if (choix == 2) {
            transactionexterne();
        }
    }

    private void transactionInterne()
    {
            accountController.transactionInterne(get_list_clients()) ;
    }

    private void transactionexterne()
    {
       accountController.transactionExterne(get_list_clients()) ;
    }

    public void checkOperationCredit() throws SQLException {
        System.out.println("=========== Check opération de crédit ===========");
        System.out.println("1. Vérifier les demandes");
        System.out.println("2. Faire une demande");

        int choix = sc.nextInt();

        if (choix == 1) {
            verifierDemandeCredit();
        } else if (choix == 2) {
            creditDemande();
        } else {
            System.out.println("Ce choix n'existe pas !");
        }
    }


    public void creditDemande() {
        //Scanner sc = new Scanner(System.in);

        System.out.println("==================== Demande de Crédit ===========================");
        System.out.print("Entrez l'ID du compte lié : ");
        String accountId = sc.next();
        Account account = accountController.getAcccountByid(UUID.fromString(accountId));

        if (account == null) {
            System.out.println("Aucun compte trouvé avec cet ID.");
            return;
        }

        if (account.getType() != AccountType.CREDIT) {
            System.out.println("Le compte choisi n'est pas de type CRÉDIT !");
            return;
        }

        System.out.print("Entrez le montant du crédit : ");
        BigDecimal montant = sc.nextBigDecimal();

        System.out.print("Entrez la durée en mois : ");
        int dureeMois = sc.nextInt();

        System.out.print("Entrez le taux d'intérêt (%) : ");
        BigDecimal taux = sc.nextBigDecimal();

        System.out.print("Entrez le type d'intérêt (SIMPLE/COMPOUND) : ");
        String interestType = sc.next();

        System.out.print("Entrez votre revenu mensuel : ");
        BigDecimal revenuMensuel = sc.nextBigDecimal();


        criditController.creditDomonde(montant, dureeMois, taux, interestType, revenuMensuel, accountId);

        System.out.println("==================================================================");
        System.out.println("Demande de crédit enregistrée (simulation) !");
    }

    public Optional<Credit> listDemande() throws SQLException {
        System.out.println("============== list des dommonde ============ ");
       // System.out.println(criditController.listDemande()) ;
        return criditController.listDemande() ;
    }

    public void verifierDemandeCredit() throws SQLException {
         Optional<Credit> creditList = listDemande() ;

        if (creditList.isPresent()) {
            UUID id = creditList.get().getId();
            BigDecimal montoneDommonde = creditList.get().getMontant() ;
            BigDecimal revenuMensuel = creditList.get().getRevenu_mensuel();
            boolean check =  criditController.checkCridit(montoneDommonde , revenuMensuel , id);
            if(check)
            {
                sc.nextLine() ;
                System.out.println("ce account et les condision pour acccipt le dommonde est exicit ....  " );
                System.out.println("ese que ce dommonde confirm oui/non : ");
                String choix = sc.nextLine() ;
                if(choix.equals("oui"))
                {
                    accepterDemande(id);
                    System.out.println("La demande de crédit de l'utilisateur est acceptée ");

                } else if (choix.equals("non")) {
                    refuserDemande(id);
                    System.out.println("La demande de crédit est refusée. ") ;
                }
            }else{
                refuserDemande(id) ;
                System.out.println("Votre demande est invalide, les conditions ne sont pas remplies ou aucun crédit trouvé.");
            }
        } else {
            System.out.println("Pas de condision pour la dommonde de crédit trouvé !");
        }
    }

    public void accepterDemande(UUID idDommonde) throws SQLException {
        criditController.accepterDemande(idDommonde) ;
    }

    public void refuserDemande(UUID idDommonde) throws SQLException
    {
        criditController.refuserDemande(idDommonde) ;
    }

    public void Tasktest(AccountRepository accountRepository)
    {
        TasksAsynchrone tasksAsynchrone = new TasksAsynchrone() ;
        tasksAsynchrone.startTask(accountRepository);
    }
}
