package ru.fomin.chat.client.gui.controllers;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.swing.*;

public class RegistrationController {

    @FXML
    private Button btn_info;

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_registration;

    @FXML
    private TextField field_login;

    @FXML
    private TextField field_nickname;

    @FXML
    private TextField field_password;

    @FXML
    private TextField field_repeat_password;
    @FXML
    void initialize() {
AuthenticationController.registrationController=this;

        btn_info.setOnAction(event -> showDeveloperInfo());
        btn_cancel.setOnAction(event -> exit());
    }
    public void exit() {
            AuthenticationController.socketTreadStop();
    }
    public void returnToAuthentication(){
        showAndHideStages("/ru/fomin/chat/client/gui/fxml/authentication.fxml", btn_cancel);
    }


}

