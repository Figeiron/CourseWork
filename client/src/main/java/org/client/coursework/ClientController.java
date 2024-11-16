package org.client.coursework;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClientController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to client");
    }
}
