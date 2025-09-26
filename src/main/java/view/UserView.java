package main.java.view;

import main.java.controller.AccountController;
import main.java.controller.AuthController;
import main.java.enums.AccountType;
import main.java.enums.Role;
import main.java.model.Account;
import main.java.model.Client;
import main.java.model.User;
import java.math.BigDecimal;
import java.util.Scanner;

import main.java.service.SessionService;

public class UserView {
    private static String email_Login = null ;
    private AuthController userController;
    private AccountController accountController; // ajout
    private Scanner sc = new Scanner(System.in);

    public UserView(AuthController authController, AccountController accountController) {
        this.userController = authController;
        this.accountController = accountController;
    }

    public void showMenu() {
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

    public void showMenuAdmin() {
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
                case 3 -> System.out.println("Gestion des comptes...");
                case 4 -> System.out.println("Gestion des crédits...");
                case 5 -> System.out.println("Gestion des profils...");
                case 6 -> System.out.println("Traitement des demandes de crédits...");
                case 7 -> System.out.println("Validation des virements...");
                case 8 -> System.out.println("Supervision des transactions...");
                case 9 -> System.out.println("Gestion des frais et commissions...");
                case 10 -> System.out.println("Génération des rapports financiers...");
                case 0 -> System.out.println("Au revoir 👋");
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

    private void login() {
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
}
