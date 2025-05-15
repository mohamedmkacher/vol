package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
    private static String user = "root";
    private static String passWord = "";

    public static Connection seConnecter() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mariadb://localhost:3306/gestion_vol", user, passWord);
        } catch (ClassNotFoundException e) {
            System.out.println("Driver non trouvé: " + e.getMessage());
        } catch (SQLException ex) {
            System.out.println("Problème de connexion: " + ex.getMessage());
        }
        return null;
    }

    public void setUser(String user) {
        Connexion.user = user;
    }

    public void setPassWord(String passWord) {
        Connexion.passWord = passWord;
    }
}