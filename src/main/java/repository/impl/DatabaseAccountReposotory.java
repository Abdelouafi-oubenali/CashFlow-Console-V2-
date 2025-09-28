package main.java.repository.impl;

import main.java.enums.AccountType;
import main.java.model.Account;
import main.java.model.Client;
import main.java.model.User;
import main.java.repository.AccountRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Scanner ;
import main.java.service.SessionService;
import main.java.enums.Currency ;
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

    public void listAccountsByClient(String clientId) {
        String sql = "SELECT a.id AS account_id, a.type, a.balance, a.currency, a.created_by, a.created_at, " +
                "c.firstname, c.lastname, c.email, c.cin, c.phone, c.address " +
                "FROM account a " +
                "JOIN clients c ON a.client_id = c.id " +
                "WHERE c.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(clientId));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String accountId = rs.getObject("account_id", UUID.class).toString();
                    String type = rs.getString("type");
                    BigDecimal balance = rs.getBigDecimal("balance");
                    String currency = rs.getString("currency");
                    String createdBy = rs.getObject("created_by", UUID.class).toString();
                    LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                    String firstname = rs.getString("firstname");
                    String lastname = rs.getString("lastname");
                    String email = rs.getString("email");
                    String cin = rs.getString("cin");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");

                    System.out.println("Client: " + firstname + " " + lastname + " | Email: " + email +
                            " | CIN: " + cin + " | Phone: " + phone + " | Address: " + address);
                    System.out.println("  Account ID: " + accountId +
                            ", Type: " + type +
                            ", Balance: " + balance + " " + currency +
                            ", Created By: " + createdBy +
                            ", Created At: " + createdAt);
                    System.out.println("------------------------------------------------------------");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Account getAccountById(String account_id) {
        String sql = "SELECT * FROM account WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, UUID.fromString(account_id));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                            AccountType.valueOf(rs.getString("type")),
                            rs.getBigDecimal("balance")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void updateBalanceAccount(String account_id , BigDecimal balance)
    {
        String sql = "UPDATE account SET balance = ? WHERE id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setBigDecimal(1,balance);
            stmt.setObject(2, UUID.fromString(account_id));
            stmt.executeUpdate();
        }catch (SQLException e){
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
