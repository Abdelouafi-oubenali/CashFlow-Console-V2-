package main.java.util;

import java.sql.Connection ;
import java.sql.DriverManager ;
import  java.sql.SQLException ;

public class DatabaseConnection {

    private static Connection connection ;
    private static final String URL = "jdbc:postgresql://localhost:5433/Bank-DB";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    private DatabaseConnection () {} ;

    public static Connection getInstance() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/Bank-DB", "postgres", "password");
                System.out.println(" Connexion à la base de données réussie !");
            } catch (ClassNotFoundException var1) {
                throw new SQLException("Driver JDBC PostgreSQL introuvable.", var1);
            }
        }
        return connection;

    }


}
