package ru.fomin.chat.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ChatController {

    @FXML
    private Button btn_info;

    @FXML
    private Button btn_cancel;

    @FXML
    private TextField field_port;

    @FXML
    private Button btn_change;

    @FXML
    private TextField field_ip;

    @FXML
    private Label label_port;

    @FXML
    private Label label_ip;

    @FXML
    void initialize() {
        AuthenticationController.handler.setChatController(this);
    }

    public void hide() {
        btn_info.getScene().getWindow().hide();
    }
}


