module com.piece.v1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;
    requires java.sql;
    requires lombok;

    opens com.piece.v1 to javafx.fxml;
    exports com.piece.v1;
}