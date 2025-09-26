package main.com.example.myapp;

import main.java.controller.AccountController;
import main.java.controller.AuthController;
import main.java.repository.AccountRepository;
import main.java.repository.UserRepository;
import main.java.repository.impl.DatabaseAccountReposotory;
import main.java.repository.impl.DatabaseUserRepository;
import main.java.service.AccountService;
import main.java.service.UserService;
import main.java.util.DatabaseConnection;
import main.java.view.UserView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        try {
            Connection conn = DatabaseConnection.getInstance();
            DatabaseUserRepository userRepository = new DatabaseUserRepository(conn) ;
            UserService userService = new UserService(userRepository) ;
            AuthController authController = new AuthController(userService) ;

            //Account
            DatabaseAccountReposotory databaseAccountReposotory = new DatabaseAccountReposotory(conn) ;
            AccountService accountService = new AccountService(databaseAccountReposotory) ;
            AccountController accountController = new AccountController(accountService);
            UserView userView = new UserView(authController ,accountController) ;

            userView.showMenu();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
