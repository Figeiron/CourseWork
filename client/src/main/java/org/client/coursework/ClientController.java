package org.client.coursework;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

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

    private ChatClient webSocketClient;

    @FXML
    public void initialize() {
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
                chatBox.appendText("Invalid port number.\n");
            }
        } else {
            chatBox.appendText("Please enter both IP address and port.\n");
        }
    }

    private void connectToServer(String url) {
        chatBox.appendText("Connecting to: " + url + "\n");
        webSocketClient = new ChatClient(chatBox, url);
        webSocketClient.connect();
    }

    @FXML
    public void handleSendButtonAction() {
        String message = chatMessage.getText();
        if (message != null && !message.isEmpty()) {
            System.out.println(message);
            webSocketClient.sendMessage(message);
            chatMessage.clear();
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
