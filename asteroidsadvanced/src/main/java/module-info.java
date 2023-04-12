module com.example.asteroidsadvanced {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gamedemo to javafx.fxml;
    exports com.example.gamedemo;
}