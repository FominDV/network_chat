package ru.fomin.chat.client.gui.controllers;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;
import ru.fomin.chat.client.core.Handler;
import ru.fomin.chat.common.Library;

import javax.swing.*;
import java.io.File;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;
public class ChangeNicknameController {
private String newNickname;
    private static final String MESSAGE_SUCCESSFULLY_CHANGING_NICKNAME = "Nickname was changed\nFor finishing this process you should reconnect!\nNew nickname: ";
    @FXML
    private Button btn_info;

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_change;

    @FXML
    private TextField field_nickname;
    @FXML
    void initialize() {
        Handler.changeNicknameController=this;
        btn_info.setOnAction(event -> showDeveloperInfo());
        btn_cancel.setOnAction(event -> exit());
        btn_change.setOnAction(event -> changeNickname());
    }

    private void changeNickname() {
        newNickname=field_nickname.getText();
        if(isValidNickname(newNickname)) AuthenticationController.handler.sendMessage(Library.getChangingNicknameMessage(newNickname)); else field_nickname.setText("");
    }

    private boolean isValidNickname(String newNickname) {
        if(newNickname.equals("")){
            showErrorMessage("Fill the field, please!");
            return false;
        }
        if (newNickname.contains(" ")) {
            showErrorMessage("Nickname should not contain spaces");
            return false;
        }
        if(newNickname.matches(".*?[<>\\\\\\/\\*\\?\\|\"\\:].*?")){
            showErrorMessage("Nickname should not contain symbols:\n<, >, \\,/,*,|,?,\",:");
            return false;
        }
        if (newNickname.length() > RegistrationController.maxNicknameLength) {
            showErrorMessage("Length of nickname should not be greater than " + RegistrationController.maxNicknameLength);
            return false;
        }
        return true;
    }

    public void exit(){
        ChatController.isChangingNicknameOpened=false;
        btn_change.getScene().getWindow().hide();
    }

    public void changingSuccessful() {
        rewriteHistoryBuNewNickname();
        Handler.setNickName(newNickname);
        showInfoMessage(MESSAGE_SUCCESSFULLY_CHANGING_NICKNAME+newNickname);
        exit();
    }

    private void rewriteHistoryBuNewNickname() {
        File history=new File(Handler.getFilePath());
        history.renameTo(new File(Handler.getFilePath(newNickname)));
    }
}

