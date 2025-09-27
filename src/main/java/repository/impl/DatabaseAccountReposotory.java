package main.java.repository.impl;

import main.java.model.Account;
import main.java.model.Client;
import main.java.model.User;
import main.java.repository.AccountRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.Scanner ;
import main.java.service.SessionService;
public class DatabaseAccountReposotory implements AccountRepository {
    private Connection connection;
    private Scanner sc = new Scanner(System.in);

    public DatabaseAccountReposotory(Connection connection) {
        this.connection = connection;
    }
    @Override
    public boolean checkAccount(Account account) {
        try {
            String sql = "SELECT 1 FROM account WHERE client_id = ? AND type = ?";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setObject(1, account.getClient());
            stmt.setString(2, account.getType().name());

            ResultSet rs = stmt.executeQuery();
            return !rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void save(Client client, Account account , String email) {
       User user =  SessionService.getUserAth(email);
        String clientSql = "INSERT INTO clients (firstname, lastname, cin, phone, email, address) " +
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        String accountSql = "INSERT INTO account (type, balance, currency, client_id, created_by) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);
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
            PreparedStatement accountStmt = connection.prepareStatement(accountSql);
            accountStmt.setString(1, account.getType().name());
            accountStmt.setBigDecimal(2, account.getBalance());
            accountStmt.setString(3, account.getCurrency().getCurrencyCode());
            accountStmt.setObject(4, clientId);
            accountStmt.setObject(5, user.getId());

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
    public void saveUpdate(Client client, Account account, String id) {

        String clientSql = "UPDATE clients SET firstname = ?, lastname = ?, cin = ?, phone = ?, email = ?, address = ? " +
                "WHERE id = ?";

        String accountSql = "UPDATE account SET type = ?, balance = ?, currency = ? " +
                "WHERE client_id = ?";

        try {
            connection.setAutoCommit(false);

            // Mise à jour du client
            PreparedStatement clientStmt = connection.prepareStatement(clientSql);
            clientStmt.setString(1, client.getFirstname());
            clientStmt.setString(2, client.getLastname());
            clientStmt.setString(3, client.getCin());
            clientStmt.setString(4, client.getPhone());
            clientStmt.setString(5, client.getEmail());
            clientStmt.setString(6, client.getAddress());
            clientStmt.setObject(7, UUID.fromString(id));

            int clientRows = clientStmt.executeUpdate();
            if (clientRows == 0) {
                throw new SQLException("Client not found for update.");
            }

            PreparedStatement accountStmt = connection.prepareStatement(accountSql);
            accountStmt.setString(1, account.getType().name());
            accountStmt.setBigDecimal(2, account.getBalance());
            accountStmt.setString(3, account.getCurrency().getCurrencyCode());
            accountStmt.setObject(4, UUID.fromString(id));

            int accountRows = accountStmt.executeUpdate();
            if (accountRows == 0) {
                throw new SQLException("Account not found for update.");
            }

            connection.commit();
            connection.setAutoCommit(true);
            System.out.println("Client et compte mis à jour avec succès.");

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void listClient() {
        String sql = "SELECT id, firstname, lastname, email FROM clients";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("id");
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                String email = rs.getString("email");

                System.out.println("ID: " + id + ", Name: " + firstname + " " + lastname + ", Email: " + email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



//    public void UpdateInfoToclient()
//    {
//        listClient();
//        System.out.println("entre id de client pour fare les modifacasion");
//        String id_client = sc.nextLine() ;
//        System.out.println("id = " + id_client ) ;
//
//
//    }
}
