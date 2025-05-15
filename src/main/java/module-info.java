module com.example.gestionvoltunisair {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mariadb.jdbc;
    requires java.sql;

    // Export main package
    exports com.example.gestionvoltunisair;

    // Export controller package
    exports Controller;

    // Open packages for FXMLLoader
    opens com.example.gestionvoltunisair to javafx.fxml;
    opens Controller to javafx.fxml;

    // For property binding if needed
    opens Classes to javafx.base;
}