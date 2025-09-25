package main.java.service;

import main.java.model.User;
import main.java.enums.Role;
import java.util.HashMap;
import java.util.Map;

public class SessionService {

    private static Map<String, Role> sessions = new HashMap<>();

    public static void startSession(User user) {
        sessions.put(user.getEmail(), user.getRole());
        System.out.println(user.getRole());
    }

    public static void endSession(User user) {
        sessions.remove(user.getEmail());
    }

    public static boolean isSessionActive(User user) {
        return sessions.containsKey(user.getEmail());
    }

    public static Role getUserRole(User user) {
        return sessions.get(user.getEmail());
    }
}
