package org.server.coursework;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerController {

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private TextArea logArea;

    @FXML
    private TextArea statusLabel;

    private ChatServer chatServer;

    @FXML
    public void initialize() {
        statusLabel.setText("Server stopped");
        stopButton.setDisable(true);
    }

    @FXML
    protected void onStartButtonClick() throws UnknownHostException {
        chatServer = new ChatServer(8080, this::logMessage);
        InetAddress inetAddress = InetAddress.getLocalHost();
        new Thread(chatServer::start).start();
        statusLabel.setText("Server running on " + inetAddress.getHostAddress() +":8080");
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
