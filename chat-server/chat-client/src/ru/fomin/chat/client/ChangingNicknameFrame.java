package ru.fomin.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangingNicknameFrame extends JFrame implements ActionListener {
    private static final String TITLE = "Edit nickname";
    private static final int WIDTH = 200;
    private static final int HEIGHT = 150;
    private final ClientGUI clientGUI;
    private static final String MESSAGE_SUCCESSFULLY_CHANGING_NICKNAME="Nickname was changed\nFor finishing this process you should reconnect!\nNew nickname: ";

    private final Label label = new Label("NEW NICKNAME:");
    private final TextField tfNickName = new TextField();
    private final JButton btnChange = new JButton("CHANGE");
    private final JButton btnCancel = new JButton("CANCEL");
    private final JPanel panel = new JPanel(new GridLayout(4, 1));

    ChangingNicknameFrame(ClientGUI clientGUI) {
        this.clientGUI = clientGUI;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setResizable(false);
        btnCancel.setBackground(Color.RED);
        btnChange.addActionListener(this);
        btnCancel.addActionListener(this);
        panel.add(label);
        panel.add(tfNickName);
        panel.add(btnChange);
        panel.add(btnCancel);
        add(panel);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnChange) {
            String newNickname = tfNickName.getText();
            if (newNickname.equals(""))
                JOptionPane.showMessageDialog(null, "Uou should insert new nickname!", "ERROR", JOptionPane.ERROR_MESSAGE);
            else clientGUI.sendChangingNicknameMessage(newNickname);
        } else if (source == btnCancel) {
            cancel();
        } else throw new RuntimeException("Unknown source: " + source);
    }

    public void changingSuccessful() {
        JOptionPane.showMessageDialog(null,MESSAGE_SUCCESSFULLY_CHANGING_NICKNAME+tfNickName.getText());
        cancel();
    }
    private void cancel(){
        clientGUI.setVisible(true);
        dispose();
    }
}
