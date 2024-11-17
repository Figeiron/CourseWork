package org.server.coursework;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.HashSet;
import java.util.Set;

public class ChatServer extends WebSocketServer {
    private final Consumer<String> logger;

    private Set<WebSocket> clients = new HashSet<>();
    private DataBaseController dbController = new DataBaseController();

    public ChatServer(int port, Consumer<String> logger) {
        super(new InetSocketAddress(port));
        this.logger = logger;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        logger.accept("New connection: " + conn.getRemoteSocketAddress());
        clients.add(conn);
        dbController.connect();
        dbController.addUserByIp(conn.getRemoteSocketAddress().getAddress().toString());
        dbController.closeConnection();

    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.accept("Connection closed: " + conn.getRemoteSocketAddress());
        clients.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String jsonMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        String sender_ip = conn.getRemoteSocketAddress().getAddress().toString();
        try {
            dbController.connect();
            rootNode = objectMapper.readTree(jsonMessage);
            if (rootNode.has("message")) {
                String message = rootNode.path("message").asText();
                MessageBuilder json_message = new MessageBuilder(sender_ip, message);
                String sender_username = dbController.getUsernameByIp(sender_ip);
                if (!Objects.equals(sender_username, "none")){
                    json_message.setSender(sender_username);
                }
//              broadcast(json_message.toJson());
                broadcastExceptSender(conn, json_message.toJson());
                logger.accept("Message from: " + json_message.getSender() + " -> " + json_message.getMessage());
            } else if (rootNode.has("service_message")) {
                String username = rootNode.path("service_message").asText();
                dbController.updateUsernameByIp(sender_ip, username);
            }
            dbController.closeConnection();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private void broadcastExceptSender(WebSocket sender, String message) {
        for (WebSocket conn : clients) {
            if (!conn.equals(sender)) {
                conn.send(message);
            }
        }
    }


    @Override
    public void onError(WebSocket conn, Exception ex) {
        logger.accept("Error: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        logger.accept("Server started!");
    }

    public void stopServer() {
        try {
            this.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.accept("Server stopped.");
    }
}
