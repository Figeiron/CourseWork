package org.client.coursework;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;

public class ChatClient extends WebSocketClient {

    public int errors = 0;
    private TextArea chatBox;

    public ChatClient(TextArea chatBox, String url) {
        super(URI.create(url));
        this.chatBox = chatBox;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        Platform.runLater(() -> chatBox.appendText("Connected to server\n"));
    }

    @Override
    public void onMessage(String message) {
        Platform.runLater(() -> chatBox.appendText("Server: " + message + "\n"));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Platform.runLater(() -> chatBox.appendText("Disconnected from server\n"));
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        Platform.runLater(() -> chatBox.appendText("Error: " + ex.getMessage() + "\n"));
        errors ++;
    }

    public void sendMessage(String message) {
        if (this.isOpen()) {
            send(message);
            Platform.runLater(() -> chatBox.appendText("You: " + message + "\n"));
        }
    }
}
