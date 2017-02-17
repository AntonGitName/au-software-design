package ru.mit.spbau.antonpp.messanger.ui;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class represents a dialog that is shown when user wants to create a new connection.
 */
@Slf4j
public class ConnectDialog extends JDialog {

    private final ConnectCallback callback;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField yourPortTF;
    private JTextField hostnameTF;
    private JTextField friendPortTF;
    private JTextField nameTF;

    public ConnectDialog(ConnectCallback callback) {
        this.callback = callback;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {

        try {
            Integer port1 = Integer.valueOf(yourPortTF.getText());
            Integer port2 = Integer.valueOf(friendPortTF.getText());
            String hostname = hostnameTF.getText();
            String name = nameTF.getText();
            callback.onTryConnect(port1, port2, hostname, name);
            dispose();
        } catch (NumberFormatException e) {
            log.info("User tried invalid input", e);
            JOptionPane.showMessageDialog(this,
                    "Invalid port format",
                    "Input error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onCancel() {
        dispose();
    }


    /**
     * This callback is called only when the corresponding dialog has enough information to start a connection/
     */
    public interface ConnectCallback {
        void onTryConnect(int listenPort, int sendPort, String hostname, String name);
    }
}
