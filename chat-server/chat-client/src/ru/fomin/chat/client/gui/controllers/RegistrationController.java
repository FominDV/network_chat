package ru.fomin.chat.client.gui.controllers;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;
import static ru.fomin.chat.common.Library.getAuthRequest;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.fomin.chat.common.Library;

import javax.swing.*;

public class RegistrationController {
public static final int maxLoginLength=15,maxNicknameLength=20,maxPasswordLength=15;
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
    private PasswordField field_password;

    @FXML
    private PasswordField field_repeat_password;
    @FXML
    void initialize() {
AuthenticationController.registrationController=this;

        btn_info.setOnAction(event -> showDeveloperInfo());
        btn_cancel.setOnAction(event -> exit());
        btn_registration.setOnAction(event ->{
            String login=getNewLogin(), password=new String(field_password.getText()), repeatPassword=field_repeat_password.getText(), nickname=getNewNickname();
            if(isValidRegistrationData(login,password,repeatPassword,nickname)) registration(login,password,nickname);else clearFields();} );
    }

    private void registration(String login, String password, String nickname) {
        AuthenticationController.handler.sendMessage(Library.getRegistrationMessage(login,password,nickname));
    }

    private boolean isValidRegistrationData(String login, String password, String repeatPassword,String nickname) {
        if(login.equals("")||password.equals("")||nickname.equals("")){ showErrorMessage("All fields should be filled");return false;}
        if (nickname.contains(" ")) {
            showErrorMessage("Nickname should not contain spaces");
            return false;
        }
        if (login.contains(" ")) {
            showErrorMessage("Login should not contain spaces");
            return false;
        }
        if (password.contains(" ")) {
            showErrorMessage("Password should not contain spaces");
            return false;
        }
        if (login.length() > maxLoginLength) {
            showErrorMessage("Length of login should not be greater than " + maxLoginLength);
            return false;
        }
        if (nickname.length() > maxNicknameLength) {
            showErrorMessage("Length of nickname should not be greater than " + maxNicknameLength);
            return false;
        }
        if (password.length() > maxLoginLength) {
            showErrorMessage("Length of password should not be greater than " + maxPasswordLength);
            return false;
        }
        if (!password.equals(repeatPassword)) {
            showErrorMessage("Passwords into field should be equal");
            return false;
        }
        return true;
    }
    public void registrationSuccessful(){
        showInfoMessage("Registration is successful\nLogin: " + getNewLogin() + "\nNickName: " + getNewNickname());
        Platform.runLater(()->showAndHideStages("/ru/fomin/chat/client/gui/fxml/chat.fxml", btn_registration));
        AuthenticationController.handler.sendMessage(getAuthRequest(getNewLogin(), field_password.getText()));
    }
    public void registrationNotSuccessful(){
        showErrorMessage("Login is already registered\nLogin: " + getNewLogin());
        clearFields();
    }
    public void exit() {
            AuthenticationController.socketTreadStop();
    }
    public void returnToAuthentication(){
        showAndHideStages("/ru/fomin/chat/client/gui/fxml/authentication.fxml", btn_cancel);
    }
private String getNewLogin(){
        return field_login.getText();
}
    private String getNewNickname(){
        return field_nickname.getText();
    }
    private void clearFields(){
        field_login.setText("");
        field_nickname.setText("");
        field_password.setText("");
        field_repeat_password.setText("");
    }
}

