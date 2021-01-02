package ru.fomin.chat.client.gui.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.swing.*;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;

public class ConnectionPropertiesController {

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
    private Label label_ip;

    @FXML
    private Label label_port;

    @FXML
    void initialize() {
        label_ip.setText(AuthenticationController.ip);
        label_port.setText(String.valueOf(AuthenticationController.port));
        btn_info.setOnAction(event -> showDeveloperInfo());
        btn_cancel.setOnAction(event -> exit());
        btn_change.setOnAction(event -> {
            String newIP = field_ip.getText();
            String newPort = field_port.getText();
            if (isValidConnectionProperties(newIP, newPort)) {
                AuthenticationController.setConnectionProperties(newIP, Integer.parseInt(newPort));
                exit();
            }else {
                field_ip.setText("");
                field_port.setText("");
            }
        });
    }

    private boolean isValidConnectionProperties(String ip, String newPort) {
        if(ip.equals("")||newPort.equals("")){
            showErrorMessage("All field should be fill");
            return false;
        }
        int port=0;
        try {
            port=Integer.parseInt(newPort);
        }catch (NumberFormatException e){
            showErrorMessage("The port can only be made up of numbers");
            return false;
        }
        if (port < 0 || port > 65536) {
            showErrorMessage("The port should be less than 65536 and greater than zero");
            return false;
        }
        if(!ip.matches("(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[0-9]{2}|[0-9])(\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[0-9]{2}|[0-9])){3}")){
            showErrorMessage("Invalid IP: "+ip);
            return false;
        }
        return true;
    }

    private void exit() {
        showAndHideStages("/ru/fomin/chat/client/gui/fxml/authentication.fxml", btn_cancel);
    }

}

