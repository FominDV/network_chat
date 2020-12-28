package ru.fomin.chat.client.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.fomin.chat.client.core.Handler;
import ru.fomin.chat.common.Library;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;

public class ChangePasswordController {
    private static final String MESSAGE_SUCCESSFULLY_CHANGING_PASSWORD = "Password was changed\n";
    private static final String MESSAGE_NOT_SUCCESSFULLY_CHANGING_PASSWORD = "You inserted wrong password\nor something else went wrong by server";
    @FXML
    private Button btn_info;

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_change;

    @FXML
    private PasswordField field_password;

    @FXML
    private PasswordField field_new_password;

    @FXML
    private PasswordField field_repeat_password;

    @FXML
    void initialize() {
        Handler.changePasswordController = this;
        btn_info.setOnAction(event -> showDeveloperInfo());
        btn_cancel.setOnAction(event -> exit());
        btn_change.setOnAction(event -> changePassword());
    }

    private void changePassword() {
        String password = field_password.getText(), newPassword = field_new_password.getText(), repeatedPassword = field_repeat_password.getText();
        if (isValidData(password, newPassword, repeatedPassword))
            AuthenticationController.handler.sendMessage(Library.getChangingPasswordMessage(password, newPassword));
        else clearFields();
    }

    private boolean isValidData(String password, String newPassword, String repeatedPassword) {
        if(password.equals("")||newPassword.equals("")||repeatedPassword.equals("")){
            showErrorMessage("All field should be filled");
            return false;
        }
        if (password.contains(" ")) {
            showErrorMessage("Password should not contain spaces");
            return false;
        }
        if (password.length() > RegistrationController.maxLoginLength) {
            showErrorMessage("Length of password should not be greater than " + RegistrationController.maxPasswordLength);
            return false;
        }
        if(!newPassword.equals(repeatedPassword)){
            showErrorMessage("Repeated password is not equal to new password");
            return false;
        }
        return true;
    }

    private void clearFields() {
        field_password.setText("");
        field_new_password.setText("");
        field_repeat_password.setText("");
    }

    public void exit() {
        ChatController.isChangingPasswordOpened = false;
        btn_change.getScene().getWindow().hide();
    }

    public void changingSuccessful() {
        showInfoMessage(MESSAGE_SUCCESSFULLY_CHANGING_PASSWORD);
        exit();
    }

    public void changingFailed() {
        showErrorMessage(MESSAGE_NOT_SUCCESSFULLY_CHANGING_PASSWORD);
        clearFields();
    }
}

