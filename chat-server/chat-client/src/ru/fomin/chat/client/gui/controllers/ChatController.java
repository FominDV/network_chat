package ru.fomin.chat.client.gui.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Arrays;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;
public class ChatController {
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

    public void setUsersList(String[] usersArray) {
        ObservableList<String> observableList= FXCollections.observableArrayList(usersArray);
        users_list.setItems(observableList);
        multipleSelectionModel=users_list.getSelectionModel();
        if(multipleSelectionModel.getSelectedItem()==null) multipleSelectionModel.select(0);
    }
}


