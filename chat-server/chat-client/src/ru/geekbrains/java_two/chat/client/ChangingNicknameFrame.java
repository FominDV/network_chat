package ru.geekbrains.java_two.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangingNicknameFrame extends JFrame implements ActionListener {
    private final String TITLE = "Edit nickname";
    private final int WIDTH = 200;
    private final int HEIGHT = 150;
    private final ClientGUI clientGUI;

    private final Label label=new Label("NEW NICKNAME:");
    private final TextField tfNickName=new TextField();
   private final JButton btnChange=new JButton("CHANGE");
    private final JButton btnCancel=new JButton("CANCEL");
    private final JPanel panel=new JPanel(new GridLayout(4,1));
    ChangingNicknameFrame(ClientGUI clientGUI){
        this.clientGUI=clientGUI;
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
        if(source==btnChange){

        }else if(source==btnCancel){
            clientGUI.setVisible(true);
            dispose();
        }else throw new RuntimeException("Unknown source: " + source);
    }
}
