package main.com.example.myapp;

import main.java.controller.AuthController;
import main.java.repository.UserRepository;
import main.java.repository.impl.DatabaseUserRepository;
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
            UserView userView = new UserView(authController) ;

            userView.showMenu();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
