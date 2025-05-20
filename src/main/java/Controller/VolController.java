package Controller;

import Classes.*;
import DAO.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class VolController {
    @FXML private TableColumn<Vol, LocalDate> departureDateColumn;
    @FXML private TableColumn<Vol, LocalTime> departureTimeColumn;
    @FXML private TableColumn<Vol, LocalDate> arrivalDateColumn;
    @FXML private TableColumn<Vol, LocalTime> arrivalTimeColumn;
    @FXML private Label userNameLabel;
    @FXML private Button logoutButton;
    @FXML private Button dashboardButton;
    @FXML private Button flightsButton;
    @FXML private Button aircraftButton;
    @FXML private Button crewButton;
    @FXML private TabPane flightCreationTabPane;
    @FXML private Tab assignationsTab;
    @FXML private TextField flightNumberField;
    @FXML private ComboBox<String> flightTypeComboBox;
    @FXML private ComboBox<String> originComboBox;
    @FXML private DatePicker departureDatePicker;
    @FXML private TextField departureTimeField;
    @FXML private ComboBox<String> destinationComboBox;
    @FXML private DatePicker arrivalDatePicker;
    @FXML private TextField arrivalTimeField;
    @FXML private ComboBox<String> aircraftComboBox;
    @FXML private ComboBox<String> pilotComboBox;
    @FXML private ListView<String> crewMembersListView;
    @FXML private Button saveButton;
    @FXML private ComboBox<String> filterTypeComboBox;
    @FXML private TextField searchField;
    @FXML private TableView<Vol> flightsTable;
    @FXML private TableColumn<Vol, String> flightNumberColumn;
    @FXML private TableColumn<Vol, String> originColumn;
    @FXML private TableColumn<Vol, String> destinationColumn;

    @FXML private TableColumn<Vol, String> flightTypeColumn;

    private ObservableList<Vol> flightData = FXCollections.observableArrayList();
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private Vol currentEditingVol;

    @FXML
    public void initialize() {
        userNameLabel.setText("Mohamed");

        // Initialisation des ComboBox
        flightTypeComboBox.setItems(FXCollections.observableArrayList("COURT","MOYEN", "LONG"));
        originComboBox.setItems(FXCollections.observableArrayList("TUN", "CDG", "JFK", "LHR"));
        destinationComboBox.setItems(FXCollections.observableArrayList("MIR", "ORY", "DXB", "FRA"));

        try {
            // Chargement des données depuis la BDD
            List<String> avions = DAOAvion.getAllAvionIdentifiers();
            aircraftComboBox.setItems(FXCollections.observableArrayList(avions));

            List<String> pilotes = DAOPilote.getAllPiloteNames();
            pilotComboBox.setItems(FXCollections.observableArrayList(pilotes));

            List<String> membresEquipage = DAOMembreEquipage.getAllMembreEquipageNames();
            crewMembersListView.setItems(FXCollections.observableArrayList(membresEquipage));

        } catch (Exception e) {
            Logger.getLogger(VolController.class.getName()).log(Level.SEVERE, "Erreur lors du chargement des données", e);
            showAlert(Alert.AlertType.ERROR, "Erreur de chargement",
                    "Impossible de charger les données depuis la base de données.");
        }

        configureTableColumns();
        initializeFilters();

        setColumnHeaders();

        loadFlights();
    }


    private void configureTableColumns() {
        flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("numero_vol"));
        originColumn.setCellValueFactory(new PropertyValueFactory<>("provenance"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));

        // Configuration des colonnes de date et heure de départ
        departureDateColumn.setCellValueFactory(cellData -> {
            LocalDateTime dateTime = cellData.getValue().getDate_heure_depart();
            return new SimpleObjectProperty<>(dateTime.toLocalDate());
        });
        departureDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });

        departureTimeColumn.setCellValueFactory(cellData -> {
            LocalDateTime dateTime = cellData.getValue().getDate_heure_depart();
            return new SimpleObjectProperty<>(dateTime.toLocalTime());
        });
        departureTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalTime time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText(null);
                } else {
                    setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
                }
            }
        });

        // Configuration des colonnes de date et heure d'arrivée
        arrivalDateColumn.setCellValueFactory(cellData -> {
            LocalDateTime dateTime = cellData.getValue().getDate_heure_arrivee();
            return new SimpleObjectProperty<>(dateTime.toLocalDate());
        });
        arrivalDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });

        arrivalTimeColumn.setCellValueFactory(cellData -> {
            LocalDateTime dateTime = cellData.getValue().getDate_heure_arrivee();
            return new SimpleObjectProperty<>(dateTime.toLocalTime());
        });
        arrivalTimeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalTime time, boolean empty) {
                super.updateItem(time, empty);
                if (empty || time == null) {
                    setText(null);
                } else {
                    setText(time.format(DateTimeFormatter.ofPattern("HH:mm")));
                }
            }
        });

        flightTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type_trajet"));
    }
    private void setColumnHeaders() {
        departureDateColumn.setText("Date de départ");
        departureTimeColumn.setText("Heure de départ");
        arrivalDateColumn.setText("Date d'arrivée");
        arrivalTimeColumn.setText("Heure d'arrivée");
    }

    private void loadFlights() {
        try {
            List<Vol> vols = DAOVol.lister();
            flightData.setAll(vols);
            flightsTable.setItems(flightData);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les vols.");
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateFlightDetails() || !validateAssignments()) {
            return;
        }

        try {
            Vol vol = createVolFromForm();

            if (currentEditingVol != null) {
                updateVol(vol);
            } else {
                saveNewVol(vol);
            }

            handleClear(null);
            setEditMode(null);
            flightCreationTabPane.getSelectionModel().selectFirst();
            loadFlights();

        } catch (Exception e) {
            Logger.getLogger(VolController.class.getName()).log(Level.SEVERE,
                    "Erreur lors de l'enregistrement du vol", e);
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Une erreur est survenue lors de l'enregistrement: " + e.getMessage());
        }
    }

    private Vol createVolFromForm() {
        Vol vol = new Vol();

        vol.setNumero_vol(flightNumberField.getText());
        vol.setType_trajet(TypeTrajet.valueOf(flightTypeComboBox.getValue()));
        vol.setProvenance(originComboBox.getValue());
        vol.setDestination(destinationComboBox.getValue());

        LocalDateTime departureDateTime = LocalDateTime.of(
                departureDatePicker.getValue(),
                LocalTime.parse(departureTimeField.getText(), timeFormatter)
        );
        LocalDateTime arrivalDateTime = LocalDateTime.of(
                arrivalDatePicker.getValue(),
                LocalTime.parse(arrivalTimeField.getText(), timeFormatter)
        );

        vol.setDate_heure_depart(departureDateTime);
        vol.setDate_heure_arrivee(arrivalDateTime);

        vol.setAvion(new Avion(aircraftComboBox.getValue(), null, null, 0, false));
        vol.setPilote(new Pilote(pilotComboBox.getValue(), null, null, null, null, null, null));
        vol.setNombre_passagers(crewMembersListView.getSelectionModel().getSelectedItems().size());

        if (currentEditingVol != null) {
            vol.setId_vol(currentEditingVol.getId_vol());
        }

        return vol;
    }

    private void saveNewVol(Vol vol) {
        try {
            DAOVol.ajouter(vol);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Vol créé avec succès.");
            loadFlights();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la création du vol.");
        }
    }

    private void updateVol(Vol vol) {
        try {
            DAOVol.modifier(vol);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Vol mis à jour avec succès.");
            loadFlights();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la mise à jour du vol.");
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        flightNumberField.clear();
        flightTypeComboBox.getSelectionModel().clearSelection();
        originComboBox.getSelectionModel().clearSelection();
        departureDatePicker.setValue(null);
        departureTimeField.clear();
        destinationComboBox.getSelectionModel().clearSelection();
        arrivalDatePicker.setValue(null);
        arrivalTimeField.clear();
        aircraftComboBox.getSelectionModel().clearSelection();
        pilotComboBox.getSelectionModel().clearSelection();
        crewMembersListView.getSelectionModel().clearSelection();
        setEditMode(null);
    }

    @FXML
    private void handleEdit(ActionEvent event) {
        Vol selectedVol = flightsTable.getSelectionModel().getSelectedItem();
        if (selectedVol != null) {
            populateFormWithVol(selectedVol);
            setEditMode(selectedVol);
            flightCreationTabPane.getSelectionModel().selectFirst();
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection",
                    "Veuillez sélectionner un vol à modifier.");
        }
    }

    private void populateFormWithVol(Vol vol) {
        flightNumberField.setText(vol.getNumero_vol());
        flightTypeComboBox.setValue(vol.getType_trajet().toString());
        originComboBox.setValue(vol.getProvenance());
        destinationComboBox.setValue(vol.getDestination());

        LocalDateTime departureDateTime = vol.getDate_heure_depart();
        departureDatePicker.setValue(departureDateTime.toLocalDate());
        departureTimeField.setText(departureDateTime.toLocalTime().format(timeFormatter));

        LocalDateTime arrivalDateTime = vol.getDate_heure_arrivee();
        arrivalDatePicker.setValue(arrivalDateTime.toLocalDate());
        arrivalTimeField.setText(arrivalDateTime.toLocalTime().format(timeFormatter));

        if (vol.getAvion() != null) {
            aircraftComboBox.setValue(vol.getAvion().toString());
        }

        if (vol.getPilote() != null) {
            pilotComboBox.setValue(vol.getPilote().toString());
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Vol selectedVol = flightsTable.getSelectionModel().getSelectedItem();
        if (selectedVol != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de Suppression");
            alert.setHeaderText("Supprimer le vol : " + selectedVol.getNumero_vol());
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce vol ?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        DAOVol.supprimer(selectedVol);
                        flightData.remove(selectedVol);
                        showAlert(Alert.AlertType.INFORMATION, "Suppression", "Vol supprimé avec succès.");
                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la suppression du vol.");
                    }
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un vol à supprimer.");
        }
    }

    private void setEditMode(Vol vol) {
        currentEditingVol = vol;
        saveButton.setText(vol != null ? "Mettre à jour" : "Enregistrer");
    }

    private boolean validateFlightDetails() {
        StringBuilder errors = new StringBuilder();

        if (flightNumberField.getText().trim().isEmpty()) {
            errors.append("- Numéro de vol manquant.\n");
        }
        if (flightTypeComboBox.getValue() == null) {
            errors.append("- Type de trajet manquant.\n");
        }
        if (originComboBox.getValue() == null) {
            errors.append("- Provenance manquante.\n");
        }
        if (departureDatePicker.getValue() == null) {
            errors.append("- Date de départ manquante.\n");
        }
        if (departureTimeField.getText().trim().isEmpty()) {
            errors.append("- Heure de départ manquante.\n");
        }
        if (destinationComboBox.getValue() == null) {
            errors.append("- Destination manquante.\n");
        }
        if (arrivalDatePicker.getValue() == null) {
            errors.append("- Date d'arrivée manquante.\n");
        }
        if (arrivalTimeField.getText().trim().isEmpty()) {
            errors.append("- Heure d'arrivée manquante.\n");
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreurs de Validation", errors.toString());
            return false;
        }
        return true;
    }

    private boolean validateAssignments() {
        StringBuilder errors = new StringBuilder();

        if (aircraftComboBox.getValue() == null) {
            errors.append("- Avion manquant.\n");
        }
        if (pilotComboBox.getValue() == null) {
            errors.append("- Pilote manquant.\n");
        }
        if (crewMembersListView.getSelectionModel().getSelectedItems().isEmpty()) {
            errors.append("- Au moins un membre d'équipage doit être sélectionné.\n");
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreurs de Validation", errors.toString());
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void initializeFilters() {
        filterTypeComboBox.setItems(FXCollections.observableArrayList("Tous", "COURT", "MOYEN", "LONG"));
        filterTypeComboBox.setValue("Tous");
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText().toLowerCase();
        String filterType = filterTypeComboBox.getValue();

        ObservableList<Vol> filteredList = flightData.stream()
                .filter(vol -> {
                    boolean matchesSearch = searchTerm.isEmpty() ||
                            vol.getNumero_vol().toLowerCase().contains(searchTerm) ||
                            vol.getProvenance().toLowerCase().contains(searchTerm) ||
                            vol.getDestination().toLowerCase().contains(searchTerm);

                    boolean matchesType = "Tous".equals(filterType) ||
                            vol.getType_trajet().toString().equals(filterType);

                    return matchesSearch && matchesType;
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        flightsTable.setItems(filteredList);
    }

    @FXML
    private void handleGoToAssignments() {
        if (validateFlightDetails()) {
            if (flightCreationTabPane != null && assignationsTab != null) {
                flightCreationTabPane.getSelectionModel().select(assignationsTab);
                System.out.println("Navigated to Assignments tab");
            }
        } else {
            System.out.println("Validation failed for flight details.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Afficher une confirmation avant la déconnexion
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation de déconnexion");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Êtes-vous sûr de vouloir vous déconnecter ?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Nettoyer les données de session si nécessaire
                // Par exemple : userNameLabel.setText("");

                // Charger la vue de login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestionvoltunisair/login.fxml"));
                Parent loginView = loader.load();

                // Obtenir la scène actuelle
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                // Définir la nouvelle scène
                Scene scene = new Scene(loginView);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de la déconnexion: " + e.getMessage());
        }
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestionvoltunisair/dashboard.fxml"));
            Parent dashboardView = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(dashboardView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement du tableau de bord: " + e.getMessage());
        }
    }

    @FXML
    private void handleFlights(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("//com/example/gestionvoltunisair/gestion-vols.fxml"));
            Parent flightsView = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(flightsView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des vols: " + e.getMessage());
        }
    }

    @FXML
    private void handleAircraft(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/aircraft.fxml/com/example/gestionvoltunisair/gestion-avions.fxml"));
            Parent aircraftView = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(aircraftView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des avions: " + e.getMessage());
        }
    }

    @FXML
    private void handleCrew(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gestionvoltunisair/gestion-equipages.fxml"));
            Parent crewView = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(crewView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement de l'équipage: " + e.getMessage());
        }
    }



}