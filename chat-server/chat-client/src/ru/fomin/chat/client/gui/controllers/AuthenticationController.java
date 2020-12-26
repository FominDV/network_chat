package ru.fomin.chat.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;


public class AuthenticationController {
    static String ip="127.0.0.1";
    static int port=8189;
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
    private Button btnTCP_IP;

    @FXML
    void initialize() {
        btn_login.setOnAction(event -> {
            System.out.println("login");

        });
        btnTCP_IP.setOnAction(event -> showAndHideStages("/ru/fomin/chat/client/gui/fxml/connection_properties.fxml", btnTCP_IP));
        btn_info.setOnAction(event -> showDeveloperInfo());
    }
}