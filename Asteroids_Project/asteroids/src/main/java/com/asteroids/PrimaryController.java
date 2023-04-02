package com.asteroids;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        Asteroids.setRoot("secondary");
    }
}
