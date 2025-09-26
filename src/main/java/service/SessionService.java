package main.java.service;

import main.java.model.User;
import main.java.enums.Role;
import java.util.HashMap;
import java.util.Map;

public class SessionService {

    private static Map<String, User> sessions = new HashMap<>();

    public static void startSession(User user) {
        sessions.put(user.getEmail(), user);
        System.out.println(sessions.get(user.getEmail()));
    }

    public static void endSession(User user) {
        sessions.remove(user.getEmail());
    }

    public static boolean isSessionActive(User user) {
        return sessions.containsKey(user.getEmail());
    }

    public static Role getUserRole(String email) {
        User u = sessions.get(email);
        if (u != null) {
            return u.getRole();
        }
        return null;
    }

    public static String getUserName(String email) {
        User u = sessions.get(email);
        if (u != null) {
            return u.getFirstname();
        }
        return null;
    }
}
