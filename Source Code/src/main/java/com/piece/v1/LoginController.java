package com.piece.v1;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;


public class LoginController {

    @FXML
    private final HomeController home = new HomeController();
    @FXML
    private final NewUserController newUser = new NewUserController();
    @FXML
    private final Utilities u = new Utilities();
    @FXML
    public Button loginB;
    @FXML
    public CheckBox showPwd;
    @FXML
    public TextField visiblePwd;
    @FXML
    public Button newUserB;
    @FXML
    public TextField uid;
    @FXML
    public PasswordField pwd;

    @FXML
    public void loginSystem() throws Exception {

        int i = 0;
        int flag = 0;

        ResultSet rs = Utilities.connection.createStatement().executeQuery("SELECT * FROM users");
        while (rs.next()) {
            u.users.add(rs.getString(1));
            u.passwords.add(rs.getString(3));
            if (uid.getText().equals(u.users.get(i))) {
                System.out.println("User found @ row" + (i + 1));
                Utilities.Name = rs.getString(2);
                Utilities.userId = rs.getInt(1);
                System.out.println("User ID: " + Utilities.userId);
                System.out.println("User Name is: " + Utilities.Name);
                if (pwd.getText().equals(u.passwords.get(i))) {
                    flag = 1;
                    Utilities.stage1.close();
                    home.homePage();
                    System.out.println("Login Successful!");
                    break;
                } else {
                    System.out.println("Incorrect Password!");
                    System.out.println("Login Unsuccessful!");
                    break;
                }
            } else {
                System.out.println("User not found @ row" + (i + 1));
                flag = 0;
            }
            i++;
        }
        rs.close();

        if (flag == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Login Unsuccessful");
            alert.setContentText("Incorrect User ID or Password");
            alert.show();

        }

    } //Takes the input from user and compares it with the database for logging the user in.

    @FXML
    public void showPwd() {
        if (showPwd.isSelected()) {
            visiblePwd.textProperty().bindBidirectional(pwd.textProperty());
            visiblePwd.toFront();
        } else {
            pwd.textProperty().bindBidirectional(visiblePwd.textProperty());
            pwd.toFront();
        }
    } //Shows User Password

    @FXML
    public void newUserPage() throws Exception {
        newUser.newUserPage();
    }
}
