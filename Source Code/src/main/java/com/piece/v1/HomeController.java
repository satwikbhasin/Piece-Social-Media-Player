package com.piece.v1;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;

import java.util.Objects;

public class HomeController {
    public void homePage() throws Exception {
        //MusicPlayerController.musicId = 1;
        Parent root = FXMLLoader.load(Main.class.getResource("Home.fxml"));
        Utilities.stage(root, Utilities.stage2, 1280, 800);
        System.out.println("Home screen loaded");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);  //Shows a dialog box upon login which welcomes user.
        alert.setContentText(Utilities.Name);
        alert.setHeaderText("Welcome");
        alert.show();
        //MusicPlayerController.musicId = 1;
    }
}//Transition from LOGIN screen to HOME Screen.

