package main.java.repository.impl;

import main.java.enums.Role;
import main.java.model.User;
import main.java.repository.UserRepository;

import java.sql.*;

public class DatabaseUserRepository implements UserRepository {
    private Connection connection ;

    public DatabaseUserRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO users (firstname, lastname, email, password, role, created_at) VALUES (?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, user.getFirstname()); // 1
            stmt.setString(2, user.getLastname());  // 2
            stmt.setString(3, user.getEmail());     // 3
            stmt.setString(4, user.getPassword());  // 4
            stmt.setString(5, user.getRole().name());// 5
            stmt.setTimestamp(6, Timestamp.valueOf(user.getCreatedAt())); // 6
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            PreparedStatement stmt = connection.prepareStatement(
                    "SELECT * FROM users WHERE email = ?"
            );
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("firstname"),
                        rs.getString("lastname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        Role.valueOf(rs.getString("role"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
