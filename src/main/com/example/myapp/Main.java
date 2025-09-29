package main.com.example.myapp;

import main.java.controller.AccountController;
import main.java.controller.AuthController;
import main.java.repository.impl.DatabaseAccountReposotory;
import main.java.repository.impl.DatabaseCreditRepository;
import main.java.repository.impl.DatabaseUserRepository;
import main.java.service.AccountService;
import main.java.service.CriditService;
import main.java.service.UserService;
import main.java.util.DatabaseConnection;
import main.java.view.UserView;
import  main.java.controller.CriditController ;
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
            DatabaseCreditRepository databaseCreditRepository = new DatabaseCreditRepository(conn);

            UserService userService = new UserService(userRepository);
            AccountService accountService = new AccountService(accountRepository);
            CriditService criditService = new CriditService(databaseCreditRepository);

            AuthController authController = new AuthController(userService);
            AccountController accountController = new AccountController(accountService);
            CriditController criditController = new CriditController(criditService) ;

            userViewInstance = new UserView(authController, accountController, criditController);
            userViewInstance.showMenu();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
