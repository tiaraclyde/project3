package org.cs213.clinic.jfx;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.cs213.clinic.cli.registry.CommandRegistry;
import org.cs213.clinic.core.ClinicManager;
import org.cs213.clinic.core.Doctor;
import org.cs213.clinic.core.Location;
import org.cs213.clinic.core.Provider;
import org.cs213.clinic.core.Radiology;
import org.cs213.clinic.core.Technician;
import org.cs213.clinic.core.Timeslot;
import org.cs213.clinic.util.Format;
import org.cs213.clinic.util.List;

import java.io.File;
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
    @FXML private TextArea outputArea;

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
        clinicManager = new ClinicManager(true);
        initializeComponents();
        setupEventHandlers();
        setupTableColumns();
        loadInitialData();
    }

    private void initializeComponents() {
        // Initialize radio button group
        ToggleGroup appointmentType = new ToggleGroup();
        officeVisitRadio.setToggleGroup(appointmentType);
        officeVisitRadio.onActionProperty().set(e -> loadProviders());
        imagingServiceRadio.setToggleGroup(appointmentType);
        imagingServiceRadio.onActionProperty().set(e -> loadProviders());

        // Initialize default values
        timeslotMenu.setText("Select Timeslot");
        loadTimeslots();
        providerMenu.setText("Select Provider");
    }

    private void loadInitialData() {
        loadProviders();
        loadTimeslots();
        loadLocations();
    }

    private void setupTableColumns() {
        cityColumn.setCellValueFactory(cellData -> {
            Location location = cellData.getValue();
            String name = Format.capitalize(location.name());
            return new SimpleStringProperty(name);  // Use enum name
        });
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("county"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
    }

    private void setupEventHandlers() {
        loadProvidersButton.setOnAction(e -> {
            // Make user select from file dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Providers File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            File file = fileChooser.showOpenDialog(appointmentPane.getScene().getWindow());
            if (file != null) {
                try {
                    clinicManager.loadProviders(file.getAbsolutePath());
                    loadProviders();
                } catch (FileNotFoundException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("File Not Found");
                    alert.setHeaderText("The file could not be found.");
                    alert.setContentText("Please try again.");
                    alert.showAndWait();
                }
                loadProviders();
                loadProvidersButton.setDisable(true);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("File Not Selected");
                alert.setHeaderText("No file was selected.");
                alert.setContentText("Please try again.");
                alert.showAndWait();
            }
        });
        scheduleButton.setOnAction(e -> scheduleAppointment());
        cancelButton.setOnAction(e -> cancelAppointment());
        clearButton.setOnAction(e -> clearForm());
        appointmentDate.setOnAction(e -> updateAvailableTimeslots());
        currentTimeslotMenu.setOnAction(e -> handleRescheduleRequest());
        newTimeslotMenu.setOnAction(e -> handleNewTimeslotSelection());
    }

    private void loadProviders() {
        // 1. Clear existing items
        providerMenu.getItems().clear();
        // 2. Get providers from your database
        if (officeVisitRadio.isSelected()) {
            providerMenu.setText("Select Provider");
            for (Provider provider : clinicManager.getDatabase().getProviders()) {
                if (provider instanceof Technician) continue;
                MenuItem item = new MenuItem(provider.toString());
                item.setOnAction(e -> {
                    providerMenu.setText(Format.capitalize(provider.getProfile().getFname()));
                    providerMenu.setId(((Doctor) provider).getNpi());
                });

                providerMenu.getItems().add(item);
            }
        } else {
            providerMenu.setText("Select Service");
            for (Radiology radiology : Radiology.values()) {
                MenuItem item = new MenuItem(Format.capitalize(radiology.name()));
                item.setOnAction(e -> {
                    providerMenu.setText(Format.capitalize(radiology.name()));
                    providerMenu.setId(radiology.name().toLowerCase());
                });
                providerMenu.getItems().add(item);
            }
        }
    }

    private void loadTimeslots() {
        // 1. Clear existing items from all timeslot menus
        timeslotMenu.getItems().clear();
        currentTimeslotMenu.getItems().clear();
        newTimeslotMenu.getItems().clear();
        // 2. Get timeslots from your database
        // Wait for clinicManager to initialize
        List<Timeslot> timeslots = clinicManager.getDatabase().getTimeslots();
        // 3. Add them to all relevant menus
        for (Timeslot timeslot : timeslots) {
            MenuItem item = new MenuItem(Format.get12Hour(timeslot));
            item.setOnAction(e -> {
                timeslotMenu.setText(Format.get12Hour(timeslot));
                timeslotMenu.setId(clinicManager.getDatabase().getTimeslotId(timeslot));
            });
            timeslotMenu.getItems().add(item);
            MenuItem currentMenuItem = new MenuItem(Format.get12Hour(timeslot));
            currentMenuItem.setOnAction(e -> {
                currentTimeslotMenu.setText(Format.get12Hour(timeslot));
                currentTimeslotMenu.setId(clinicManager.getDatabase().getTimeslotId(timeslot));
            });
            currentTimeslotMenu.getItems().add(currentMenuItem);
            MenuItem newMenuItem = new MenuItem(Format.get12Hour(timeslot));
            newMenuItem.setOnAction(e -> {
                newTimeslotMenu.setText(Format.get12Hour(timeslot));
                newTimeslotMenu.setId(clinicManager.getDatabase().getTimeslotId(timeslot));
            });
            newTimeslotMenu.getItems().add(newMenuItem);
        }
    }

    private void loadLocations() {
        for (Location location : Location.values()) {
            locationTable.getItems().add(location);
        }
    }

    private void scheduleAppointment() {
        if (!validateAppointmentInput()) {
            showValidationError();
            return;
        }

        CommandRegistry commandRegistry = clinicManager.getCommandRegistry();
        if (officeVisitRadio.isSelected()) {
            loadProviders();
            // Schedule an office visit
            String[] args = {
                Format.ukToUsDate(appointmentDate.getValue().toString(), "-", "/"),
                timeslotMenu.getId(),
                firstName.getText(),
                lastName.getText(),
                Format.ukToUsDate(dateOfBirth.getValue().toString(), "-", "/"),
                providerMenu.getId()
            };
            final String docScheduleCommand = "D";
            String out = commandRegistry.executeCommand(docScheduleCommand, args);
            outputArea.appendText(out);  // print to output area
            // print to output area
        } else if (imagingServiceRadio.isSelected()) {
            loadProviders();
            // Schedule an imaging service
            String[] args = {
                Format.ukToUsDate(appointmentDate.getValue().toString(), "-", "/"),
                timeslotMenu.getId(),
                firstName.getText(),
                lastName.getText(),
                Format.ukToUsDate(dateOfBirth.getValue().toString(), "-", "/"),
                providerMenu.getId()
            };
            final String techScheduleCommand = "T";
            String out = commandRegistry.executeCommand(techScheduleCommand, args);
            outputArea.appendText(out);
        }
    }

    private void cancelAppointment() {
        if (!validateAppointmentInput()) {
            showValidationError();
            return;
        }

        String[] args = {
            Format.ukToUsDate(appointmentDate.getValue().toString(), "-", "/"),
            timeslotMenu.getId(),
            firstName.getText(),
            lastName.getText(),
            Format.ukToUsDate(dateOfBirth.getValue().toString(), "-", "/")
        };
        final String cancelCommand = "C";
        String out = clinicManager.getCommandRegistry().executeCommand(cancelCommand, args);
        outputArea.appendText(out);
        clearForm();
    }

    private void handleRescheduleRequest() {
        // TODO: Handle appointment rescheduling
        // 1. Validate inputs
        // 2. Create command arguments
        // 3. Execute reschedule command
        // 4. Handle the result
        if (!validateAppointmentInput()) {
            showValidationError();
            return;
        }

        String[] args = {
            Format.ukToUsDate(appointmentDate.getValue().toString(), "-", "/"),
            currentTimeslotMenu.getId(),
            rescheduleFirstName.getText(),
            rescheduleLastName.getText(),
            Format.ukToUsDate(rescheduleDob.getValue().toString(), "-", "/"),
            newTimeslotMenu.getId()
        };

        final String rescheduleCommand = "R";
        String out = clinicManager.getCommandRegistry().executeCommand(rescheduleCommand, args);
        outputArea.appendText(out);
    }

    private void handleNewTimeslotSelection() {
        newTimeslotMenu.setText(newTimeslotMenu.getText());
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
        loadTimeslots();
    }

    private void showValidationError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Please check your input");
        alert.setContentText("All fields are required to schedule an appointment.");
        alert.showAndWait();
    }
}