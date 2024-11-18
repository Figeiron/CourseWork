package org.client.coursework;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Platform;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

public class ChatClient extends WebSocketClient {

    private final TextArea chatBox;

    private final TextField usernameField;

    private final Button closeButton;

    private final Button connectButton;

    private final Button usernameButton;


    public ChatClient(TextArea chatBox, Button connectButton, Button closeButton, Button usernameButton, TextField usernameField, String url) {
        super(URI.create(url));
        this.chatBox = chatBox;
        this.usernameField = usernameField;
        this.closeButton = closeButton;
        this.connectButton = connectButton;
        this.usernameButton = usernameButton;
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        Platform.runLater(() -> {
            chatBox.appendText("Connected to server\n");
            closeButton.setDisable(false);
            connectButton.setDisable(true);
            usernameButton.setDisable(false);
        });

    }

    @Override
    public void onMessage(String json_message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(json_message);
            if (jsonNode.has("message")) {
                String message;
                String sender;
                jsonNode = jsonNode.path("message");
                message = jsonNode.get("client_message").asText();
                sender = jsonNode.get("sender").asText();
                Platform.runLater(() -> chatBox.appendText(sender + " -> " + message + "\n"));
            }else if (jsonNode.has("service_message")){
                String username = jsonNode.get("service_message").asText();
                Platform.runLater(()->usernameField.setText(username));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Platform.runLater(() -> {
            chatBox.appendText("Disconnected from server\n");
            closeButton.setDisable(true);
            connectButton.setDisable(false);
            usernameButton.setDisable(true);
            usernameField.setText("");
        });

    }

    @Override
    public void onError(Exception ex) {
        Platform.runLater(() -> {
            chatBox.appendText("Error: " + ex.getMessage() + "\n");
            closeButton.setDisable(true);
            connectButton.setDisable(false);
            usernameButton.setDisable(true);
            usernameField.setText("");
        });

    }

    public void sendMessage(String message) {
        if (this.isOpen()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(Map.of("message", message));
                send(jsonMessage);

                Platform.runLater(() -> chatBox.appendText("You: " + message + "\n"));
            } catch (Exception e) {
                Platform.runLater(() -> chatBox.appendText("Error sending message.\n"));
            }
        }
    }

    public void sendServiceMessage(String message) {
        if (this.isOpen()) {
            send("{\"service_message\": \"" + message + "\"}");
        }
    }

}
