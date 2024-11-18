package org.server.coursework;

import com.fasterxml.jackson.core.JsonProcessingException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class ServerMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerMain.class.getResource("server_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 1);
        stage.setTitle("Chat Server");
        stage.setScene(scene);
        stage.setMinHeight(400);
        stage.setMinWidth(270);
        stage.show();
        DataBaseController dbController = new DataBaseController();
        dbController.connect();
        dbController.createTable();
        dbController.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
