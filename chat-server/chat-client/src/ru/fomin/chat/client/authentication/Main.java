package ru.fomin.chat.client.authentication;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
static Stage authenticationFrame;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        authenticationFrame=primaryStage;
        primaryStage.setTitle("Authentication");
        primaryStage.setScene(new Scene(root, 290, 400));
        primaryStage.show();
       primaryStage.setResizable(false);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
