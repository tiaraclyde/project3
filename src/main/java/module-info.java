module org.cs213.clinic {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.cs213.clinic.jfx to javafx.fxml;
    exports org.cs213.clinic.jfx;
}