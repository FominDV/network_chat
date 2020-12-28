package ru.fomin.chat.client.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.fomin.chat.client.core.Handler;

import java.net.URL;
import java.util.ResourceBundle;

import static ru.fomin.chat.common.Library.*;
import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;


public class AuthenticationController {
   public static RegistrationController registrationController;
    private boolean isConnected = false;
    static Handler handler;
    static String ip = "127.0.0.1";
    static int port = 8189;

    static void setConnectionProperties(String ip, int port) {
        AuthenticationController.ip = ip;
        AuthenticationController.port = port;
    }

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
            connect();

        });
        btn_registration.setOnAction(event -> {
            connect();
            if (isConnected)
                showAndHideStages("/ru/fomin/chat/client/gui/fxml/registration.fxml", btnTCP_IP);
        });
        btn_login.setOnAction(event -> {
            connect();
            String login = field_login.getText(), password = field_password.getText();
            if (isConnected && isValidField(login, password)) {
                showAndHideStages("/ru/fomin/chat/client/gui/fxml/chat.fxml", btn_login);
                handler.sendMessage(getAuthRequest(login, password));
            }
        });
        btnTCP_IP.setOnAction(event -> showAndHideStages("/ru/fomin/chat/client/gui/fxml/connection_properties.fxml", btnTCP_IP));
        btn_info.setOnAction(event -> showDeveloperInfo());
    }

    private boolean isValidField(String login, String password) {
        if (login.equals("") || password.equals("")) {
            showErrorMessage("All fields should be filled");
            return false;
        } else return true;
    }

    private void connect() {
        if (!isConnected) {
            isConnected = true;
            handler = new Handler(port, ip, this);
        }
    }

    public void changeIsConnected() {
        if (isConnected) isConnected = false;
        else isConnected = true;
    }

    public void reload() {
        try {
            registrationController.returnToAuthentication();
        } catch (NullPointerException e) {
            showStage("/ru/fomin/chat/client/gui/fxml/authentication.fxml");
        }

    }

    static void socketTreadStop() {
        handler.stopSocketThread();
    }

}