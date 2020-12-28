package ru.fomin.chat.client.gui.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import ru.fomin.chat.client.core.Handler;

import javax.swing.*;
import java.util.Arrays;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;
import static ru.fomin.chat.common.Library.getTypeClientBcast;
import static ru.fomin.chat.common.Library.getTypeClientPrivate;

public class ChatController {
    static boolean isChangingNicknameOpened = false;
    private MultipleSelectionModel<String> multipleSelectionModel;
    @FXML
    private Label title;

    @FXML
    private Button btn_info;

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_send;

    @FXML
    private TextField field_message;

    @FXML
    private CheckBox cb_private;

    @FXML
    private ListView<String> users_list;

    @FXML
    private TextArea log;

    @FXML
    private Button btn_change_password;

    @FXML
    private Button btn_change_nickname;

    @FXML
    void initialize() {
        AuthenticationController.handler.setChatController(this);
        btn_info.setOnAction(event -> showDeveloperInfo());
        btn_cancel.setOnAction(event -> exit());
        btn_send.setOnAction(event -> sendMessage());
        field_message.setOnAction(event -> sendMessage());
        btn_change_nickname.setOnAction(event -> {
            if (!isChangingNicknameOpened) {
                isChangingNicknameOpened = true;
                showStage("/ru/fomin/chat/client/gui/fxml/change_nickname.fxml");
            }
        });
    }

    private void sendMessage() {
        String msg = field_message.getText();
        field_message.setText("");
        field_message.requestFocus();
        if (msg.matches("\\s*")) {
            return;
        }
        if (cb_private.isSelected()) {
            if (!(multipleSelectionModel.getSelectedItem() == null)) {
                AuthenticationController.handler.sendMessage(getTypeClientPrivate(multipleSelectionModel.getSelectedItem(), msg));
            } else {
                appendToLog("ERROR of sending private message");
                cb_private.setSelected(false);
            }
        } else {
            AuthenticationController.handler.sendMessage(getTypeClientBcast(msg));
        }
    }

    public void hide() {
        btn_info.getScene().getWindow().hide();
    }

    public void setTitle(String nickname) {
        title.setText(title.getText() + nickname);
    }

    private void exit() {
        AuthenticationController.handler.stopSocketThread();
    }

    public void appendToLog(String message) {
        log.appendText(message+"\n");
        Handler.writeMessageToHistory(message);
    }

    public void setHistoryToLog(String history) {
      log.appendText(history);
        log.positionCaret(log.getLength());
    }

    public void setUsersList(String[] usersArray) {
        ObservableList<String> observableList = FXCollections.observableArrayList(usersArray);
        users_list.setItems(observableList);
        multipleSelectionModel = users_list.getSelectionModel();
        if (multipleSelectionModel.getSelectedItem() == null) multipleSelectionModel.select(0);
    }
}


