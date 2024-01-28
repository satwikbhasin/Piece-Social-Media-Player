package com.piece.v1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;


public class BaseController implements Initializable {

    @FXML
    public Button logoutB;
    @FXML
    public Button homeB;
    @FXML
    public Button aboutB;
    @FXML
    public Button chatB;
    @FXML
    public Label welcomeLabel;

    @FXML
    public void homeButton() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("Home.fxml"));
        Utilities.stage(root, Utilities.stage2, 1280, 800);
        System.out.println("Home screen invoked!");
    } //Takes user back to the HOME screen and refreshes the home screen.

    public void chatButton() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("Chat.fxml"));
        Utilities.stage(root, Utilities.stage2, 1280, 800);
        System.out.println("Chat screen invoked!");
    } //Takes user to the ABOUT US screen.

    public void aboutButton() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("About.fxml"));
        Utilities.stage(root, Utilities.stage2, 1280, 800);
        System.out.println("About screen invoked!");
    } //Takes user to the ABOUT screen.

    public void logoutButton() throws Exception {
        for (Integer musicId : MusicPlayerController.mediaPlayersMap.keySet()) {
            MusicPlayerController.mediaPlayersMap.get(musicId).stop();
            MusicPlayerController.mediaPlayersMap.get(musicId).dispose();
        }
        MusicPlayerController.mediaPlayersMap.clear();
        Utilities.stage2.close();
        Utilities.stage1.close();
        System.out.println("User Logged out");
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Utilities.stage1.setTitle("Piece");
        Utilities.stage(root, Utilities.stage1, 500, 300);
    } //Logs user out by changing screen from HOME to LOGIN.

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome to Piece, " + Utilities.Name);
    }
}
