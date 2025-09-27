package main.java.service;
import main.java.enums.Role;
import main.java.model.User;
import main.java.repository.UserRepository;
import main.java.service.SessionService ;


public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean register(User user , String email) {
        if (SessionService.getUserRole(email) != Role.ADMIN) {
            System.out.println("Accès refusé : seul l'admin peut créer des utilisateurs.");
            System.out.println("role de ce user : " + SessionService.getUserRole(email)) ;
            return false;
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            System.out.println("Utilisateur déjà existant avec cet email : " + user.getEmail());
            return false;
        }
        userRepository.save(user);
        System.out.println("Nouvel utilisateur créé avec succès : " + user.getFirstname());
        return true;
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("test done login " + user) ;
            SessionService.startSession(user);
            return user;
        }
        return null;
    }
}
