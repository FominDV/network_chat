package ru.fomin.chat.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;


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
    private Button btnTCP_IP;

    @FXML
    void initialize() {
        btn_login.setOnAction(event -> {
            System.out.println("login");

        });
        btnTCP_IP.setOnAction(event -> {
            btnTCP_IP.getScene().getWindow().hide();
           showStage("/ru/fomin/chat/client/gui/fxml/connection_properties.fxml");
        });
        btn_info.setOnAction(event -> showDeveloperInfo());
    }
}