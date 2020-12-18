package ru.fomin.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistrationFrame extends JFrame implements ActionListener {
    private final int maxLoginLength = 15;
    private final int maxPasswordLength = 20;
    private final int maxNicknameLength = 15;
    private static final String TITLE = "Registration";
    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;
    private final ClientGUI clientGUI;
    private final JPanel mainPanel = new JPanel(new GridLayout(6, 1));
    private final JPanel[] subPanel = new JPanel[4];
    private final Font textFont = new Font(Font.SANS_SERIF, Font.ITALIC, 16);
    private final Font textBottomFont = new Font(Font.SANS_SERIF, Font.BOLD, 14);
    private final Font textFontHeader = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    private final JLabel labelHeader = new JLabel("REGISTRATION");
    private final JLabel labelLogin = new JLabel("Login:");
    private final JLabel labelNickname = new JLabel("NickName:");
    private final JLabel labelPassword = new JLabel("Password:");
    private final JLabel labelRepeatPassword = new JLabel("Repeat password:");
    private final JTextField fieldLogin = new JTextField();
    private final JTextField fieldNickname = new JTextField();
    private final JPasswordField fieldPassword = new JPasswordField();
    private final JPasswordField fieldRepeatPassword = new JPasswordField();
    private final JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
    private final JPanel networkPanel = new JPanel(new GridLayout(1, 2));
    private final JButton buttonRegistration = new JButton("REGISTRATION");
    private final JButton buttonCancel = new JButton("CANCEL");
    private final JLabel labelIp = new JLabel();
    private final JLabel labelPort = new JLabel();
    private final String ip;
    private final int port;

    public RegistrationFrame(ClientGUI clientGUI, String ip, int port) {
        this.clientGUI=clientGUI;
        this.ip = ip;
        this.port = port;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setResizable(false);
        for (int i = 0; i < subPanel.length; i++) subPanel[i] = new JPanel(new GridLayout(1, 2));
        labelHeader.setFont(textFontHeader);
        labelHeader.setHorizontalAlignment(SwingConstants.CENTER);
        labelNickname.setFont(textFont);
        labelLogin.setFont(textFont);
        labelPassword.setFont(textFont);
        labelRepeatPassword.setFont(textFont);
        fieldNickname.setFont(textFont);
        fieldLogin.setFont(textFont);
        fieldPassword.setFont(textFont);
        fieldRepeatPassword.setFont(textFont);
        subPanel[0].add(labelNickname);
        subPanel[0].add(fieldNickname);
        subPanel[1].add(labelLogin);
        subPanel[1].add(fieldLogin);
        subPanel[2].add(labelPassword);
        subPanel[2].add(fieldPassword);
        subPanel[3].add(labelRepeatPassword);
        subPanel[3].add(fieldRepeatPassword);
        mainPanel.add(labelHeader);
        for (JPanel panel : subPanel) mainPanel.add(panel);
        mainPanel.add(buttonRegistration);
        buttonRegistration.setFont(textFontHeader);
        buttonCancel.setFont(textFontHeader);
        buttonCancel.setBackground(Color.RED);
        labelIp.setText("IP: " + ip);
        labelPort.setText("PORT: " + port);
        labelIp.setFont(textBottomFont);
        labelPort.setFont(textBottomFont);
        networkPanel.add(labelIp);
        networkPanel.add(labelPort);
        bottomPanel.add(networkPanel);
        bottomPanel.add(buttonCancel);
        buttonRegistration.addActionListener(this);
        buttonCancel.addActionListener(this);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(buttonCancel)) {
            clientGUI.isRegistrationProcess=false;
            cancel();
            return;
        }
        if (source.equals(buttonRegistration)) {
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
        String repeatPassword = new String(fieldRepeatPassword.getPassword());
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

    private String getNickName() {
        return fieldNickname.getText();
    }

    private String getLogin() {
        return fieldLogin.getText();
    }

    private char[] getPassword() {
        return fieldPassword.getPassword();
    }

    public void registrationSuccessful() {
        clientGUI.setLoginAndPasswordFields(getLogin(), String.valueOf(getPassword()));
        showResultRegistration("Registration is successful\nLogin: " + getLogin() + "\nNickName: " + fieldNickname.getText());
        cancel();
    }

    private void showResultRegistration(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void registrationNotSuccessful() {
        showInvalidDataError("Login or nickname is already registered\nLogin: " + getLogin() + "\nNickName: " + fieldNickname.getText());
        fieldNickname.setText("");
        fieldLogin.setText("");
        fieldPassword.setText("");
        fieldRepeatPassword.setText("");
    }
}

