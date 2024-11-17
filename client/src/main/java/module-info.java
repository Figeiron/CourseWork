module org.client.coursework {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires kotlin.stdlib;
    requires Java.WebSocket;
    requires com.fasterxml.jackson.databind;

    opens org.client.coursework to javafx.fxml;
    exports org.client.coursework;
}
