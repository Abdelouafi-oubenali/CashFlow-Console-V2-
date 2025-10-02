package main.java.repository.impl;

import main.java.enums.CreditStatus;
import main.java.enums.InterestType;
import main.java.model.Credit;
import main.java.repository.CreditRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class DatabaseCreditRepository implements CreditRepository {
    private static Connection connection;
    public DatabaseCreditRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveCridit(BigDecimal montant, int dureeMois, BigDecimal taux, String interestType,
                           BigDecimal revenuMensuel, String accountId) {

        String sql = "INSERT INTO credit (montant, duree, taux, interest_type, status, account_id, revenu_mensuel) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, montant);
            stmt.setInt(2, dureeMois);
            stmt.setBigDecimal(3, taux);
            stmt.setString(4, interestType);
            stmt.setString(5, "PENDING");
            stmt.setObject(6, UUID.fromString(accountId));
            stmt.setBigDecimal(7, revenuMensuel);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Credit enregistré avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Optional<Credit> getCreditById(UUID id) throws SQLException {
        String sql = "SELECT * FROM credit WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Credit credit = new Credit(
                            (UUID) rs.getObject("id"),
                            rs.getBigDecimal("montant"),
                            rs.getInt("duree"),
                            rs.getBigDecimal("taux"),
                            InterestType.valueOf(rs.getString("interest_type")),
                            null,
                            CreditStatus.valueOf(rs.getString("status")),
                            null,
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getBigDecimal("revenu_mensuel")
                    );
                    return Optional.of(credit);
                }
            }
        }

        return Optional.empty();
    }

    public void getCreditDemande() {
        String sql = """
        SELECT 
            credit.id AS credit_id,
            credit.montant,
            credit.status,
            clients.firstname,
            clients.lastname,
            account.type AS account_type,
            account.balance
        FROM credit 
        JOIN account ON credit.account_id = account.id
        JOIN clients ON account.client_id = clients.id
        WHERE credit.status = 'PENDING'
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String id = rs.getString("credit_id");
                String client = rs.getString("firstname") + " " + rs.getString("lastname");
                double montant = rs.getDouble("montant");
                String status = rs.getString("status");
                String accountType = rs.getString("account_type");
                double balance = rs.getDouble("balance");

                System.out.println("Crédit ID: " + id +
                        " | Client: " + client +
                        " | Montant: " + montant +
                        " | Statut: " + status +
                        " | Compte: " + accountType +
                        " | Solde: " + balance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatusCredit(UUID id, String action) throws SQLException {
        String sql = "UPDATE credit SET status = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if ("ACTIVE".equalsIgnoreCase(action)) {
                stmt.setString(1, CreditStatus.ACTIVE.name());
            } else if ("REFUSED".equalsIgnoreCase(action)) {
                stmt.setString(1, CreditStatus.REFUSED.name());
            } else {
                System.out.println(" Action non reconnue : " + action);
                return;
            }

            stmt.setObject(2, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Statut du crédit mis à jour avec succès !");
            } else {
                System.out.println("Aucun crédit trouvé avec cet ID.");
            }
        }
    }



}
