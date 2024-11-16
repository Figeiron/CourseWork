package org.client.coursework;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("client_view.fxml"));
        ClientKeyController ClientKeyController = new ClientKeyController(stage);

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        scene.setOnKeyPressed(ClientKeyController);

        stage.setTitle("Chat!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {launch(args);}
}
