package org.client.coursework;

import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class ClientKeyController implements EventHandler<KeyEvent> {

    private final Stage stage;

    public ClientKeyController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().toString().equals("F11")) {
            stage.setFullScreen(!stage.isFullScreen());
        }
        if (event.getCode().toString().equals("ENTER")) {}
    }
}
