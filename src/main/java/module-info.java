module org.openjfx.clientfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.openjfx.clientfx to javafx.fxml;
    opens org.openjfx.client to javafx.fxml;
    opens org.openjfx.server.models to javafx.fxml;
    opens org.openjfx.server to javafx.fxml;

    exports org.openjfx.clientfx;
    exports org.openjfx.server.models;
    exports org.openjfx.server;
    exports org.openjfx.client;


}