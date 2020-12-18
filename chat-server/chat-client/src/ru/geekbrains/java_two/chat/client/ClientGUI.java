package ru.geekbrains.java_two.chat.client;

import ru.geekbrains.java_two.chat.common.Library;
import ru.geekbrains.java_two.network.SocketThread;
import ru.geekbrains.java_two.network.SocketThreadListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class ClientGUI extends JFrame implements ActionListener, Thread.UncaughtExceptionHandler, SocketThreadListener {
    private static final int WIDTH = 650;
    private static final int HEIGHT = 300;
    private final String cbPrivateText = "Send private message to ";
    boolean isRegistrationProcess = false;
    private RegistrationFrame registrationFrame;
    private ChangingNicknameFrame changingNicknameFrame;

    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2, 3));
    private final JPanel panelTopForButtons = new JPanel(new GridLayout(1, 2));

    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JCheckBox cbAlwaysOnTop = new JCheckBox("Always on top");
    private final JTextField tfLogin = new JTextField("ivan");
    private final JPasswordField tfPassword = new JPasswordField("123");
    private final JButton btnLogin = new JButton("Login");
    private final JButton btnRegistration = new JButton("Registration");
    private final JPanel panelAllBottom = new JPanel(new GridLayout(2, 1));
    private final JPanel panelBottomTop = new JPanel(new GridLayout(1, 2));
    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JButton btnChangeNickname = new JButton("<html><b>Change nickname</b></html>");
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
        panelTopForButtons.add(btnLogin);
        panelTopForButtons.add(btnRegistration);
        panelBottomTop.add(cbPrivate);
        panelBottomTop.add(btnChangeNickname);
        panelAllBottom.add(panelBottomTop);
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
        add(scrollLog, BorderLayout.CENTER);
        add(scrollUser, BorderLayout.EAST);
        add(panelTop, BorderLayout.NORTH);
        add(panelAllBottom, BorderLayout.SOUTH);
        setVisible(true);
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
            changingNicknameFrame = new ChangingNicknameFrame(this);
            setVisible(false);
        } else throw new RuntimeException("Unknown source: " + src);
    }

    public void sendRegistrationData(String login, String password, String nickName) {
        socketThread.sendMessage(Library.getRegistrationMessage(login, password, nickName));
    }

    private boolean connect() {
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
                socketThread.sendMessage(Library.getTypeClientPrivate(userList.getSelectedValue(), msg));
            } else {
                putLog("ERROR");
                cbPrivate.setSelected(false);
            }
        } else {
            socketThread.sendMessage(Library.getTypeClientBcast(msg));
        }
    }

    private void wrtMsgToLogFile(String msg, String username) {
        try (FileWriter out = new FileWriter("log.txt", true)) {
            out.write(username + ": " + msg + "\n");
            out.flush();
        } catch (IOException e) {
            if (!shownIoErrors) {
                shownIoErrors = true;
                showException(Thread.currentThread(), e);
            }
        }
    }

    private void putLog(String msg) {
        if ("".equals(msg)) return;
        SwingUtilities.invokeLater(() -> {
            log.append(msg + "\n");
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
        String[] arr = msg.split(Library.DELIMITER);
        String msgType = arr[0];
        switch (msgType) {
            case Library.AUTH_ACCEPT:
                setTitle(WINDOW_TITLE + " entered with nickname: " + arr[1]);
                break;
            case Library.AUTH_DENIED:
                putLog("Authorization failed");
                break;
            case Library.MSG_FORMAT_ERROR:
                putLog(msg);
                socketThread.close();
                break;
            case Library.TYPE_BROADCAST:
                putLog(DATE_FORMAT.format(Long.parseLong(arr[1])) +
                        arr[2] + ": " + arr[3]);
                break;
            case Library.USER_LIST:
                msg = msg.substring(Library.USER_LIST.length() + Library.DELIMITER.length());
                String[] usersArray = msg.split(Library.DELIMITER);
                Arrays.sort(usersArray);
                userList.setListData(usersArray);
                if (userList.getSelectedValue() == null) userList.setSelectedIndex(0);
                break;
            case Library.TYPE_PRIVATE:
                putLog(DATE_FORMAT.format(Long.parseLong(arr[1])) + "private from " +
                        arr[2] + ": " + arr[3]);
                break;
            case Library.TYPE_ERROR_SENDING_YOURSELF:
                putLog("You try to send message yourself!");
                break;
            case Library.REGISTRATION_SUCCESSFULLY:
                registrationFrame.registrationSuccessful();
                isRegistrationProcess = false;
                break;
            case Library.REGISTRATION_NOT_SUCCESSFULLY:
                registrationFrame.registrationNotSuccessful();
                break;
            default:
                throw new RuntimeException("Unknown message type: " + msg);
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
        putLog("Start");
    }

    @Override
    public void onSocketStop(SocketThread thread) {
        try {
            registrationFrame.dispose();
        } catch (Exception e) {
        }
        try {
            changingNicknameFrame.dispose();
        } catch (Exception e) {
        }
        setVisible(true);
        cbPrivate.setText(cbPrivateText);
        panelAllBottom.setVisible(false);
        panelTop.setVisible(true);
        setTitle(WINDOW_TITLE);
        userList.setListData(new String[0]);
        JOptionPane.showMessageDialog(null, "Connection was lost");
    }

    @Override
    public void onSocketReady(SocketThread thread, Socket socket) {
        if (isRegistrationProcess) return;
        panelAllBottom.setVisible(true);
        panelTop.setVisible(false);
        String login = tfLogin.getText();
        String password = new String(tfPassword.getPassword());
        thread.sendMessage(Library.getAuthRequest(login, password));
    }

    @Override
    public void onReceiveString(SocketThread thread, Socket socket, String msg) {
        handleMessage(msg);
    }

    @Override
    public void onSocketException(SocketThread thread, Exception exception) {
        showException(thread, exception);
    }
}
