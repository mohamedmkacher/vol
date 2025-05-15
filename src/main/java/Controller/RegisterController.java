package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import DAO.DAOUser;
import Classes.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Hyperlink loginLink;
    @FXML private Label errorLabel;

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation des champs
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Tous les champs sont obligatoires");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Les mots de passe ne correspondent pas");
            return;
        }

        try {
            // Cryptage du mot de passe
            String encryptedPassword = encryptPassword(password);

            // Création de l'utilisateur
            User newUser = new User(username, encryptedPassword);

            // Enregistrement dans la base de données
            DAOUser userDAO = new DAOUser();
            boolean success = userDAO.create(newUser);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Inscription réussie",
                        "Votre compte a été créé avec succès");
                // Redirection vers la page de connexion
                handleLoginLink();
            } else {
                errorLabel.setText("Erreur lors de l'inscription");
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur technique");
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLoginLink() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/gestionvoltunisair/login.fxml"));
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}