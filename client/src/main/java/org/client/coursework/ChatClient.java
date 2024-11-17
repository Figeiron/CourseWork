package org.client.coursework;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class ChatClient extends WebSocketClient {

    private final TextArea chatBox;

    private final Button closeButton;

    private final Button connectButton;

    private final Button usernameButton;


    public ChatClient(TextArea chatBox, Button connectButton, Button closeButton,Button usernameButton, String url) {
        super(URI.create(url));
        this.chatBox = chatBox;
        this.closeButton = closeButton;
        this.connectButton = connectButton;
        this.usernameButton = usernameButton;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        Platform.runLater(() -> chatBox.appendText("Connected to server\n"));
        closeButton.setDisable(false);
        connectButton.setDisable(true);
        usernameButton.setDisable(false);
    }

    @Override
    public void onMessage(String json_message) {
        String message;
        String sender;
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("sdf   "+json_message);
        try {

            JsonNode jsonNode = objectMapper.readTree(json_message);
            message = jsonNode.get("message").asText();
            sender = jsonNode.get("sender").asText();
        } catch (JsonProcessingException e) {throw new RuntimeException(e);}

        Platform.runLater(() -> chatBox.appendText( sender + " -> "+ message + "\n"));
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Platform.runLater(() -> chatBox.appendText("Disconnected from server\n"));
        closeButton.setDisable(true);
        connectButton.setDisable(false);
        usernameButton.setDisable(true);
    }

    @Override
    public void onError(Exception ex) {
        Platform.runLater(() -> chatBox.appendText("Error: " + ex.getMessage() + "\n"));
        closeButton.setDisable(true);
        connectButton.setDisable(false);
        usernameButton.setDisable(true);
    }

    public void sendMessage(String message) {
        if (this.isOpen()) {
            send("{\"message\": \"" + message + "\"}");
            Platform.runLater(() -> chatBox.appendText("You: " + message + "\n"));
        }
    }
    public void sendServiceMessage(String message) {
        if (this.isOpen()) {
            send("{\"service_message\": \"" + message + "\"}");
        }
    }

}
