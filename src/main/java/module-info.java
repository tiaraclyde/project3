module org.cs213.clinic {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.cs213.clinic.jfx to javafx.fxml;  // Already have this
    opens org.cs213.clinic.core to javafx.base;  // Add this line

    exports org.cs213.clinic.jfx;
}