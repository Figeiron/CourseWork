package org.client.coursework;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import javafx.application.Platform;

public class ClientController {

    @FXML
    private TextArea chatBox;

    private ChatClient webSocketClient;

    public void initialize() {
        webSocketClient = new ChatClient(chatBox);
        webSocketClient.connect();  // Підключення до сервера
    }

    @FXML
    public void handleSendButtonAction() {
        String message = chatBox.getText();
        if (message != null && !message.isEmpty()) {
            webSocketClient.sendMessage(message);
            chatBox.clear();
        }
    }

    @FXML
    public void handleKeyPress(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            handleSendButtonAction();
        }
    }

    public void closeConnection() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}
