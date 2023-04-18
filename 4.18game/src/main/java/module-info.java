module com.example.game233 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.game233 to javafx.fxml;
    exports com.example.game233;
}