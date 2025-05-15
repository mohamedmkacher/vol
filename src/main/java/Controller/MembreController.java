package Controller;

import Classes.Employe;
import Classes.MembreEquipage;
import Classes.Pilote;
import Classes.User;
import DAO.Connexion;
import DAO.DAOMembreEquipage;
import DAO.DAOPilote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MembreController implements Initializable {
    @FXML private ComboBox<String> pilotAvailabilityFilter;
    @FXML private ComboBox<String> crewAvailabilityFilter;
    public Button clearPilotFiltersButton;
    public Button clearCrewFiltersButton;
    @FXML private TableColumn pilotePhoneColumn;
    @FXML private TableColumn crewPhoneColumn;
    @FXML private TextField crewPhone;
    @FXML private TextField pilotePhone;
    @FXML private Label userNameLabel;
    @FXML private Button logoutButton;
    @FXML private Button dashboardButton;
    @FXML private Button flightsButton;
    @FXML private Button aircraftButton;
    @FXML private Button crewButton;

    @FXML private TextField pilotIdField;
    @FXML private TextField pilotLastNameField;
    @FXML private TextField pilotFirstNameField;
    @FXML private Button clearPilotButton;
    @FXML private Button savePilotButton;
    @FXML private TextField pilotSearchField;
    @FXML private Button pilotSearchButton;
    @FXML private TableView<Employe> pilotsTable;
    @FXML private TableColumn<Employe, String> pilotIdColumn;
    @FXML private TableColumn<Employe, String> pilotLastNameColumn;
    @FXML private TableColumn<Employe, String> pilotFirstNameColumn;
    @FXML private TableColumn<Employe, Boolean> pilotAvailabilityColumn;
    @FXML private Button editPilotButton;
    @FXML private Button deletePilotButton;
    @FXML private TextField crewIdField;
    @FXML private TextField crewLastNameField;
    @FXML private TextField crewFirstNameField;
    @FXML private Button clearCrewButton;
    @FXML private Button saveCrewButton;
    @FXML private TextField crewSearchField;
    @FXML private Button crewSearchButton;
    @FXML private Button editCrewButton;
    @FXML private Button deleteCrewButton;
    @FXML private Tab piloteWindow;
    @FXML private TextField PilotEmailField;
    @FXML private DatePicker pilotHireDateField;
    @FXML private CheckBox PilotAvailibilityField;
    @FXML private TableColumn<Employe, String> pilotEmailColumn;
    @FXML private TableColumn<Employe, Date> pilotHireDateColumn;
    @FXML private Tab crewMemberWindow;
    @FXML private DatePicker crewHireDateField;
    @FXML private CheckBox crewAvailibilityField;
    @FXML private TextField crewEmailField;
    @FXML private TableView<Employe> crewTable;
    @FXML private TableColumn<Employe, String> crewIdColumn;
    @FXML private TableColumn<Employe, String> crewLastNameColumn;
    @FXML private TableColumn<Employe, String> crewFirstNameColumn;
    @FXML private TableColumn<Employe, String> crewEmailColumn;
    @FXML private TableColumn<Employe, Date> crewHireDateColumn;
    @FXML private TableColumn<Employe, Boolean> crewAvailabilityColumn;

    private final ObservableList<Employe> piloteData = FXCollections.observableArrayList();
    private final ObservableList<Employe> crewData = FXCollections.observableArrayList();
    private Employe selectedPilot = null;
    private Employe selectedCrewMember = null;
    private User currentUser;
    private final EmailValidator emailValidator = new EmailValidator();
    private final PhoneValidator phoneValidator = new PhoneValidator();


    Connection connection = Connexion.seConnecter();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            userNameLabel.setText("Mohamed");
            initializePilotTable();
            initializeCrewTable();
            initializeFilters();
            loadPilots();
            loadCrewMembers();
            setupValidators();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur d'initialisation",
                    "Échec de l'initialisation: " + e.getMessage());
        }
    }
    private void initializePilotTable() {
        setTableColumnProperties(pilotIdColumn, pilotLastNameColumn, pilotFirstNameColumn,
                pilotEmailColumn, pilotHireDateColumn, pilotAvailabilityColumn, pilotePhoneColumn);
        pilotsTable.setItems(piloteData);
        setupPilotTableSelection();
    }

    private void setupValidators() {
        // Validation en temps réel pour les emails
        PilotEmailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!emailValidator.validate(newValue)) {
                PilotEmailField.setStyle("-fx-border-color: red;");
            } else {
                PilotEmailField.setStyle("");
            }
        });

        crewEmailField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!emailValidator.validate(newValue)) {
                crewEmailField.setStyle("-fx-border-color: red;");
            } else {
                crewEmailField.setStyle("");
            }
        });

        // Validation en temps réel pour les numéros de téléphone
        pilotePhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!phoneValidator.validate(newValue)) {
                pilotePhone.setStyle("-fx-border-color: red;");
            } else {
                pilotePhone.setStyle("");
            }
        });

        crewPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!phoneValidator.validate(newValue)) {
                crewPhone.setStyle("-fx-border-color: red;");
            } else {
                crewPhone.setStyle("");
            }
        });

        // Validation CIN
        pilotIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{8}")) {
                pilotIdField.setStyle("-fx-border-color: red;");
            } else {
                pilotIdField.setStyle("");
            }
        });

        crewIdField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{8}")) {
                crewIdField.setStyle("-fx-border-color: red;");
            } else {
                crewIdField.setStyle("");
            }
        });
    }

    private void initializeFilters() {
        // Initialiser les options de disponibilité
        ObservableList<String> availabilityOptions = FXCollections.observableArrayList(
                "Tous",
                "Disponible",
                "Non disponible"
        );

        pilotAvailabilityFilter.setItems(availabilityOptions);
        crewAvailabilityFilter.setItems(availabilityOptions);

        // Définir la valeur par défaut
        pilotAvailabilityFilter.setValue("Tous");
        crewAvailabilityFilter.setValue("Tous");

        // Ajouter les listeners pour les filtres
        pilotAvailabilityFilter.setOnAction(this::handlePilotFilter);
        crewAvailabilityFilter.setOnAction(this::handleCrewFilter);
    }


    @FXML
    public void handlePilotFilter(ActionEvent event) {
        Boolean disponibilite = null;
        String filterValue = pilotAvailabilityFilter.getValue();
        if ("Disponible".equals(filterValue)) {
            disponibilite = true;
        } else if ("Non disponible".equals(filterValue)) {
            disponibilite = false;
        }

        piloteData.clear();
        piloteData.addAll(DAOPilote.filtrerParDisponibilite(disponibilite));
    }

    @FXML
    public void handleCrewFilter(ActionEvent event) {
        Boolean disponibilite = null;
        String filterValue = crewAvailabilityFilter.getValue();
        if ("Disponible".equals(filterValue)) {
            disponibilite = true;
        } else if ("Non disponible".equals(filterValue)) {
            disponibilite = false;
        }

        crewData.clear();
        crewData.addAll(DAOMembreEquipage.filtrerParDisponibilite(disponibilite));
    }


    private void initializeCrewTable() {
        setTableColumnProperties(crewIdColumn, crewLastNameColumn, crewFirstNameColumn,
                crewEmailColumn, crewHireDateColumn, crewAvailabilityColumn, crewPhoneColumn);
        crewTable.setItems(crewData);
        setupCrewTableSelection();
    }

    private void setTableColumnProperties(TableColumn<Employe, String> idCol,
                                          TableColumn<Employe, String> lastNameCol,
                                          TableColumn<Employe, String> firstNameCol,
                                          TableColumn<Employe, String> emailCol,
                                          TableColumn<Employe, Date> hireDateCol,
                                          TableColumn<Employe, Boolean> availabilityCol,
                                          TableColumn phoneCol) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("cin"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        hireDateCol.setCellValueFactory(new PropertyValueFactory<>("dateEmbauche"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("disponibilite"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("num_tel"));
    }


    private void setupPilotTableSelection() {
        pilotsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedPilot = newSelection;
                    if (newSelection != null) {
                        showPilotDetails(newSelection);
                    }
                });
    }
    @FXML
    public void handlePilotSearch(ActionEvent event) {
        String searchText = pilotSearchField.getText().trim();
        searchText = searchText.isEmpty() ? null : searchText;

        piloteData.clear();
        piloteData.addAll(DAOPilote.chercherPilotes(searchText));
    }

    @FXML
    public void handleCrewSearch(ActionEvent event) {
        String searchText = crewSearchField.getText().trim();
        searchText = searchText.isEmpty() ? null : searchText;

        crewData.clear();
        crewData.addAll(DAOMembreEquipage.chercher(searchText));
    }


    @FXML
    public void handleClearFilters(ActionEvent event) {
        pilotSearchField.clear();
        crewSearchField.clear();
        pilotAvailabilityFilter.setValue("Tous");
        crewAvailabilityFilter.setValue("Tous");
        loadPilots();
        loadCrewMembers();
    }

    private void setupCrewTableSelection() {
        crewTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    selectedCrewMember = newSelection;
                    if (newSelection != null) {
                        showCrewDetails(newSelection);
                    }
                });
    }

    @FXML
    public void handleSavePilot(ActionEvent event) {
        if (!validatePilotFields()) {
            return;
        }

        try {
            Pilote pilote = createPiloteFromFields();
            // Vérifier l'existence du CIN uniquement pour un nouvel ajout
            if (selectedPilot == null) {
                if (DAOPilote.existsByCin(pilote.getCin())) {
                    showAlert(Alert.AlertType.ERROR, "Erreur",
                            "Un pilote avec ce CIN existe déjà.");
                    return;
                }
            }

            boolean success = selectedPilot == null ?
                    DAOPilote.ajouter(pilote) :
                    DAOPilote.modifier(pilote);

            if (success) {
                loadPilots();
                clearPilotFields();
                showAlert(Alert.AlertType.INFORMATION, "Succès",
                        selectedPilot == null ? "Pilote ajouté" : "Pilote mis à jour");
                selectedPilot = null;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de l'enregistrement du pilote: " + e.getMessage());
        }
    }

    @FXML
    public void handleSaveCrew(ActionEvent event) {
        if (!validateCrewFields()) {
            return;
        }

        try {
            MembreEquipage membre = createCrewMemberFromFields();
            // Vérifier l'existence du CIN uniquement pour un nouvel ajout
            if (selectedCrewMember == null) {
                if (DAOMembreEquipage.existsByCin(membre.getCin())) {
                    showAlert(Alert.AlertType.ERROR, "Erreur",
                            "Un membre d'équipage avec ce CIN existe déjà.");
                    return;
                }
            }

            boolean success = selectedCrewMember == null ?
                    DAOMembreEquipage.ajouter(membre) :
                    DAOMembreEquipage.modifier(membre);

            if (success) {
                loadCrewMembers();
                clearCrewFields();
                showAlert(Alert.AlertType.INFORMATION, "Succès",
                        selectedCrewMember == null ? "Membre d'équipage ajouté" : "Membre d'équipage mis à jour");
                selectedCrewMember = null;
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de l'enregistrement du membre d'équipage: " + e.getMessage());
        }
    }
    private Pilote createPiloteFromFields() {
        Pilote pilote = new Pilote(
                pilotIdField.getText(),
                pilotLastNameField.getText(),
                pilotFirstNameField.getText(),
                PilotEmailField.getText(),
                Date.valueOf(pilotHireDateField.getValue()),
                PilotAvailibilityField.isSelected(),
                pilotePhone.getText()
        );
        // Ajouter l'ID si c'est une modification
        if (selectedPilot != null) {
            pilote.setId(selectedPilot.getId());
        }
        return pilote;
    }

    private MembreEquipage createCrewMemberFromFields() {
        MembreEquipage membre = new MembreEquipage(
                crewIdField.getText(),
                crewLastNameField.getText(),
                crewFirstNameField.getText(),
                crewEmailField.getText(),
                Date.valueOf(crewHireDateField.getValue()),
                crewAvailibilityField.isSelected(),
                crewPhone.getText()
        );
        // Ajouter l'ID si c'est une modification
        if (selectedCrewMember != null) {
            membre.setId(selectedCrewMember.getId());
        }
        return membre;
    }


    private boolean validatePilotFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (!pilotIdField.getText().matches("\\d{8}")) {
            errorMessage.append("Le CIN doit contenir exactement 8 chiffres.\n");
        }
        if (pilotLastNameField.getText().isEmpty()) {
            errorMessage.append("Le nom est requis.\n");
        }
        if (pilotFirstNameField.getText().isEmpty()) {
            errorMessage.append("Le prénom est requis.\n");
        }
        if (!emailValidator.validate(PilotEmailField.getText())) {
            errorMessage.append("Email invalide.\n");
        }
        if (!phoneValidator.validate(pilotePhone.getText())) {
            errorMessage.append("Numéro de téléphone invalide.\n");
        }
        if (pilotHireDateField.getValue() == null) {
            errorMessage.append("La date d'embauche est requise.\n");
        } else if (pilotHireDateField.getValue().isAfter(LocalDate.now())) {
            errorMessage.append("La date d'embauche ne peut pas être dans le futur.\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", errorMessage.toString());
            return false;
        }
        return true;
    }

    private boolean validateCrewFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (!crewIdField.getText().matches("\\d{8}")) {
            errorMessage.append("Le CIN doit contenir exactement 8 chiffres.\n");
        }
        if (crewLastNameField.getText().isEmpty()) {
            errorMessage.append("Le nom est requis.\n");
        }
        if (crewFirstNameField.getText().isEmpty()) {
            errorMessage.append("Le prénom est requis.\n");
        }
        if (!emailValidator.validate(crewEmailField.getText())) {
            errorMessage.append("Email invalide.\n");
        }
        if (!phoneValidator.validate(crewPhone.getText())) {
            errorMessage.append("Numéro de téléphone invalide.\n");
        }
        if (crewHireDateField.getValue() == null) {
            errorMessage.append("La date d'embauche est requise.\n");
        } else if (crewHireDateField.getValue().isAfter(LocalDate.now())) {
            errorMessage.append("La date d'embauche ne peut pas être dans le futur.\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation", errorMessage.toString());
            return false;
        }
        return true;
    }

    private static class EmailValidator {
        private final Pattern pattern = Pattern.compile(
                "^[A-Za-z0-9+_.-]+@(.+)$"
        );

        public boolean validate(String email) {
            if (email == null) return false;
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }

    private static class PhoneValidator {
        private final Pattern pattern = Pattern.compile(
                "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$"
        );

        public boolean validate(String phone) {
            if (phone == null) return false;
            Matcher matcher = pattern.matcher(phone);
            return matcher.matches();
        }
    }



    public void setCurrentUser(User user) {
        this.currentUser = user;
        userNameLabel.setText(user.getUsername());
    }





    private void loadPilots() {
        piloteData.clear();
        piloteData.addAll(DAOPilote.lister());
    }

    private void loadCrewMembers() {
        crewData.clear();
        crewData.addAll(DAOMembreEquipage.lister());
    }

    @FXML
    public void handleClearPilot(ActionEvent event) {
        clearPilotFields();
        selectedPilot = null;
    }

    @FXML
    public void handleClearCrew(ActionEvent event) {
        clearCrewFields();
        selectedCrewMember = null;
    }

    private void clearPilotFields() {
        pilotIdField.clear();
        pilotLastNameField.clear();
        pilotFirstNameField.clear();
        PilotEmailField.clear();
        pilotHireDateField.setValue(null);
        PilotAvailibilityField.setSelected(false);
        pilotePhone.clear();
        pilotIdField.setStyle("");
        PilotEmailField.setStyle("");
        pilotePhone.setStyle("");
    }

    private void clearCrewFields() {
        crewIdField.clear();
        crewLastNameField.clear();
        crewFirstNameField.clear();
        crewEmailField.clear();
        crewHireDateField.setValue(null);
        crewAvailibilityField.setSelected(false);
        crewPhone.clear();
        crewIdField.setStyle("");
        crewEmailField.setStyle("");
        crewPhone.setStyle("");
    }

    @FXML
    public void handleDeletePilot(ActionEvent event) {
        if (selectedPilot == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement",
                    "Veuillez sélectionner un pilote à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Êtes-vous sûr de vouloir supprimer ce pilote ?",
                ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (DAOPilote.supprimer((Pilote) selectedPilot)) {
                    loadPilots();
                    clearPilotFields();
                    showAlert(Alert.AlertType.INFORMATION, "Succès",
                            "Pilote supprimé avec succès");
                    selectedPilot = null;
                }
            }
        });
    }

    @FXML
    public void handleDeleteCrew(ActionEvent event) {
        if (selectedCrewMember == null) {
            showAlert(Alert.AlertType.WARNING, "Avertissement",
                    "Veuillez sélectionner un membre d'équipage à supprimer");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Êtes-vous sûr de vouloir supprimer ce membre d'équipage ?",
                ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (DAOMembreEquipage.supprimer((MembreEquipage) selectedCrewMember)) {
                    loadCrewMembers();
                    clearCrewFields();
                    showAlert(Alert.AlertType.INFORMATION, "Succès",
                            "Membre d'équipage supprimé avec succès");
                    selectedCrewMember = null;
                }
            }
        });
    }

    private void showPilotDetails(Employe pilot) {
        pilotIdField.setText(pilot.getCin());
        pilotLastNameField.setText(pilot.getNom());
        pilotFirstNameField.setText(pilot.getPrenom());
        PilotEmailField.setText(pilot.getEmail());
        pilotHireDateField.setValue(pilot.getDateEmbauche().toLocalDate());
        PilotAvailibilityField.setSelected(pilot.getDisponibilite());
        pilotePhone.setText(pilot.getNum_tel());
    }

    private void showCrewDetails(Employe crewMember) {
        crewIdField.setText(crewMember.getCin());
        crewLastNameField.setText(crewMember.getNom());
        crewFirstNameField.setText(crewMember.getPrenom());
        crewEmailField.setText(crewMember.getEmail());
        crewHireDateField.setValue(crewMember.getDateEmbauche().toLocalDate());
        crewAvailibilityField.setSelected(crewMember.getDisponibilite());
        crewPhone.setText(crewMember.getNum_tel());
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void switchToPilotWindow(Event event) {
        loadPilots();
    }

    @FXML
    public void switchToCrewMemberWindow(Event event) {
        loadCrewMembers();
    }

    @FXML
    public void handleEditPilot(ActionEvent event) {
        if (selectedPilot != null) {
            showPilotDetails(selectedPilot);
        }
    }

    @FXML
    public void handleEditCrew(ActionEvent event) {
        if (selectedCrewMember != null) {
            showCrewDetails(selectedCrewMember);
        }
    }

    // Méthodes de navigation
    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de déconnexion");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Êtes-vous sûr de vouloir vous déconnecter ?");

            if (confirmation.showAndWait().get() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/gestionvoltunisair/login.fxml"));
                Stage stage = (Stage) pilotsTable.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            }
        } catch (IOException | SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Erreur lors de la déconnexion: " + e.getMessage());
        }
    }

    @FXML
    public void handleDashboard(ActionEvent event) {
        navigateToView("/com/example/gestionvoltunisair/dashboard.fxml", "Tableau de bord");
    }

    @FXML
    public void handleFlights(ActionEvent event) {
        navigateToView("/com/example/gestionvoltunisair/gestion-vols.fxml", "Gestion des vols");
    }

    @FXML
    public void handleAircraft(ActionEvent event) {
        navigateToView("/com/example/gestionvoltunisair/gestion-avions.fxml", "Gestion des avions");
    }

    @FXML
    public void handleCrew(ActionEvent event) {
        // Déjà sur la page de gestion des équipages
    }



    private void navigateToView(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) pilotsTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur",
                    "Impossible de charger l'interface " + title + ": " + e.getMessage());
        }
    }
}