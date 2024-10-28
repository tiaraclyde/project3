package org.cs213.clinic.jfx;
import org.cs213.clinic.core.ClinicManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.FileNotFoundException;
import java.net.URL;
import java.security.Provider;
import java.util.ResourceBundle;
//no direct access to database class go through clinic manager
public class ClinicManagerController implements Initializable {
    @FXML

    private ComboBox<Provider> cmb_provider;

    @FXML
    private Label myLabel;

    @FXML
    private ChoiceBox <String> myTimeslotChoice;

    private ClinicManager clinicManager;

//}
//    //she said so the backend can communicate with the frontend or something like that
//    @Override
//    public void initialize(URl arg0, ResourceBundle arg1){
//        myTimeslotChoice.getItems().addAll(time);
//        // public void initialize() {
//    }
//setvisible
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            clinicManager = new ClinicManager();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //myTimeslotChoice.getItems().addAll(timeslot);
    }
//        ObservableList<Provider> providers = FXCollections.observableArrayList(Provider.values());
//        cmb_provider.setItems(providers);
//        cmb_provider.getSelectionModel().getSelectedItem();
//    }
}
