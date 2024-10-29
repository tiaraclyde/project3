package org.cs213.clinic.jfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.cs213.clinic.core.ClinicManager;
import org.cs213.clinic.core.Location;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClinicManagerController implements Initializable {
    // Reference to your core clinic manager
    private ClinicManager clinicManager;

    // Existing FXML injected fields remain the same...
    @FXML private AnchorPane appointmentPane;
    @FXML private DatePicker appointmentDate;
    @FXML private Label appointmentLabel;
    @FXML private TextField firstName;
    @FXML private TextField lastName;
    @FXML private DatePicker dateOfBirth;
    @FXML private RadioButton officeVisitRadio;
    @FXML private RadioButton imagingServiceRadio;
    @FXML private Label patientLabel;
    @FXML private MenuButton timeslotMenu;
    @FXML private MenuButton providerMenu;
    @FXML private Button loadProvidersButton;
    @FXML private Button scheduleButton;
    @FXML private Button cancelButton;
    @FXML private Button clearButton;

    // Reschedule tab
    @FXML private MenuButton currentTimeslotMenu;
    @FXML private Label rescheduleLabel;
    @FXML private TextField rescheduleFirstName;
    @FXML private TextField rescheduleLastName;
    @FXML private DatePicker rescheduleDob;
    @FXML private Label reschedulePatientLabel;
    @FXML private MenuButton newTimeslotMenu;
    @FXML private Label newTimeslotLabel;

    // Clinic Locations tab
    @FXML private TableColumn<Location, String> cityColumn;
    @FXML private TableColumn<Location, String> countyColumn;
    @FXML private TableColumn<Location, String> zipColumn;
    @FXML private TableView<Location> locationTable;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: Initialize your ClinicManager instance
        try {
            clinicManager = new ClinicManager();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        initializeComponents();
        setupEventHandlers();
        setupTableColumns();
        loadInitialData();
    }

    private void initializeComponents() {
        // Initialize radio button group
        ToggleGroup appointmentType = new ToggleGroup();
        officeVisitRadio.setToggleGroup(appointmentType);
        imagingServiceRadio.setToggleGroup(appointmentType);

        // Initialize default values
        timeslotMenu.setText("Select Timeslot");
        providerMenu.setText("Select Provider");
    }

    private void loadInitialData() {
        // TODO: Load initial data from your ClinicManager
        loadProviders();
        loadTimeslots();
        loadLocations();
    }

    private void setupTableColumns() {
        cityColumn.setCellValueFactory(cellData -> {
            Location location = cellData.getValue();
            String name = location.name().toLowerCase();
            final int restOfWord = 1;
            name = name.substring(0, 1).toUpperCase() + name.substring(restOfWord);
            return new SimpleStringProperty(name);  // Use enum name
        });
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("county"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
    }

    private void setupEventHandlers() {
        loadProvidersButton.setOnAction(e -> loadProviders());
        scheduleButton.setOnAction(e -> scheduleAppointment());
        cancelButton.setOnAction(e -> cancelAppointment());
        clearButton.setOnAction(e -> clearForm());
        appointmentDate.setOnAction(e -> updateAvailableTimeslots());
        currentTimeslotMenu.setOnAction(e -> handleRescheduleRequest());
        newTimeslotMenu.setOnAction(e -> handleNewTimeslotSelection());
    }

    private void loadProviders() {
        // TODO: Load providers from your ClinicManager
        // 1. Clear existing items
        providerMenu.getItems().clear();
        // 2. Get providers from your database
        // 3. Add them as MenuItems to providerMenu
    }

    private void loadTimeslots() {
        // TODO: Load timeslots from your ClinicManager
        // 1. Clear existing items from all timeslot menus
        timeslotMenu.getItems().clear();
        currentTimeslotMenu.getItems().clear();
        newTimeslotMenu.getItems().clear();
        // 2. Get timeslots from your database
        // 3. Add them to all relevant menus
    }

    private void loadLocations() {
        for (Location location : Location.values()) {
            locationTable.getItems().add(location);
        }
    }

    private void scheduleAppointment() {
        if (validateAppointmentInput()) {
            // TODO: Use your ClinicManager to schedule the appointment
            // 1. Get selected appointment type (office visit or imaging)
            // 2. Create appropriate command arguments
            // 3. Execute command via command registry
            // 4. Handle the result
        }
    }

    private void cancelAppointment() {
        if (validateAppointmentInput()) {
            // TODO: Use your ClinicManager to cancel the appointment
            // 1. Create command arguments
            // 2. Execute cancel command
            // 3. Handle the result
            clearForm();
        }
    }

    private void handleRescheduleRequest() {
        // TODO: Handle appointment rescheduling
        // 1. Validate inputs
        // 2. Create command arguments
        // 3. Execute reschedule command
        // 4. Handle the result
    }

    private void handleNewTimeslotSelection() {
        // TODO: Handle the selection of a new timeslot
        // Update any relevant UI elements or state
    }

    private boolean validateAppointmentInput() {
        return !firstName.getText().isEmpty()
                && !lastName.getText().isEmpty()
                && dateOfBirth.getValue() != null
                && appointmentDate.getValue() != null
                && (officeVisitRadio.isSelected() || imagingServiceRadio.isSelected());
    }

    private void clearForm() {
        firstName.clear();
        lastName.clear();
        dateOfBirth.setValue(null);
        appointmentDate.setValue(null);
        officeVisitRadio.setSelected(false);
        imagingServiceRadio.setSelected(false);
        timeslotMenu.setText("Select Timeslot");
        providerMenu.setText("Select Provider");
    }

    private void updateAvailableTimeslots() {
        // TODO: Update available timeslots based on selected date
        // 1. Clear existing timeslots
        timeslotMenu.getItems().clear();
        // 2. Get available timeslots for the selected date
        // 3. Update the timeslot menu
    }

    private void showValidationError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Please check your input");
        alert.setContentText("All fields are required to schedule an appointment.");
        alert.showAndWait();
    }
}