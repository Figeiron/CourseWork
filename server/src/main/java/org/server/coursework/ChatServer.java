package org.server.coursework;

import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.function.Consumer;
import java.util.HashSet;
import java.util.Set;

public class ChatServer extends WebSocketServer {
    private final Consumer<String> logger;

    private Set<WebSocket> clients = new HashSet<>();

    public ChatServer(int port, Consumer<String> logger) {
        super(new InetSocketAddress(port));
        this.logger = logger;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        logger.accept("New connection: " + conn.getRemoteSocketAddress());
        clients.add(conn);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.accept("Connection closed: " + conn.getRemoteSocketAddress());
        clients.remove(conn);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        logger.accept("Message from (" + conn.getRemoteSocketAddress().getAddress() + "): " + message);
        String formatedMessage = conn.getRemoteSocketAddress().getAddress()+ ": " + message;
        broadcastExceptSender(conn, formatedMessage);
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
