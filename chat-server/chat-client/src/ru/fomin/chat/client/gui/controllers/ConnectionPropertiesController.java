package ru.fomin.chat.client.gui.controllers;


    import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

    import static ru.fomin.chat.client.gui.controllers.CommonCommands.showDeveloperInfo;

public class ConnectionPropertiesController {

        @FXML
        private Button btn_info;

        @FXML
        private Button btnCancel;

        @FXML
        private TextField field_port;

        @FXML
        private Button btn_change;

        @FXML
        private PasswordField field_ip;

        @FXML
        void initialize() {
            btn_info.setOnAction(event -> showDeveloperInfo());

        }
    }

