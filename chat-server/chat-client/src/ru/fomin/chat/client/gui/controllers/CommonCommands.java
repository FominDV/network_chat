package ru.fomin.chat.client.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Labeled;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public final class CommonCommands {
    static void showDeveloperInfo() {
        JOptionPane.showMessageDialog(null,
                "<html>Developer: Dmitriy Fomin<br>GitHub: https://github.com/FominDV <br> Email: 79067773397@yandex.ru<br>*All rights reserved*</html>",
                "Developer info", JOptionPane.INFORMATION_MESSAGE);
    }

    static void showAndHideStages(String pathOfFXML, Labeled o) {
        o.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(CommonCommands.class.getResource(pathOfFXML));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
