package main.java.view;

import main.java.controller.AuthController;
import main.java.enums.Role;
import main.java.model.User;

import java.util.Scanner;

public class UserView {
    private AuthController userController ;

    private Scanner sc = new Scanner(System.in) ;

    public UserView (AuthController authController)
    {
        this.userController = authController ;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n=== MENU AUTH ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Logout");
            System.out.println("4. Who am I?");
            System.out.println("0. Quit");
            System.out.print("Choix: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> register();
                case 2 -> login();
                case 3 -> logout();
                case 4 -> whoAmI();
                case 0 -> { return; }
                default -> System.out.println("Choix invalide !");
            }
        }
    }

    private void register() {
        System.out.print("Prénom: ");
        String firstname = sc.nextLine();
        System.out.print("Nom: ");
        String lastname = sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Mot de passe: ");
        String password = sc.nextLine();

        User user = new User(firstname, lastname, email, password, Role.TELLER);
        if (userController.register(user)) {
            System.out.println("Inscription réussie.");
        } else {
            System.out.println("Email déjà utilisé.");
        }
    }


    private void login() {
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Mot de passe: ");
        String password = sc.nextLine();

        if (userController.login(email, password)) {
            System.out.println("ienvenue " + userController.getCurrentUser().getFirstname());
        } else {
            System.out.println("Identifiants invalides.");
        }
    }

    private void logout() {
        userController.logout();
        System.out.println("Déconnexion réussie.");
    }

    private void whoAmI() {
        if (userController.getCurrentUser() != null) {
            System.out.println("Utilisateur connecté: " + userController.getCurrentUser());
        } else {
            System.out.println("Aucun utilisateur connecté.");
        }
    }
}
