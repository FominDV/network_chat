package ru.fomin.chat.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;
public class ChatController {

    @FXML
    private Button btn_info;

    @FXML
    private Button btn_cancel;

    @FXML
    private Label title;

    @FXML
    private TextArea log;

    @FXML
    void initialize() {
        AuthenticationController.handler.setChatController(this);

        btn_info.setOnAction(event -> showDeveloperInfo());
        btn_cancel.setOnAction(event -> exit());
    }

    public void hide() {
        btn_info.getScene().getWindow().hide();
    }
    public void setTitle(String nickname) {
        title.setText(title.getText()+nickname);
    }
    private void exit(){
        AuthenticationController.handler.stopSocketThread();
    }

    public void appendToLog(String message){
        log.appendText(message);
    }
}


