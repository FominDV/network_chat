package ru.fomin.chat.client.authentication;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ru.fomin.chat.client.CommonController.showDeveloperInfo;


public class AuthenticationController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private Button btn_info;

    @FXML
    private URL location;

    @FXML
    private TextField field_login;

    @FXML
    private Button btn_login;

    @FXML
    private Button btn_registration;

    @FXML
    private PasswordField field_password;

    @FXML
    void initialize() {
        btn_login.setOnAction(event -> {
                    System.out.println("login");

                });
btn_info.setOnAction(event -> showDeveloperInfo());
    }
}