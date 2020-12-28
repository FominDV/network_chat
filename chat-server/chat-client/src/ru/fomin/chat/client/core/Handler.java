package ru.fomin.chat.client.core;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.fomin.chat.client.gui.controllers.*;
import rufomin.network.SocketThread;
import rufomin.network.SocketThreadListener;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;
import static ru.fomin.chat.common.Library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.fomin.chat.common.Library.*;

public class Handler implements SocketThreadListener {
    private SocketThread socketThread;
    public static String nickName;
    private AuthenticationController authenticationController;
    private ChatController chatController;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    public static ChangeNicknameController changeNicknameController;

    public Handler(int port, String ip, AuthenticationController authenticationController) {
        this.authenticationController = authenticationController;
        try {
            Socket socket = new Socket(ip, port);
            socketThread = new SocketThread(this, "Client", socket);
        } catch (IOException e) {
            authenticationController.changeIsConnected();
            showConnectionError();
        }
    }


    private void handleMessage(String msg) {
        String[] arr = msg.split(DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case AUTH_ACCEPT:
                Platform.runLater(() -> {
                    nickName = arr[1];
                    chatController.setTitle(arr[1]);
                    chatController.setHistoryToLog(getHistory());
                });
                break;
            case AUTH_DENIED:
                JOptionPane.showMessageDialog(null, "Login or password is wrong!", "Authorization failed", JOptionPane.ERROR_MESSAGE);
                break;
            case MSG_FORMAT_ERROR:
                String finalMsg = msg;
                Platform.runLater(() -> chatController.appendToLog(finalMsg + "\n"));
                socketThread.close();
                break;
            case TYPE_BROADCAST:
                Platform.runLater(() -> chatController.appendToLog(DATE_FORMAT.format(Long.parseLong(arr[1])) + arr[2] + ": " + arr[3]));
                break;
            case USER_LIST:
                msg = msg.substring(USER_LIST.length() + DELIMITER.length());
                String[] usersArray = msg.split(DELIMITER);
                Arrays.sort(usersArray);
                Platform.runLater(() -> chatController.setUsersList(usersArray));
                break;
            case TYPE_PRIVATE:
                Platform.runLater(() -> chatController.appendToLog(DATE_FORMAT.format(Long.parseLong(arr[1])) + "private from " + arr[2] + ": " + arr[3]));
                break;
            case TYPE_ERROR_SENDING_YOURSELF:
                Platform.runLater(() -> chatController.appendToLog("You try to send message yourself!"));
                break;
            case REGISTRATION_SUCCESSFULLY:
                  AuthenticationController.registrationController.registrationSuccessful();
                break;
            case REGISTRATION_NOT_SUCCESSFULLY:
                AuthenticationController.registrationController.registrationNotSuccessful();
                break;
            case NICKNAME_WAS_CHANGED:
               Platform.runLater(()->changeNicknameController.changingSuccessful());
                break;
            case CHANGING_PASSWORD:
                //  changingPasswordFrame.changingSuccessful();
                break;
            case CHANGING_PASSWORD_ERROR:
                //  changingPasswordFrame.changingFailed();
                break;
            default:
                throw new RuntimeException("Unknown message type: " + msg);
        }
    }


    private String getHistory() {
        String history = "";
        try (RandomAccessFile in = new RandomAccessFile(getFilePath(), "r")) {
            List<String> lines = new ArrayList<>();
            String line;
            while (true) {
                line = in.readLine();
                if (line != null) lines.add(line);
                else break;
            }
            int startOfReadingLines, countOfLines = lines.size();
            if (countOfLines == 0) return "";
            if (countOfLines >= 100) startOfReadingLines = countOfLines - 100;
            else startOfReadingLines = 0;
            for (int i = startOfReadingLines; i < countOfLines; i++) {
                history += lines.get(i) + "\n";
            }
        } catch (IOException e) {
            System.out.println("History was not found");
        }
        return (history);
    }
    public static void writeMessageToHistory(String msg) {
        if (nickName == null ||msg.startsWith("You try")|| (msg.length() >= 16 && msg.substring(10, 16).equals(SERVER))) return;
        try (FileWriter out = new FileWriter(getFilePath(), true)) {
            out.write(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getFilePath() {
        return (String.format("history_%s.txt", nickName));
    }

    public static String getFilePath(String nickName) {
        return (String.format("history_%s.txt", nickName));
    }

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {

    }

    @Override
    public void onSocketStop(SocketThread thread) {
        authenticationController.changeIsConnected();
        Platform.runLater(() -> {
            authenticationController.reload();
            try {
                chatController.hide();
            } catch (NullPointerException e) {

            }
        });
        nickName = null;

    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        if(chatController!=null)
            Platform.runLater(() -> chatController.appendToLog("Start"));
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        handleMessage(msg);
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        authenticationController.changeIsConnected();
        showConnectionError();
    }

    private void showConnectionError() {
        JOptionPane.showMessageDialog(null, "Connection was lost", "Connection ERROR", JOptionPane.ERROR_MESSAGE);
    }

    public void sendChangingNicknameMessage(String newNickname) {
        socketThread.sendMessage(getChangingNicknameMessage(newNickname));
    }

    public static void setNickName(String newNickName) {
        nickName = newNickName;
    }

    public void stopSocketThread() {
        socketThread.close();
    }

    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }

    public void sendMessage(String message) {
        socketThread.sendMessage(message);
    }

}
