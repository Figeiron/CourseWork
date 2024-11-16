package org.server.coursework;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;

public class ServerController {

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private TextArea logArea;

    @FXML
    private Label statusLabel;

    private ChatServer chatServer;

    @FXML
    public void initialize() {
        statusLabel.setText("Server stopped");
        stopButton.setDisable(true);
    }

    @FXML
    protected void onStartButtonClick() {
        chatServer = new ChatServer(8080, this::logMessage);
        new Thread(chatServer::start).start();
        statusLabel.setText("Server running on port 8080");
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    @FXML
    protected void onStopButtonClick() {
        if (chatServer != null) {
            chatServer.stopServer();
            statusLabel.setText("Server stopped");
            startButton.setDisable(false);
            stopButton.setDisable(true);
        }
    }

    public void logMessage(String message) {
        Platform.runLater(() -> logArea.appendText(message + "\n"));
    }
}
