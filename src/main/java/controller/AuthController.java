package main.java.controller;

import main.java.model.User;
import main.java.service.UserService;

public class AuthController {
    private UserService userService ;
    private User currentUser ;

    public AuthController (UserService userService)
    {
        this.userService = userService ;
    }

    public boolean register(User user , String email)
    {
        return userService.register(user , email ) ;
    }

    public boolean login(String email, String password) {
        User user = userService.login(email, password);
        if (user != null) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
