package com.piece.v1;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.util.Objects;

public class NewUserController {

    @FXML
    private final Utilities u = new Utilities();
    @FXML
    private final BaseController home = new BaseController();
    @FXML
    private TextField newName;
    @FXML
    private TextField newUid;
    @FXML
    private TextField newPwd;
    @FXML
    private TextField confirmPwd;

    @FXML
    protected void newUserPage() throws Exception {
        Utilities.stage1.close();
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("NewUser.fxml")));
        Utilities.stage(root, Utilities.stage1, 500, 300);
        System.out.println("New User page opened");

    }

    @FXML
    private void createUser() throws Exception {
        int i = 0;
        int flag = 0;

        ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT * FROM USERS");

        if (newName.getText().isEmpty() || newUid.getText().isEmpty() || newPwd.getText().isEmpty() || confirmPwd.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Some fields are missing!");
            alert.show();
            flag = 1;
        }

        if (flag != 1) {
            while (rs.next()) {
                u.users.add(rs.getString(1));
                if (newUid.getText().equals(u.users.get(i))) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setHeaderText("User ID already exists, Try a new one");
                    alert.show();
                    flag = 1;
                    break;
                }
                i++;
            }
            rs.close();
        }

        if (flag != 1) {
            if (newPwd.getText().equals(confirmPwd.getText())) {
                String query = "INSERT INTO USERS(user_name,user_id,password) VALUES('" + newName.getText() + "','" + newUid.getText() + "','" + newPwd.getText() + "')";
                Utilities.connection.createStatement().executeUpdate(query);
                Alert created = new Alert(Alert.AlertType.INFORMATION);
                created.setHeaderText("User Created");
                System.out.println("New User Created");
                created.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Passwords do not match");
                alert.show();
            }
        }
    }

    @FXML
    private void loginScreen() throws Exception {
        home.logoutButton();
    }
}

