package com.example.project3;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.security.Provider;
import java.util.ResourceBundle;

public class ClinicManagerController implements Initializable {
    @FXML

    private ComboBox<Provider> cmb_provider;

    @FXML
    private Label myLabel;

    @FXML
    private ChoiceBox <String> myTimeslotChoice;
    //data to populate the choice box so our timeslots
    private String[] time = {"9:00 AM", "10:00 AM", "11:00 AM", "12:00 PM", "1:00 PM", "2:00 PM", "3:00 PM", "4:00 PM"};

//}
//    //she said so the backend can communicate with the frontend or something like that
//    @Override
//    public void initialize(URl arg0, ResourceBundle arg1){
//        myTimeslotChoice.getItems().addAll(time);
//        // public void initialize() {
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myTimeslotChoice.getItems().addAll(time);
    }
//        ObservableList<Provider> providers = FXCollections.observableArrayList(Provider.values());
//        cmb_provider.setItems(providers);
//        cmb_provider.getSelectionModel().getSelectedItem();
//    }
}
