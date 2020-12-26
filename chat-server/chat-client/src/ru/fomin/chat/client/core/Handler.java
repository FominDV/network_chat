package ru.fomin.chat.client.core;

import rufomin.network.SocketThread;
import rufomin.network.SocketThreadListener;

import static ru.fomin.chat.common.Library.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.fomin.chat.common.Library.*;

public class Handler implements ActionListener,  SocketThreadListener {
    private SocketThread socketThread;
    private String nickName;

    public Handler(int port, String ip) {
        try {
            Socket socket = new Socket(ip, port);
            socketThread = new SocketThread(this, "Client", socket);
        } catch (IOException e) {
            showException(Thread.currentThread(), e);
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

    private void showException(Thread t, Throwable e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                    t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
            JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
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
                history += lines.get(i);
                if (i != countOfLines - 1) history += "\n";
            }
        } catch (IOException e) {
            System.out.println("History was not found");
        }
        return (history + "\n");
    }

    protected String getFilePath() {
        return (String.format("history_%s.txt", nickName));
    }

    protected String getFilePath(String nickName) {
        return (String.format("history_%s.txt", nickName));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }



    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {

    }

    @Override
    public void onSocketStop(SocketThread thread) {

    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {

    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {

    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {

    }
}
