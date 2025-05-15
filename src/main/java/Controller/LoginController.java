package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import DAO.DAOUser;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Hyperlink registration;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username et mot de passe requis");
            return;
        }

        try {
            String encryptedPassword = encryptPassword(password);
            boolean isValidUser = DAOUser.verifyUserCredentials(username, encryptedPassword);

            if (isValidUser) {
                redirectToDashboard();
            } else {
                errorLabel.setText("Identifiants incorrects");
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur de connexion");
            e.printStackTrace();
        }
    }

    private String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        StringBuilder hexString = new StringBuilder();

        for (byte b : digest) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    private void redirectToDashboard() {
        try {
            URL fxmlUrl = getClass().getResource("/com/example/gestionvoltunisair/gestion-avions.fxml");
            System.out.println("Loading dashboard from: " + fxmlUrl);

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            errorLabel.setText("Erreur de chargement de l'interface");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegister() {
        try {
            URL fxmlUrl = getClass().getResource("/com/example/gestionvoltunisair/register.fxml");
            System.out.println("Loading register from: " + fxmlUrl);

            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage) registration.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            errorLabel.setText("Erreur lors du chargement de register.fxml");
            e.printStackTrace();
        }
    }
}