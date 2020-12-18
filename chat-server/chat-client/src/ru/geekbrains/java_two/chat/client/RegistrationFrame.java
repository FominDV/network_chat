package ru.geekbrains.java_two.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RegistrationFrame extends JFrame implements ActionListener {
    private final int maxLoginLength = 15;
    private final int maxPasswordLength = 20;
    private final int maxNicknameLength = 15;
    private final String TITLE = "Registration";
    private final int WIDTH = 300;
    private final int HEIGHT = 300;
    private final ClientGUI clientGUI;
    private final JPanel MAIN_PANEL = new JPanel(new GridLayout(6, 1));
    private final JPanel[] SUB_PANEL = new JPanel[4];
    private final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.ITALIC, 16);
    private final Font TEXT_BOTTOM_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    private final Font TEXT_FONT_HEADER = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private final JLabel LABEL_HEADER = new JLabel("REGISTRATION");
    private final JLabel LABEL_LOGIN = new JLabel("Login:");
    private final JLabel LABEL_NICKNAME = new JLabel("NickName:");
    private final JLabel LABEL_PASSWORD = new JLabel("Password:");
    private final JLabel LABEL_REPEAT_PASSWORD = new JLabel("Repeat password:");
    private final JTextField FIELD_LOGIN = new JTextField();
    private final JTextField FIELD_NICKNAME = new JTextField();
    private final JPasswordField FIELD_PASSWORD = new JPasswordField();
    private final JPasswordField FIELD_REPEAT_PASSWORD = new JPasswordField();
    private final JPanel BOTTOM_PANEL = new JPanel(new GridLayout(2, 1));
    private final JPanel NETWORK_PANEL = new JPanel(new GridLayout(1, 2));
    private final JButton BUTTON_REGISTRATION = new JButton("REGISTRATION");
    private final JButton BUTTON_CANCEL = new JButton("CANCEL");
    private final JLabel LABEL_IP = new JLabel();
    private final JLabel LABEL_PORT = new JLabel();
    private final String IP;
    private final int PORT;

    public RegistrationFrame(ClientGUI clientGUI, String ip, int port) {
        this.clientGUI=clientGUI;
        IP = ip;
        PORT = port;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setResizable(false);
        for (int i = 0; i < SUB_PANEL.length; i++) SUB_PANEL[i] = new JPanel(new GridLayout(1, 2));
        LABEL_HEADER.setFont(TEXT_FONT_HEADER);
        LABEL_HEADER.setHorizontalAlignment(SwingConstants.CENTER);
        LABEL_NICKNAME.setFont(TEXT_FONT);
        LABEL_LOGIN.setFont(TEXT_FONT);
        LABEL_PASSWORD.setFont(TEXT_FONT);
        LABEL_REPEAT_PASSWORD.setFont(TEXT_FONT);
        FIELD_NICKNAME.setFont(TEXT_FONT);
        FIELD_LOGIN.setFont(TEXT_FONT);
        FIELD_PASSWORD.setFont(TEXT_FONT);
        FIELD_REPEAT_PASSWORD.setFont(TEXT_FONT);
        SUB_PANEL[0].add(LABEL_NICKNAME);
        SUB_PANEL[0].add(FIELD_NICKNAME);
        SUB_PANEL[1].add(LABEL_LOGIN);
        SUB_PANEL[1].add(FIELD_LOGIN);
        SUB_PANEL[2].add(LABEL_PASSWORD);
        SUB_PANEL[2].add(FIELD_PASSWORD);
        SUB_PANEL[3].add(LABEL_REPEAT_PASSWORD);
        SUB_PANEL[3].add(FIELD_REPEAT_PASSWORD);
        MAIN_PANEL.add(LABEL_HEADER);
        for (JPanel panel : SUB_PANEL) MAIN_PANEL.add(panel);
        MAIN_PANEL.add(BUTTON_REGISTRATION);
        BUTTON_REGISTRATION.setFont(TEXT_FONT_HEADER);
        BUTTON_CANCEL.setFont(TEXT_FONT_HEADER);
        BUTTON_CANCEL.setBackground(Color.RED);
        LABEL_IP.setText("IP: " + IP);
        LABEL_PORT.setText("PORT: " + PORT);
        LABEL_IP.setFont(TEXT_BOTTOM_FONT);
        LABEL_PORT.setFont(TEXT_BOTTOM_FONT);
        NETWORK_PANEL.add(LABEL_IP);
        NETWORK_PANEL.add(LABEL_PORT);
        BOTTOM_PANEL.add(NETWORK_PANEL);
        BOTTOM_PANEL.add(BUTTON_CANCEL);
        BUTTON_REGISTRATION.addActionListener(this);
        BUTTON_CANCEL.addActionListener(this);
        add(MAIN_PANEL, BorderLayout.CENTER);
        add(BOTTOM_PANEL, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(BUTTON_CANCEL)) {
            clientGUI.isRegistrationProcess=false;
            cancel();
            return;
        }
        if (source.equals(BUTTON_REGISTRATION)) {
            registration();
            return;
        }
        throw new RuntimeException("Unknown source: " + source);
    }

    private void registration() {
        if (isValidData()) clientGUI.sendRegistrationData(getLogin(), String.valueOf(getPassword()), getNickName());
    }

    private boolean isValidData() {
        String password = String.valueOf(getPassword());
        String repeatPassword = new String(FIELD_REPEAT_PASSWORD.getPassword());
        String nickname = getNickName();
        String login = getLogin();
        if (login.equals("")) {
            showInvalidDataError("Field of login should be not empty");
            return false;
        }
        if (password.equals("")) {
            showInvalidDataError("Field of password should be not empty");
            return false;
        }
        if (nickname.equals("")) {
            showInvalidDataError("Field of nickname should be not empty");
            return false;
        }
        if (nickname.contains(" ")) {
            showInvalidDataError("Nickname should not contain spaces");
            return false;
        }
        if (login.contains(" ")) {
            showInvalidDataError("Login should not contain spaces");
            return false;
        }
        if (password.contains(" ")) {
            showInvalidDataError("Password should not contain spaces");
            return false;
        }
        if (login.length() > maxLoginLength) {
            showInvalidDataError("Length of login should not be greater than " + maxLoginLength);
            return false;
        }
        if (nickname.length() > maxNicknameLength) {
            showInvalidDataError("Length of nickname should not be greater than " + maxNicknameLength);
            return false;
        }
        if (password.length() > maxLoginLength) {
            showInvalidDataError("Length of password should not be greater than " + maxPasswordLength);
            return false;
        }
        if (!password.equals(repeatPassword)) {
            showInvalidDataError("Passwords into field should be equal");
            return false;
        }
        return true;
    }

    private void showInvalidDataError(String message) {
        JOptionPane.showMessageDialog(null, message, "Invalid registration's data", JOptionPane.ERROR_MESSAGE);
    }

    private void cancel() {
        clientGUI.setVisible(true);
        dispose();
    }

    public String getNickName() {
        return FIELD_NICKNAME.getText();
    }

    public String getLogin() {
        return FIELD_LOGIN.getText();
    }

    public char[] getPassword() {
        return FIELD_PASSWORD.getPassword();
    }

    public void registrationSuccessful() {
        clientGUI.setLoginAndPasswordFields(getLogin(), String.valueOf(getPassword()));
        showResultRegistration("Registration is successful\nLogin: " + getLogin() + "\nNickName: " + FIELD_NICKNAME.getText());
        cancel();
    }

    private void showResultRegistration(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void registrationNotSuccessful() {
        showInvalidDataError("Login or nickname is already registered\nLogin: " + getLogin() + "\nNickName: " + FIELD_NICKNAME.getText());
        FIELD_NICKNAME.setText("");
        FIELD_LOGIN.setText("");
        FIELD_PASSWORD.setText("");
        FIELD_REPEAT_PASSWORD.setText("");
    }
}

