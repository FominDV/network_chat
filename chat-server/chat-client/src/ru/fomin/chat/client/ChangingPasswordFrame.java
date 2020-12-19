package ru.fomin.chat.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChangingPasswordFrame extends JFrame implements ActionListener {
    private static final String TITLE = "Edit password";
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    private  final ClientGUI clientGUI;
    private static final String MESSAGE_SUCCESSFULLY_CHANGING_PASSWORD="Password was changed\n";
    private final JPasswordField[] fields=new JPasswordField[3];
    private final JLabel[] labelsForFields=new JLabel[fields.length];
    private final JPanel panelWithFields=new JPanel(new GridLayout(3,2));
    private final JPanel panelBottom=new JPanel(new GridLayout(2,1));
    private final JButton btnChange = new JButton("CHANGE");
    private final JButton btnCancel = new JButton("CANCEL");

    public ChangingPasswordFrame(ClientGUI clientGUI){
        this.clientGUI=clientGUI;
        SwingUtilities.invokeLater(() -> initialization());
    }

    private void initialization() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle(TITLE);
        setResizable(false);
        btnChange.addActionListener(this);
        btnCancel.addActionListener(this);
        btnCancel.setBackground(Color.RED);
        labelsForFields[0]=new JLabel("Insert password");
        labelsForFields[1]=new JLabel("Insert new password");
        labelsForFields[2]=new JLabel("Repeat new password");
        for(int i=0;i<fields.length;i++){
            fields[i]=new JPasswordField();
            panelWithFields.add(labelsForFields[i]);
            panelWithFields.add(fields[i]);
        }
        panelBottom.add(btnChange);
        panelBottom.add(btnCancel);
        add(panelWithFields,BorderLayout.CENTER);
        add(panelBottom,BorderLayout.SOUTH);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source =e.getSource();
        if(source==btnChange){
            String newPassword=new String(fields[1].getPassword()), password=new String(fields[0].getPassword());
            if(isValidCompletedFields(password,newPassword,new String(fields[2].getPassword())))
            clientGUI.sendChangingPasswordMessage(password,newPassword); else clearAllFields();
        }else if(source==btnCancel){
            cancel();
        }else throw new RuntimeException("Unknown source: " + source);
    }
  private boolean isValidCompletedFields(String password,String newPassword, String repeatedPassword){
        if(password.equals("")||newPassword.equals("")||repeatedPassword.equals("")){
            showErrorMessage("All field should be filled");
            return false;
        }
        if(!newPassword.equals(repeatedPassword)){
            showErrorMessage("Repeated password is not equal to new password");
            return false;
        }
        return true;
   }
   private void showErrorMessage(String message){
        JOptionPane.showMessageDialog(null,message,"ERROR",JOptionPane.ERROR_MESSAGE);
   }
   private void clearAllFields(){
        for(JPasswordField field: fields) field.setText("");
   }
   public void changingSuccessful(){
        JOptionPane.showMessageDialog(null,MESSAGE_SUCCESSFULLY_CHANGING_PASSWORD);
        cancel();
   }
    public void changingFailed(){
        showErrorMessage("You inserted wrong password\nor something else went wrong by server");
        clearAllFields();
    }
    private void cancel(){
        clientGUI.setVisible(true);
        dispose();
    }
}
