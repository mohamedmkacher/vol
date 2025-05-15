package Controller;

import Classes.Avion;
import DAO.Connexion;
import DAO.DAOAvion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class AvionController implements Initializable {

    // TableView et colonnes
    @FXML private Label userNameLabel;
    @FXML private TableView<Avion> aircraftTable;
    @FXML private TableColumn<Avion, String> registrationColumn;
    @FXML private TableColumn<Avion, String> marqueColumn;
    @FXML private TableColumn<Avion, String> modelColumn;
    @FXML private TableColumn<Avion, Integer> capacityColumn;
    // Champs de formulaire
    @FXML private TextField registrationField;
    @FXML
    private ComboBox<String> marqueComboBox;

    @FXML
    private ComboBox<String> modeleComboBox;

    @FXML
    private ComboBox<String> capacityComboBox;

    @FXML private CheckBox disponibleCheckBox;

    // Avion sélectionné
    private Avion avionSelectionne = null;


    // Filtrage et recherche
    @FXML private ComboBox<String> filterComboBox;
    @FXML private TextField searchField;
    private Map<String, List<String>> marqueToModeles = new HashMap<>();
    private Map<String, String> modeleToCapacite = new HashMap<>();

    // Données
    private ObservableList<Avion> avionList = FXCollections.observableArrayList();
    private Connection connection = Connexion.seConnecter();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableColumns();
        setupFilterOptions();
        loadAvionData();
        setupTableSelectionListener();
        marqueToModeles.put("Airbus", Arrays.asList("A320", "A330"));
        marqueToModeles.put("Boeing", Arrays.asList("737", "777"));

        modeleToCapacite.put("A320", "180");
        modeleToCapacite.put("A330", "260");
        modeleToCapacite.put("737", "160");
        modeleToCapacite.put("777", "300");

        marqueComboBox.getItems().addAll(marqueToModeles.keySet());
        marqueComboBox.setOnAction(e -> handleMarqueSelection());
        modeleComboBox.setOnAction(e -> handleModeleSelection());
        userNameLabel.setText("Mohamed");
        // Nouvelle méthode
    }

    @FXML
    private TableColumn<Avion, Boolean> typeColumn; // Boolean au lieu de String

    // 2. Modifiez la configuration des colonnes
    private void setupTableColumns() {
        registrationColumn.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        marqueColumn.setCellValueFactory(new PropertyValueFactory<>("marque"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("modele"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacite"));


        // Configuration corrigée pour la colonne disponibilité
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        typeColumn.setCellFactory(col -> new TableCell<Avion, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "Disponible" : "Indisponible");
                }
            }
        });
    }

    private void setupFilterOptions() {
        filterComboBox.getItems().addAll("Tous", "Disponibles", "Indisponibles");
        filterComboBox.setValue("Tous");
    }
    private void loadAvionData() {
        System.out.println("Chargement des données...");
        avionList.clear();
        ArrayList<Avion> avions = DAOAvion.lister();
        System.out.println("Nombre d'avions chargés: " + avions.size());
        avionList.addAll(avions);
        aircraftTable.setItems(avionList);
    }

    @FXML
    private void handleLogout() {
        try {
            // Afficher une boîte de dialogue de confirmation
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de déconnexion");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Êtes-vous sûr de vouloir vous déconnecter ?");

            // Si l'utilisateur confirme
            if (confirmation.showAndWait().get() == ButtonType.OK) {
                // Charger l'interface de login
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/gestionvoltunisair/login.fxml"));
                Stage stage = (Stage) aircraftTable.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

                // Optionnel: Fermer la connexion à la base de données
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface de login", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Problème lors de la fermeture de la connexion", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void handleDashboard() {
        try {
            // Charger l'interface gestion-vols.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/gestionvoltunisair/dashboard.fxml"));
            Stage stage = (Stage) aircraftTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface de dashboard", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleFlights() {
        try {
            // Charger l'interface gestion-vols.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/gestionvoltunisair/gestion-vols.fxml"));
            Stage stage = (Stage) aircraftTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface des vols", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAircraft() {
        try {
            // Charger l'interface gestion-vols.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/gestionvoltunisair/gestion-avions.fxml"));
            Stage stage = (Stage) aircraftTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface des avions", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCrew() {
        try {
            // Charger l'interface gestion-vols.fxml
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/gestionvoltunisair/gestion-equipages.fxml"));
            Stage stage = (Stage) aircraftTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger l'interface des vols", Alert.AlertType.ERROR);
        }
    }




    @FXML
    private void handleEdit() {
        // Implémentez l'édition
        System.out.println("Édition demandée");
    }


    @FXML
    private void handleSearch() {
        String keyword = searchField.getText();
        String filter = filterComboBox.getValue();

        ArrayList<Avion> resultats = DAOAvion.chercherAvion(keyword);
        ObservableList<Avion> filteredList = FXCollections.observableArrayList();

        for (Avion avion : resultats) {
            boolean matchesFilter = filter.equals("Tous") ||
                    (filter.equals("Disponibles") && avion.isDisponible()) ||
                    (filter.equals("Indisponibles") && !avion.isDisponible());

            if (matchesFilter) {
                filteredList.add(avion);
            }
        }

        aircraftTable.setItems(filteredList);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void setupTableSelectionListener() {
        aircraftTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    avionSelectionne = newSelection;
                    if (newSelection != null) {
                        fillFormWithSelectedAvion();
                    }
                });
    }

    private void fillFormWithSelectedAvion() {
        if (avionSelectionne != null) {
            registrationField.setText(avionSelectionne.getMatricule());
            marqueComboBox.setValue(avionSelectionne.getMarque());
            modeleComboBox.setValue(avionSelectionne.getModele());
            capacityComboBox.setValue(String.valueOf(avionSelectionne.getCapacite()));
            disponibleCheckBox.setSelected(avionSelectionne.isDisponible());
        }
    }


    private boolean validateForm() {
        if (registrationField.getText().isEmpty() ||
                marqueComboBox.getValue() == null ||
                modeleComboBox.getValue() == null ||
                capacityComboBox.getValue() == null) {

            showAlert("Erreur", "Veuillez remplir tous les champs", Alert.AlertType.ERROR);
            return false;
        }

        try {
            String capaciteText = capacityComboBox.getValue().trim();
            Integer.parseInt(capaciteText);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "La capacité doit être un nombre valide", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }


    private void clearForm() {
        if (registrationField != null) registrationField.clear();
        if (marqueComboBox != null) marqueComboBox.setValue(null);
        if (modeleComboBox != null) modeleComboBox.setValue(null);
        if (capacityComboBox != null) capacityComboBox.setValue(null);
        if (disponibleCheckBox != null) disponibleCheckBox.setSelected(false);

        avionSelectionne = null;
        if (aircraftTable != null) {
            aircraftTable.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void handleSave() {
        if (!validateForm()) return;

        try {
            String matricule = registrationField.getText();

            // Vérification existence matricule seulement pour nouvel avion
            if (avionSelectionne == null && DAOAvion.existeMatricule(matricule)) {
                showAlert("Erreur", "La matricule existe déjà ! Impossible d'ajouter.", Alert.AlertType.ERROR);
                return;
            }

            Avion avion = new Avion(
                    matricule,
                    marqueComboBox.getValue(),
                    modeleComboBox.getValue(),
                    Integer.parseInt(capacityComboBox.getValue()),
                    disponibleCheckBox.isSelected()
            );

            if (avionSelectionne != null) {
                if (DAOAvion.modifier(avion)) {
                    showAlert("Succès", "Avion modifié avec succès", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Erreur", "Échec de la modification", Alert.AlertType.ERROR);
                }
            }
            // Sinon -> AJOUT
            else {
                if (DAOAvion.ajouter(avion)) {
                    showAlert("Succès", "Avion ajouté avec succès", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Erreur", "Échec de l'ajout", Alert.AlertType.ERROR);
                }
            }

            loadAvionData(); // Recharger les données
            clearForm(); // Vider le formulaire

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Capacité invalide", Alert.AlertType.ERROR);
        }
    }

    private void refreshTableData() {
        List<Avion> nouvellesDonnees = DAOAvion.lister();
        avionList.clear();
        avionList.addAll(nouvellesDonnees);
        aircraftTable.refresh();
        System.out.println("Table rafraîchie. Nombre d'éléments: " + avionList.size());
    }
    @FXML
    private void handleDelete() {
        if (avionSelectionne != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet avion ?");

            if (confirmation.showAndWait().get() == ButtonType.OK) {
                if (DAOAvion.supprimer(avionSelectionne)) {
                    showAlert("Succès", "Avion supprimé avec succès", Alert.AlertType.INFORMATION);
                    loadAvionData();
                    clearForm();
                } else {
                    showAlert("Erreur", "Échec de la suppression de l'avion", Alert.AlertType.ERROR);
                }
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un avion à supprimer", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
    }
    @FXML
    private void handleMarqueSelection() {
        String selectedMarque = marqueComboBox.getValue();
        modeleComboBox.getItems().clear();
        capacityComboBox.getItems().clear();
        if (selectedMarque != null) {
            modeleComboBox.getItems().addAll(marqueToModeles.get(selectedMarque));
        }
    }

    @FXML
    private void handleModeleSelection() {
        String selectedModele = modeleComboBox.getValue();
        capacityComboBox.getItems().clear();
        if (selectedModele != null && modeleToCapacite.containsKey(selectedModele)) {
            capacityComboBox.getItems().add(modeleToCapacite.get(selectedModele));
            capacityComboBox.setValue(modeleToCapacite.get(selectedModele)); // Auto selection
        }
    }

}