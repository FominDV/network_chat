package ru.fomin.chat.client;

import static java.util.Base64.*;
import static ru.fomin.chat.common.Library.*;

import rufomin.network.SocketThread;
import rufomin.network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {
    private static final int WIDTH = 650;
    private static final int HEIGHT = 300;
    private final String cbPrivateText = "Send private message to ";
    boolean isRegistrationProcess = false;
    private RegistrationFrame registrationFrame;
    private ChangingNicknameFrame changingNicknameFrame;
    private ChangingPasswordFrame changingPasswordFrame;
    private String nickName;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JPanel panelTopForButtons = new JPanel(new GridLayout(1, 2));

    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField tfLogin = new JTextField("FDV");
    private final JPasswordField tfPassword = new JPasswordField("123");
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnRegistration = new JButton("Registration");
    private final JPanel panelAllBottom = new JPanel(new GridLayout(2, 1));
    private final JPanel panelButtonsForEditTop = new JPanel(new GridLayout(2, 1));
    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnChangeNickname = new JButton("<html><b>Change<br>Nickname</b></html>");
    private final JButton btnChangePassword = new JButton("<html><b>Change<br>Password</b></html>");
    private final JButton btnDisconnect = new JButton("<html><b>Disconnect</b></html>");
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");
    private JCheckBox cbPrivate = new JCheckBox(cbPrivateText);
    private final JList<String> userList = new JList<>();
    private boolean shownIoErrors = false;
    private SocketThread socketThread;
    private final DateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss: ");
    private final String WINDOW_TITLE = "Chat";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() { // Event Dispatching Thread
                new ClientGUI();
            }
        });
    }

    private ClientGUI() {
        Thread.setDefaultUncaughtExceptionHandler(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(WIDTH, HEIGHT);
        setTitle(WINDOW_TITLE);
        JScrollPane scrollLog = new JScrollPane(log);
        JScrollPane scrollUser = new JScrollPane(userList);
        log.setEditable(false);
        log.setLineWrap(true);
        scrollUser.setPreferredSize(new Dimension(150, 0));
        cbAlwaysOnTop.addActionListener(this);
        btnSend.addActionListener(this);
        tfMessage.addActionListener(this);
        btnLogin.addActionListener(this);
        btnRegistration.addActionListener(this);
        btnDisconnect.addActionListener(this);
        btnChangeNickname.addActionListener(this);
        btnChangePassword.addActionListener(this);
        panelTopForButtons.add(btnLogin);
        panelTopForButtons.add(btnRegistration);
        panelButtonsForEditTop.add(btnChangeNickname);
        panelButtonsForEditTop.add(btnChangePassword);
        panelAllBottom.add(cbPrivate);
        panelAllBottom.add(panelBottom);
        panelAllBottom.setVisible(false);
        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(cbAlwaysOnTop);
        panelTop.add(tfLogin);
        panelTop.add(tfPassword);
        panelTop.add(panelTopForButtons);
        panelBottom.add(btnDisconnect, BorderLayout.WEST);
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        userList.addListSelectionListener(e -> {
            if (userList.getSelectedValue() == null)
                cbPrivate.setText(cbPrivateText);
            else
                cbPrivate.setText(cbPrivateText + userList.getSelectedValue());
        });
        add(panelButtonsForEditTop, BorderLayout.WEST);
        add(scrollLog, BorderLayout.CENTER);
        add(scrollUser, BorderLayout.EAST);
        add(panelTop, BorderLayout.NORTH);
        add(panelAllBottom, BorderLayout.SOUTH);
        setVisible(true);
        panelButtonsForEditTop.setVisible(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == cbAlwaysOnTop) {
            setAlwaysOnTop(cbAlwaysOnTop.isSelected());
        } else if (src == btnSend || src == tfMessage) {
            sendMessage();
        } else if (src == btnLogin) {
            connect();
        } else if (src == btnDisconnect) {
            socketThread.close();
        } else if (src == btnRegistration) {
            isRegistrationProcess = true;
            if (!connect()) return;
            registrationFrame = new RegistrationFrame(this, tfIPAddress.getText(), Integer.parseInt(tfPort.getText()));
            setVisible(false);
        } else if (src == btnChangeNickname) {
            changingNicknameFrame = new ChangingNicknameFrame(this,nickName);
            setVisible(false);
        } else if (src == btnChangePassword) {
            changingPasswordFrame = new ChangingPasswordFrame(this);
            setVisible(false);
        } else throw new RuntimeException("Unknown source: " + src);
    }

    public void sendChangingPasswordMessage(String password, String newPassword) {
        socketThread.sendMessage(getChangingPasswordMessage(password, newPassword));
    }

    public void sendRegistrationData(String login, String password, String nickName) {
        socketThread.sendMessage(getRegistrationMessage(login, password, nickName));
    }

   protected boolean connect() {
        try {
            Socket socket = new Socket(tfIPAddress.getText(), Integer.parseInt(tfPort.getText()));
            socketThread = new SocketThread(this, "Client", socket);
            return true;
        } catch (IOException e) {
            showException(Thread.currentThread(), e);
            return false;
        }
    }

    private void sendMessage() {
        String msg = tfMessage.getText();
        if ("".equals(msg)) return;
        tfMessage.setText(null);
        tfMessage.grabFocus();
        if (cbPrivate.isSelected()) {
            if (!(userList.getSelectedValue() == null)) {
                socketThread.sendMessage(getTypeClientPrivate(userList.getSelectedValue(), msg));
            } else {
                putLog("ERROR");
                cbPrivate.setSelected(false);
            }
        } else {
            socketThread.sendMessage(getTypeClientBcast(msg));
        }
    }

    protected String getFilePath() {
        return (String.format("history_%s.txt", nickName));
    }
    protected String getFilePath(String nickName) {
        return (String.format("history_%s.txt", nickName));
    }

    private void writeMessageToHistory(String msg) {
        if (nickName == null || (msg.length() >= 16 && msg.substring(10, 16).equals(SERVER))) return;
        try (FileWriter out = new FileWriter(getFilePath(), true)) {
            out.write(msg);
            out.flush();
        } catch (IOException e) {
            showIoError(e);
        }
    }

    private void putLog(String msg) {
        if ("".equals(msg)) return;
        SwingUtilities.invokeLater(() -> {
            String message = msg + "\n";
            log.append(message);
            writeMessageToHistory(message);
            log.setCaretPosition(log.getDocument().getLength());
        });
    }

    public void setLoginAndPasswordFields(String login, String password) {
        tfLogin.setText(login);
        tfPassword.setText(password);
    }

    private void showException(Thread t, Throwable e) {
        String msg;
        StackTraceElement[] ste = e.getStackTrace();
        if (ste.length == 0)
            msg = "Empty Stacktrace";
        else {
            msg = String.format("Exception in \"%s\" %s: %s\n\tat %s",
                    t.getName(), e.getClass().getCanonicalName(), e.getMessage(), ste[0]);
            JOptionPane.showMessageDialog(this, msg, "Exception", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, msg, "Exception", JOptionPane.ERROR_MESSAGE);
    }

    private void handleMessage(String msg) {
        String[] arr = msg.split(DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case AUTH_ACCEPT:
                setTitle(WINDOW_TITLE + " entered with nickname: " + arr[1]);
                nickName = arr[1];
                getHistory();
                break;
            case AUTH_DENIED:
                putLog("Authorization failed");
                JOptionPane.showMessageDialog(null, "Login or password is wrong!", "Authorization failed", JOptionPane.ERROR_MESSAGE);
                break;
            case MSG_FORMAT_ERROR:
                log.append(msg + "\n");
                socketThread.close();
                break;
            case TYPE_BROADCAST:
                putLog(DATE_FORMAT.format(Long.parseLong(arr[1])) +
                        arr[2] + ": " + arr[3]);
                break;
            case USER_LIST:
                msg = msg.substring(USER_LIST.length() + DELIMITER.length());
                String[] usersArray = msg.split(DELIMITER);
                Arrays.sort(usersArray);
                userList.setListData(usersArray);
                if (userList.getSelectedValue() == null) userList.setSelectedIndex(0);
                break;
            case TYPE_PRIVATE:
                putLog(DATE_FORMAT.format(Long.parseLong(arr[1])) + "private from " +
                        arr[2] + ": " + arr[3]);
                break;
            case TYPE_ERROR_SENDING_YOURSELF:
                log.append("You try to send message yourself!\n");
                break;
            case REGISTRATION_SUCCESSFULLY:
                registrationFrame.registrationSuccessful();
                isRegistrationProcess = false;
                break;
            case REGISTRATION_NOT_SUCCESSFULLY:
                registrationFrame.registrationNotSuccessful();
                break;
            case NICKNAME_WAS_CHANGED:
                changingNicknameFrame.changingSuccessful();
                break;
            case CHANGING_PASSWORD:
                changingPasswordFrame.changingSuccessful();
                break;
            case CHANGING_PASSWORD_ERROR:
                changingPasswordFrame.changingFailed();
                break;
            default:
                throw new RuntimeException("Unknown message type: " + msg);
        }
    }

    private void getHistory() {
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
            if (countOfLines >= 100) startOfReadingLines = countOfLines - 100;
            else startOfReadingLines = 0;
            for (int i = startOfReadingLines; i < countOfLines; i++) {
                history += lines.get(i);
                if (i != countOfLines - 1) history += "\n";
            }
        } catch (IOException e) {
            System.out.println("History was not found");
        }
        if (!history.equals("")) log.append(history + "\n");
    }

    private void showIoError(Exception e) {
        if (!shownIoErrors) {
            shownIoErrors = true;
            showException(Thread.currentThread(), e);
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        e.printStackTrace();
        showException(t, e);
        System.exit(1);
    }


    /**
     * Socket thread listener methods
     */

    @Override
    public void onSocketStart(SocketThread thread, Socket socket) {
        log.append("Start\n");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        nickName = null;
        try {
            registrationFrame.dispose();
        } catch (Exception e) {
        }
        try {
            changingNicknameFrame.dispose();
        } catch (Exception e) {
        }
        try {
            changingPasswordFrame.dispose();
        } catch (Exception e) {
        }
        setVisible(true);
        log.setText("");
        cbPrivate.setText(cbPrivateText);
        panelAllBottom.setVisible(false);
        panelButtonsForEditTop.setVisible(false);
        panelTop.setVisible(true);
        setTitle(WINDOW_TITLE);
        userList.setListData(new String[0]);
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        if (isRegistrationProcess) return;
        panelAllBottom.setVisible(true);
        panelTop.setVisible(false);
        panelButtonsForEditTop.setVisible(true);
        String login = tfLogin.getText();
        String password = new String(tfPassword.getPassword());
        thread.sendMessage(getAuthRequest(login, password));
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        handleMessage(msg);
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        showException(thread, exception);
    }

    protected void sendChangingNicknameMessage(String newNickname) {
        socketThread.sendMessage(getChangingNicknameMessage(newNickname));
    }
    protected void setNickName(String newNickName){
        nickName=newNickName;
    }
}
