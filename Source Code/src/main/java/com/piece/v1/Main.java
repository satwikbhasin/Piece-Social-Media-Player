package com.piece.v1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.sql.SQLException;


public class Main extends Application {
    public static void main(String[] args) throws SQLException {
        try {
            launch(args);
        } finally {
            Utilities.connection.close();                             //Closes the database connection at the time when application closes
            System.out.println("Database Connection Closed!");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Utilities.mysqlConnection();
        Utilities.stage1 = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        primaryStage.setTitle("Piece");
        Utilities.stage(root, primaryStage, 500, 300);
    }

}