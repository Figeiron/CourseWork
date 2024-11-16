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
        closeButton.setDisable(true);
        connectButton.setDisable(false);
        chatMessage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode().toString().equals("ENTER") && !event.isControlDown()) {
                event.consume();
                handleSendButtonAction();
            } else if (event.getCode().toString().equals("ENTER") && event.isControlDown()) {
                chatMessage.appendText("\n");
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
                chatBox.appendText("Invalid port number.\n");
            }
        } else {
            chatBox.appendText("Please enter both IP address and port.\n");
        }
    }

    private void connectToServer(String url) {
        chatBox.appendText("Connecting to: " + url + "\n");
        webSocketClient = new ChatClient(chatBox,connectButton,closeButton, url);
        webSocketClient.connect();
        closeButton.setDisable(false);
        connectButton.setDisable(true);
    }

    @FXML
    public void handleSendButtonAction() {
        String message = chatMessage.getText();
        if (message != null && !message.isEmpty() && webSocketClient != null) {
            if(webSocketClient.isOpen()){
                try {
                    webSocketClient.sendMessage(message.trim());
                } catch (Exception e) {
                    chatBox.appendText("error: " + e.getMessage());
                }
                chatMessage.clear();
            }
        }
    }



    public void closeConnection() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
    }
}
