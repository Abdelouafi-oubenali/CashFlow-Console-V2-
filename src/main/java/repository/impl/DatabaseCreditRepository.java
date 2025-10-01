package main.java.repository.impl;

import main.java.repository.CreditRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseCreditRepository implements CreditRepository {
    private Connection connection;
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

}
