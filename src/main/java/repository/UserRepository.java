package main.java.repository;
import main.java.model.User;

public interface UserRepository {
    void save(User user) ;
    User findByEmail(String email) ;
}
