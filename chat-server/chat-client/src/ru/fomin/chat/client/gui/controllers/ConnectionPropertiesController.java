package ru.fomin.chat.client.gui.controllers;


    import javafx.fxml.FXML;
import javafx.scene.control.Button;
    import javafx.scene.control.Label;
    import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

    import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;

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
    private Label label_ip;

    @FXML
    private Label label_port;

        @FXML
        void initialize() {
            label_ip.setText(AuthenticationController.ip);
            label_port.setText(String.valueOf(AuthenticationController.port));
            btn_info.setOnAction(event -> showDeveloperInfo());
            btnCancel.setOnAction(event -> showAndHideStages("/ru/fomin/chat/client/gui/fxml/authentication.fxml",btnCancel));
        }
    }

