package main.com.example.myapp;

import main.java.controller.AccountController;
import main.java.controller.AuthController;
import main.java.repository.impl.DatabaseAccountReposotory;
import main.java.repository.impl.DatabaseUserRepository;
import main.java.service.AccountService;
import main.java.service.UserService;
import main.java.util.DatabaseConnection;
import main.java.view.UserView;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    private static UserView userViewInstance;

    public static UserView getUserViewInstance() {
        return userViewInstance;
    }

    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getInstance();

            DatabaseUserRepository userRepository = new DatabaseUserRepository(conn);
            DatabaseAccountReposotory accountRepository = new DatabaseAccountReposotory(conn);

            UserService userService = new UserService(userRepository);
            AccountService accountService = new AccountService(accountRepository);
            AuthController authController = new AuthController(userService);
            AccountController accountController = new AccountController(accountService);

            userViewInstance = new UserView(authController, accountController);
            userViewInstance.showMenu();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
