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

/**
 * The ClinicManagerController class is the controller for the ClinicManager
 * application. It is responsible for handling the user interface events and
 * updating the user interface based on the user's input.
 */
public class ClinicManagerController implements Initializable {
    // Reference to your core clinic manager
    /** The ClinicManager instance that manages the clinic. */
    private ClinicManager clinicManager;

    // Existing FXML injected fields remain the same...
    /** The anchor pane for the appointment tab. */
    @FXML private AnchorPane appointmentPane;
    /** The date picker for the appointment date. */
    @FXML private DatePicker appointmentDate;
    /** The label for the appointment tab. */
    @FXML private Label appointmentLabel;
    /** The text field for the patient's first name. */
    @FXML private TextField firstName;
    /** The text field for the patient's last name. */
    @FXML private TextField lastName;
    /** The date picker for the patient's date of birth. */
    @FXML private DatePicker dateOfBirth;
    /** The radio button for an office visit. */
    @FXML private RadioButton officeVisitRadio;
    /** The radio button for an imaging service. */
    @FXML private RadioButton imagingServiceRadio;
    /** The label for the patient's name. */
    @FXML private Label patientLabel;
    /** The menu button for the timeslot. */
    @FXML private MenuButton timeslotMenu;
    /** The menu button for the provider. */
    @FXML private MenuButton providerMenu;
    /** The button to load providers. */
    @FXML private Button loadProvidersButton;
    /** The button to schedule an appointment. */
    @FXML private Button scheduleButton;
    /** The button to cancel an appointment. */
    @FXML private Button cancelButton;
    /** The button to clear the form. */
    @FXML private Button clearButton;
    /** The text area for the output. */
    @FXML private TextArea outputArea;

    // Reschedule tab
    /** The menu button for the current timeslot. */
    @FXML private MenuButton currentTimeslotMenu;
    /** The label for the reschedule tab. */
    @FXML private Label rescheduleLabel;
    /** The date picker for the reschedule date. */
    @FXML private DatePicker rescheduleDate;
    /** The text field for the patient's first name. */
    @FXML private TextField rescheduleFirstName;
    /** The text field for the patient's last name. */
    @FXML private TextField rescheduleLastName;
    /** The date picker for the patient's date of birth. */
    @FXML private DatePicker rescheduleDob;
    /** The label for the reschedule patient. */
    @FXML private Label reschedulePatientLabel;
    /** The text area for the reschedule output. */
    @FXML private TextArea rescheduleOutputArea;
    /** The button to reschedule an appointment. */
    @FXML private Button rescheduleButton;
    /** The menu button for the new timeslot. */
    @FXML private MenuButton newTimeslotMenu;
    /** The label for the new timeslot. */
    @FXML private Label newTimeslotLabel;

    // Clinic Locations tab
    /** The table column for the city. */
    @FXML private TableColumn<Location, String> cityColumn;
    /** The table column for the county. */
    @FXML private TableColumn<Location, String> countyColumn;
    /** The table column for the zip code. */
    @FXML private TableColumn<Location, String> zipColumn;
    /** The table view for the locations. */
    @FXML private TableView<Location> locationTable;

    // View tab
    /** The text area for the view output. */
    @FXML private TextArea viewOutputArea;
    /** The button to view the output. */
    @FXML private Button viewButton;
    /** The menu button for the view print options. */
    @FXML private MenuButton viewPrintMenu;

    /**
     * Constructs a ClinicManagerController with the specified ClinicManager.
     * This constructor is used for testing purposes.
     */
    public ClinicManagerController() {
        // Default constructor
    }

    /**
     * Initializes the controller class for the gui application.
     *
     * @param url the URL to the FXML file
     * @param rb the resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO: Initialize your ClinicManager instance
        clinicManager = new ClinicManager(true);
        initializeComponents();
        setupEventHandlers();
        setupTableColumns();
        loadInitialData();
    }

    /**
     * Initializes the components for the gui application and references
     * the clinic manager command options.
     */
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

