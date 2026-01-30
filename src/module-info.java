module com.example.Meta1ADS {
    requires javafx.controls;
    requires javafx.fxml;


    opens Interfaz to javafx.fxml;
    exports Main;
}