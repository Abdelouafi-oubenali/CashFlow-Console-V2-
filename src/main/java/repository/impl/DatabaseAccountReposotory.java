package main.java.repository.impl;

import main.java.enums.AccountType;
import main.java.model.Account;
import main.java.model.Client;
import main.java.model.User;
import main.java.model.Bank;
import main.java.repository.AccountRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

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
                "VALUES (?, ?, ?, ?, ?, ?)";
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

            try (ResultSet  rs = stmt.executeQuery()) {
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

        public List<Account> getAccountsTypeCredit() {
        String sql = "SELECT * FROM account WHERE type = 'CREDIT'";
        List<Account> accounts = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                UUID id = (UUID) rs.getObject("id");
                BigDecimal balance = rs.getBigDecimal("balance");

                String type = rs.getString("type");
                AccountType accountType = AccountType.valueOf(type.toUpperCase());

                Account account = new Account(id ,accountType , balance );
                accounts.add(account);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }
    public BigDecimal sallerAccount(UUID id)
    {
        String sql = "SELECT revenu_mensuel FROM credit WHERE account_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("revenu_mensuel");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal montoneCredit(UUID id)
    {
        String sql = "SELECT montant FROM credit WHERE account_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("montant");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }


    public Bank getInfoBank(String bankId) {
        String sql = "SELECT * FROM bank WHERE id = ?";

        try (PreparedStatement prmt = connection.prepareStatement(sql)) {
            prmt.setString(1, bankId);

            ResultSet rs = prmt.executeQuery();
            if (rs.next()) {
                BigDecimal capital = rs.getBigDecimal("capital");
                BigDecimal totalFees = rs.getBigDecimal("total_fees");
                BigDecimal totalGains = rs.getBigDecimal("total_gains");

                Bank bank = new Bank(capital, totalFees, totalGains);

                rs.close();
                return bank;
            } else {
                rs.close();
                System.out.println("Aucune banque trouvée avec cet ID !");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void updateBankBalanceJust(BigDecimal capital, String bankId) {
        String sql = "UPDATE bank SET capital = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, capital);
            stmt.setString(2, bankId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Capital de la banque mis à jour avec succès !");
            } else {
                System.out.println("Aucune banque trouvée avec cet ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateBankBalance(Bank bank, String bankId) {
        String sql = "UPDATE bank SET capital = ?, total_fees = ?, total_gains = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, bank.getCapital());
            stmt.setBigDecimal(2, bank.getTotal_fees());
            stmt.setBigDecimal(3, bank.getTotal_gains());
            stmt.setString(4, bankId);

            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Lignes mises à jour : " + rowsUpdated);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void updateBaloneAccount(BigDecimal montant, UUID compteId)
//    {
//        String sql = "UPDATE account SET balance = balance + ? WHERE id = ?";
//
//        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
//            stmt.setBigDecimal(1, montant);
//            stmt.setObject(2, compteId);
//            int lignesModifiees = stmt.executeUpdate();
//
//            if (lignesModifiees > 0) {
//                System.out.println("Solde mis à jour avec succès pour le compte : " + compteId);
//            } else {
//                System.out.println("Aucun compte trouvé avec cet ID.");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

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
