package ru.fomin.chat.client.core;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.fomin.chat.client.gui.controllers.AuthenticationController;
import ru.fomin.chat.client.gui.controllers.CommonCommands;
import ru.fomin.chat.client.gui.controllers.RegistrationController;
import rufomin.network.SocketThread;
import rufomin.network.SocketThreadListener;

import static ru.fomin.chat.client.gui.controllers.CommonCommands.*;
import static ru.fomin.chat.common.Library.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.fomin.chat.common.Library.*;

public class Handler implements SocketThreadListener {
    private SocketThread socketThread;
    private String nickName;
    private AuthenticationController authenticationController;


    public Handler(int port, String ip, AuthenticationController authenticationController) {
        this.authenticationController=authenticationController;
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
                // setTitle(WINDOW_TITLE + " entered with nickname: " + arr[1]);
                nickName = arr[1];
                getHistory();
                break;
            case AUTH_DENIED:
                // putLog("Authorization failed");
                JOptionPane.showMessageDialog(null, "Login or password is wrong!", "Authorization failed", JOptionPane.ERROR_MESSAGE);
                break;
            case MSG_FORMAT_ERROR:
                //  log.append(msg + "\n");
                socketThread.close();
                break;
            case TYPE_BROADCAST:
                //   putLog(DATE_FORMAT.format(Long.parseLong(arr[1])) +
                //     arr[2] + ": " + arr[3]);
                break;
            case USER_LIST:
                msg = msg.substring(USER_LIST.length() + DELIMITER.length());
                String[] usersArray = msg.split(DELIMITER);
                Arrays.sort(usersArray);
                //  userList.setListData(usersArray);
                //   if (userList.getSelectedValue() == null) userList.setSelectedIndex(0);
                break;
            case TYPE_PRIVATE:
                // putLog(DATE_FORMAT.format(Long.parseLong(arr[1])) + "private from " +
                //  arr[2] + ": " + arr[3]);
                break;
            case TYPE_ERROR_SENDING_YOURSELF:
                // log.append("You try to send message yourself!\n");
                break;
            case REGISTRATION_SUCCESSFULLY:
                //  registrationFrame.registrationSuccessful();
                // isRegistrationProcess = false;
                break;
            case REGISTRATION_NOT_SUCCESSFULLY:
                //  registrationFrame.registrationNotSuccessful();
                break;
            case NICKNAME_WAS_CHANGED:
                //  changingNicknameFrame.changingSuccessful();
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
                history += lines.get(i)+"\n";
            }
        } catch (IOException e) {
            System.out.println("History was not found");
        }
        return (history);
    }

    protected String getFilePath() {
        return (String.format("history_%s.txt", nickName));
    }

    protected String getFilePath(String nickName) {
        return (String.format("history_%s.txt", nickName));
    }

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {

    }

    @Override
    public void onSocketStop(SocketThread thread) {
        authenticationController.changeIsConnected();
        Platform.runLater(()->{
            
        });


        nickName = null;

    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
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

    public void setNickName(String newNickName) {
        nickName = newNickName;
    }

    public void logIn(String login, String password) {
        socketThread.sendMessage(getAuthRequest(login, password));
    }


}
