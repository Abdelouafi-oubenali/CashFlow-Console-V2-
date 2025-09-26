package main.java.repository.impl;

import main.java.model.Account;
import main.java.model.Client;
import main.java.repository.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseAccountReposotory implements AccountRepository {
    private Connection connection;

    public DatabaseAccountReposotory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean checkAccount(Account account) {
        try {
            String sql = "SELECT 1 FROM account WHERE client_id = ? AND type = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, account.getClient());              // UUID au lieu de String
            stmt.setString(2, account.getType().name());         // Enum -> String

            ResultSet rs = stmt.executeQuery();
            return !rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save(Client client, Account account) {
        String clientSql = "INSERT INTO clients (firstname, lastname, cin, phone, email, address) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        String accountSql = "INSERT INTO account (type, balance, currency, client_id, created_by) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // Sauvegarde du client
            PreparedStatement clientStmt = connection.prepareStatement(clientSql);
            clientStmt.setString(1, client.getFirstname());
            clientStmt.setString(2, client.getLastname());
            clientStmt.setString(3, client.getCin());
            clientStmt.setString(4, client.getPhone());
            clientStmt.setString(5, client.getEmail());
            clientStmt.setString(6, client.getAddress());

            ResultSet rs = clientStmt.executeQuery();
            UUID clientId = null;
            if (rs.next()) {
                clientId = (UUID) rs.getObject("id");
            } else {
                throw new SQLException("Failed to insert client.");
            }

            // Sauvegarde du compte
            PreparedStatement accountStmt = connection.prepareStatement(accountSql);
            accountStmt.setString(1, account.getType().name());
            accountStmt.setBigDecimal(2, account.getBalance());
            accountStmt.setString(3, account.getCurrency().getCurrencyCode());
            accountStmt.setObject(4, clientId);
            accountStmt.setObject(5, "testSesion");

            accountStmt.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("Client and Account saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
