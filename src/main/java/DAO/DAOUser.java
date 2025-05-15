package DAO;

import Classes.User;
import java.sql.*;

public class DAOUser {

    public boolean create(User user) {
        String query = "INSERT INTO user (id,username, password) VALUES (null,?, ?)";

        try (Connection connection = Connexion.seConnecter();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyUserCredentials(String username, String password) {
        String query = "SELECT * FROM user WHERE username=? AND password=?";

        try (Connection connection = Connexion.seConnecter();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true si un utilisateur est trouvé
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
