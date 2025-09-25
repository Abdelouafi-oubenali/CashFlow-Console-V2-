package main.java.service;

import main.java.model.User;
import main.java.repository.UserRepository;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(User user) {
        if (userRepository.findByEmail(user.getEmail()) != null) {
            return false;
        } else {
            userRepository.save(user);
            return true;
        }
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
