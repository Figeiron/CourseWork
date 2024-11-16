package org.client.coursework;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ChatClient extends WebSocketClient {

    private TextArea chatBox;

    private Button closeButton;

    private Button connectButton;


    public ChatClient(TextArea chatBox, Button connectButton, Button closeButton, String url) {
        super(URI.create(url));
        this.chatBox = chatBox;
        this.closeButton = closeButton;
        this.connectButton = connectButton;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        Platform.runLater(() -> chatBox.appendText("Connected to server\n"));
        closeButton.setDisable(false);
        connectButton.setDisable(true);
    }

    @Override
    public void onMessage(String message) {
        Platform.runLater(() -> chatBox.appendText(message + "\n"));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Platform.runLater(() -> chatBox.appendText("Disconnected from server\n"));
        closeButton.setDisable(true);
        connectButton.setDisable(false);
    }

    @Override
    public void onError(Exception ex) {
        Platform.runLater(() -> chatBox.appendText("Error: " + ex.getMessage() + "\n"));
        closeButton.setDisable(true);
        connectButton.setDisable(false);
    }

    public void sendMessage(String message) {
        if (this.isOpen()) {
            send(message);
            Platform.runLater(() -> chatBox.appendText("You: " + message + "\n"));
        }
    }
}