    /**
     * Loads the initial data for the clinic manager application.
     * This includes loading the providers, timeslots, locations, and print options.
     */
    private void loadInitialData() {
        loadProviders();
        loadTimeslots();
        loadLocations();
        loadPrintOptions();
    }

    /**
     * Sets up the table columns for the locations table.
     */
    private void setupTableColumns() {
        cityColumn.setCellValueFactory(cellData -> {
            Location location = cellData.getValue();
            String name = Format.capitalize(location.name());
            return new SimpleStringProperty(name);  // Use enum name
        });
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("county"));
        zipColumn.setCellValueFactory(new PropertyValueFactory<>("zip"));
    }

    /**
     * Sets up the event handlers for the gui application. This includes
     * handling the load providers button, view button, schedule button,
     * reschedule button, cancel button, clear button, appointment date,
     * current timeslot menu, and new timeslot menu.
     */
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
        viewButton.setOnAction(e -> handlePrint());
        scheduleButton.setOnAction(e -> scheduleAppointment());
        rescheduleButton.setOnAction(e -> handleRescheduleRequest());
        cancelButton.setOnAction(e -> cancelAppointment());
        clearButton.setOnAction(e -> clearForm());
        appointmentDate.setOnAction(e -> updateAvailableTimeslots());
        currentTimeslotMenu.setOnAction(e -> handleRescheduleRequest());
        newTimeslotMenu.setOnAction(e -> handleNewTimeslotSelection());
    }

    /**
     * Loads the providers for the clinic manager application into the
     * provider menu. This includes loading the providers for an office visit
     * or an imaging service.
     */
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

    /**
     * Handles the print command for the clinic manager application. This
     * includes printing the appointments, appointments by patient, appointments
     * by location, statements, imaging appointments, credits by provider, and
     * office appointments.
     */
    private void handlePrint() {
        viewOutputArea.clear();
        String printCommand = viewPrintMenu.getId();
        String text = viewPrintMenu.getText();
        if (printCommand == null || text.equalsIgnoreCase("Print by")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Print Selection");
            alert.setHeaderText("Please select a print option");
            alert.setContentText("Please select a print option from the dropdown menu.");
            alert.showAndWait();
            return;
        }

        String out = clinicManager.getCommandRegistry().executeCommand(printCommand, new String[0]);
        viewOutputArea.setText(out);
    }

    /**
     * Loads the print options for the clinic manager application into the
     * view print menu.
     */
    private void loadPrintOptions() {
        viewPrintMenu.getItems().clear();
        MenuItem appointments = new MenuItem("Appointments");
        appointments.setOnAction(e -> {
            viewPrintMenu.setText("Appointments");
            viewPrintMenu.setId("PA");
        });
        MenuItem patients = new MenuItem("Appointments By Patient");
        patients.setOnAction(e -> {
            viewPrintMenu.setText("Appointments By Patient");
            viewPrintMenu.setId("PP");
        });
        MenuItem locations = new MenuItem("Appointments by Location");
        locations.setOnAction(e -> {
            viewPrintMenu.setText("Appointments by Location");
            viewPrintMenu.setId("PL");
        });
        MenuItem statements = new MenuItem("Statements");
        statements.setOnAction(e -> {
            viewPrintMenu.setText("Statements");
            viewPrintMenu.setId("PS");
        });
        MenuItem imaging = new MenuItem("Imaging Appointments");
        imaging.setOnAction(e -> {
            viewPrintMenu.setText("Imaging Appointments");
            viewPrintMenu.setId("PI");
        });
        MenuItem credits = new MenuItem("Credits by Provider");
        credits.setOnAction(e -> {
            viewPrintMenu.setText("Credits by Provider");
            viewPrintMenu.setId("PC");
        });
        MenuItem office = new MenuItem("Office Appointments");
        office.setOnAction(e -> {
            viewPrintMenu.setText("Office Appointments");
            viewPrintMenu.setId("PO");
        });
        viewPrintMenu.getItems().addAll(appointments, patients, locations, statements, imaging, credits, office);
    }

    /**
     * Loads the timeslots for the clinic manager application into the
     * timeslot menu. This includes loading the timeslots for the current
     * timeslot and the new timeslot.
     */
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

    /**
     * Loads the locations for the clinic manager application into the
     * location table.
     */
    private void loadLocations() {
        for (Location location : Location.values()) {
            locationTable.getItems().add(location);
        }
    }

    /**
     * Schedules an appointment for the clinic manager application. This
     * includes scheduling an office visit or an imaging service.
     */
    private void scheduleAppointment() {
        if (!validateAppointmentInput()) {
            showValidationError("schedule");
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

    /**
     * Cancels an appointment for the clinic manager application.
     * This includes cancelling an office visit or an imaging service.
     * The appointment is cancelled based on the patient's first name,
     * last name, date of birth, appointment date, and timeslot.
     */
    private void cancelAppointment() {
        if (!validateAppointmentInput()) {
            showValidationError("cancel");
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

    /**
     * Handles the reschedule request for the clinic manager application.
     * This includes rescheduling an appointment based on the current
     * timeslot, new timeslot, patient's first name, last name, date of birth,
     * and reschedule date.
     */
    private void handleRescheduleRequest() {
        if (!validateRescheduleInput()) {
            showValidationError("reschedule");
            return;
        }

        String[] args = {
            Format.ukToUsDate(rescheduleDate.getValue().toString(), "-", "/"),
            currentTimeslotMenu.getId(),
            rescheduleFirstName.getText(),
            rescheduleLastName.getText(),
            Format.ukToUsDate(rescheduleDob.getValue().toString(), "-", "/"),
            newTimeslotMenu.getId()
        };

        final String rescheduleCommand = "R";
        String out = clinicManager.getCommandRegistry().executeCommand(rescheduleCommand, args);
        rescheduleOutputArea.appendText(out);
    }

    /**
     * Handles the new timeslot selection for the clinic manager application.
     * This includes updating the new timeslot menu text.
     */
    private void handleNewTimeslotSelection() {
        newTimeslotMenu.setText(newTimeslotMenu.getText());
    }

    /**
     * Validates the appointment input for the clinic manager application.
     * This includes validating the patient's first name, last name, date of birth,
     * appointment date, timeslot, and provider.
     *
     * @return if gui input is valid
     */
    private boolean validateAppointmentInput() {
        return !firstName.getText().isEmpty()
                && !lastName.getText().isEmpty()
                && !timeslotMenu.getText().equalsIgnoreCase("Select Timeslot")
                && !providerMenu.getText().equalsIgnoreCase("Select Provider")
                && dateOfBirth.getValue() != null
                && appointmentDate.getValue() != null
                && (officeVisitRadio.isSelected() || imagingServiceRadio.isSelected());
    }

    /**
     * Validates the reschedule input for the clinic manager application.
     * This includes validating the patient's first name, last name, date of birth,
     * reschedule date, current timeslot, and new timeslot.
     *
     * @return if gui input for reschedule is valid
     */
    private boolean validateRescheduleInput() {
        return !rescheduleFirstName.getText().isEmpty()
                && !rescheduleLastName.getText().isEmpty()
                && !currentTimeslotMenu.getText().equalsIgnoreCase("Timeslot")
                && !newTimeslotMenu.getText().equalsIgnoreCase("Timeslot")
                && rescheduleDob.getValue() != null
                && rescheduleDate.getValue() != null
                && currentTimeslotMenu.getId() != null
                && rescheduleDob.getValue() != null
                && currentTimeslotMenu.getId() != null
                && newTimeslotMenu.getId() != null;
    }

    /**
     * Clears the form for the clinic manager application.
     */
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

    /**
     * Updates the available timeslots for the clinic manager application.
     */
    private void updateAvailableTimeslots() {
        timeslotMenu.getItems().clear();
        loadTimeslots();
    }

    /**
     * Shows a validation error for the clinic manager application.
     *
     * @param action the action to show validation error for
     */
    private void showValidationError(String action) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Please check your input");
        alert.setContentText(
                String.format("All fields are required to %s an appointment.", action)
        );
        alert.showAndWait();
    }
}