package ru.fomin.chat.client.gui.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public final class CommonCommands {
     static void showDeveloperInfo() {
        JOptionPane.showMessageDialog(null,
                "<html>Developer: Dmitriy Fomin<br>GitHub: https://github.com/FominDV <br> Email: 79067773397@yandex.ru<br>*All rights reserved*</html>",
                "Developer info", JOptionPane.INFORMATION_MESSAGE);
    }
     static void showStage(String pathOfFXML){
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
         stage.show();
     }
}
