package Controller;

import Classes.Vol;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML private Label userNameLabel;
    @FXML private Button logoutButton;
    @FXML private Button dashboardButton;
    @FXML private Button flightsButton;
    @FXML private Button aircraftButton;
    @FXML private Button crewButton;

    @FXML private Label dateLabel;
    @FXML private Label flightCountLabel;
    @FXML private Label aircraftCountLabel;
    @FXML private Label crewCountLabel;
    @FXML private TableView<Vol> todayFlightsTable;
    @FXML private TableColumn<Vol, String> flightNumberColumn;
    @FXML private TableColumn<Vol, String> originColumn;
    @FXML private TableColumn<Vol, String> destinationColumn;
    @FXML private TableColumn<Vol, String> departureTimeColumn;
    @FXML private TableColumn<Vol, String> statusColumn;
    @FXML private TableColumn<Vol, String> statusColumn1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userNameLabel.setText("Mohamed");
        dateLabel.setText("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        initializeTableColumns();
        loadDashboardData();
        setActiveButton(dashboardButton);
    }

    private void initializeTableColumns() {
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
        originColumn.setCellValueFactory(new PropertyValueFactory<>("origin"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        statusColumn1.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadDashboardData() {
        flightCountLabel.setText("24");
        aircraftCountLabel.setText("18");
        crewCountLabel.setText("42");
    }

    private void navigateToView(String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) todayFlightsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDashboard() {
        navigateToView("/com/example/gestionvoltunisair/dashboard.fxml");
    }

    @FXML
    private void handleFlights() {
        navigateToView("/com/example/gestionvoltunisair/gestion-vols.fxml");
    }

    @FXML
    private void handleAircraft() {
        navigateToView("/com/example/gestionvoltunisair/gestion-avions.fxml");
    }

    @FXML
    private void handleCrew() {
        setActiveButton(crewButton);
    }



    @FXML
    private void handleLogout() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de déconnexion");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Êtes-vous sûr de vouloir vous déconnecter ?");

        if (confirmation.showAndWait().get() == ButtonType.OK) {
            navigateToView("/com/example/gestionvoltunisair/login.fxml");
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setActiveButton(Button activeButton) {
        dashboardButton.setStyle("-fx-background-color: transparent;");
        flightsButton.setStyle("-fx-background-color: transparent;");
        aircraftButton.setStyle("-fx-background-color: transparent;");
        crewButton.setStyle("-fx-background-color: transparent;");

        activeButton.setStyle("-fx-background-color: #e0e0e0;");
    }
}