package org.client.coursework;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import kotlin.random.PlatformRandomKt;

public class ClientController {

    @FXML
    private TextField ipField;

    @FXML
    private TextField portField;

    @FXML
    private TextArea chatBox;

    @FXML
    private TextArea chatMessage;

    @FXML
    private Button closeButton;

    @FXML
    private Button connectButton;

    @FXML
    private Button usernameButton;

    @FXML
    private TextField usernameField;

    private ChatClient webSocketClient;

    @FXML
    public void initialize() {
        Platform.runLater(() -> {
            closeButton.setDisable(true);
            connectButton.setDisable(false);
            usernameButton.setDisable(true);
        });
        chatMessage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().toString().equals("ENTER") && !event.isControlDown()) {
                event.consume();
                handleSendButtonAction();
            } else if (event.getCode().toString().equals("ENTER") && event.isControlDown()) {
                Platform.runLater(() -> chatMessage.appendText("\n"));
            }
        });
    }

    @FXML
    public void handleConnectButtonAction() {
        String ipAddress = ipField.getText();
        String port = portField.getText();

        if (ipAddress != null && !ipAddress.isEmpty() && port != null && !port.isEmpty()) {
            try {
                int portNumber = Integer.parseInt(port);
                String url = "ws://" + ipAddress + ":" + portNumber;
                connectToServer(url);
            } catch (NumberFormatException e) {
                Platform.runLater(() -> chatBox.appendText("Invalid port number.\n"));
            }
        } else {
            Platform.runLater(() -> chatBox.appendText("Please enter both IP address and port.\n"));
        }
    }

    private void connectToServer(String url) {
        webSocketClient = new ChatClient(chatBox, connectButton, closeButton, usernameButton, usernameField, url);
        webSocketClient.connect();
        Platform.runLater(() -> {
            chatBox.appendText("Connecting to: " + url + "\n");
            closeButton.setDisable(false);
            connectButton.setDisable(true);
            usernameButton.setDisable(false);
        });
    }

    @FXML
    public void handleSendButtonAction() {
        String message = chatMessage.getText();
        if (message != null && !message.isEmpty() && webSocketClient != null) {
            if (webSocketClient.isOpen()) {
                try {
                    webSocketClient.sendMessage(message.trim());
                } catch (Exception e) {
                    Platform.runLater(()->chatBox.appendText("error: " + e.getMessage()));
                }
                Platform.runLater(()->chatMessage.clear());
            }
        }
    }


    @FXML
    public void handleSendUsernameButtonAction() {
        String username = usernameField.getText();
        if (username != null && !username.isEmpty() && webSocketClient != null) {
            if (webSocketClient.isOpen()) {
                try {
                    webSocketClient.sendServiceMessage(username.trim());
                } catch (Exception e) {
                    Platform.runLater(()->chatBox.appendText("error: " + e.getMessage()));
                }
                Platform.runLater(()->chatMessage.clear());
            }
        }
    }


    public void closeConnection() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}
