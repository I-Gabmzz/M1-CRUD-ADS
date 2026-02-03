module com.example.Meta1ADS {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mariadb.jdbc;

    opens Elementos to javafx.base;

    opens Interfaz to javafx.fxml;
    exports Main;
}
