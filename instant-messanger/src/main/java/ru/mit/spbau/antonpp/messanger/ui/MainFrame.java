package ru.mit.spbau.antonpp.messanger.ui;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import ru.mit.spbau.antonpp.messanger.network.data.SignedMessage;
import ru.mit.spbau.antonpp.messanger.network.receive.MessageReceiver;
import ru.mit.spbau.antonpp.messanger.network.receive.impl.ServerConnectionProviderImpl;
import ru.mit.spbau.antonpp.messanger.network.send.MessageSender;
import ru.mit.spbau.antonpp.messanger.network.send.impl.ClientConnectionProviderImpl;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * This class implements all business logic and defines all ui elements of the app.
 *
 * @author Anton Mordberg
 * @since 17.02.17
 */
@Slf4j
public class MainFrame extends JFrame
        implements ConnectDialog.ConnectCallback, MessageReceiver.ReceiverCallback, MessageSender.SenderStatusCallback {

    private JPanel panel1;
    private JTextArea dialogArea;
    private JEditorPane messagePane;
    private JButton sendButton;
    private JButton disconnectButton;
    private JButton connectButton;
    private JScrollPane scrollPane;
    private JTextArea statusArea;

    private boolean isConnected = false;
    private boolean isFriendConnected = false;

    @Nullable
    private MessageSender sender;
    @Nullable
    private MessageReceiver receiver;
    @Nullable
    private String name;

    public MainFrame() {
        super("Mors™ Chat [closed beta]");

        setBounds(100, 100, 640, 480);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(panel1);

        connectButton.addActionListener(e -> {
            ConnectDialog dialog = new ConnectDialog(MainFrame.this);
            dialog.pack();
            dialog.setVisible(true);
        });

        disconnectButton.addActionListener(e -> {
            onDisconnect();
        });

        sendButton.addActionListener(e -> {
            onSend();
        });

        messagePane.registerKeyboardAction(e -> onSend(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_FOCUSED);
        messagePane.registerKeyboardAction(e -> onSend(), KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        updateButtons();

        setResizable(false);
        setVisible(true);

        Timer timer = new Timer(0, a -> printStatus());
        timer.setDelay(100);
        timer.start();
    }

    private void onSend() {
        if (sender != null && isConnected) {
            try {
                SignedMessage message = SignedMessage.builder().sender(name).text(messagePane.getText()).build();
                onReceive(message);
                sender.send(message);
                messagePane.setText("");
            } catch (IOException e) {
                showError(e, "Could not send message", false);
            }
        } else {
            log.error("Invariant is broken: message is sent after disconnect");
        }
    }

    private void onDisconnect() {
        try {
            if (receiver != null) {
                receiver.close();
            }

            if (sender != null) {
                sender.close();
            }
        } catch (IOException e) {
            showError(e, "Could not disconnect correctly", false);
        } finally {
            isConnected = false;
            isFriendConnected = false;
            sender = null;
            receiver = null;
            updateButtons();
        }
    }

    @Override
    public void onTryConnect(int listenPort, int sendPort, String hostname, String name) {
        sender = new MessageSender(this, new ClientConnectionProviderImpl(hostname, sendPort));
        this.name = name;
        try {
            receiver = new MessageReceiver(this, new ServerConnectionProviderImpl(listenPort));
            dialogArea.setText("");
            isConnected = true;
            messagePane.requestFocus();
        } catch (IOException e) {
            try {
                sender.close();
            } catch (IOException e1) {
                showError(e1, "Could not close correctly sender after receiver failed to start", false);
            }
            sender = null;
            receiver = null;
            isConnected = false;
            showError(e, "Could not connect to specified peer", true);
        }
        updateButtons();
        isFriendConnected = false;
    }

    private void updateButtons() {
        connectButton.setEnabled(!isConnected);
        disconnectButton.setEnabled(isConnected);
        sendButton.setEnabled(isConnected);
        messagePane.setEditable(isConnected);
    }

    @Override
    public void onReceive(SignedMessage msg) {
        String str = String.format("[%s]: %s%n%n", msg.getSender(), msg.getText());
        dialogArea.append(str);
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }

    private void showError(Throwable e, String message, boolean needDialog) {
        log.error(message, e);
        onReceive(SignedMessage.builder().sender("SYS").text(message).build());
        if (needDialog) {
            JOptionPane.showMessageDialog(this,
                    message,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printSysMessage(String msg) {
        onReceive(SignedMessage.builder().sender("SYS").text(msg).build());
    }

    @Override
    public void onConnected() {
        if (!isFriendConnected && isConnected) {
            printSysMessage("***User Connected***");
        }
        isFriendConnected = true;
    }

    @Override
    public void onDisconnected() {
        if (isFriendConnected) {
            printSysMessage("***User Disconnected***");
        }
        isFriendConnected = false;
    }

    private void printStatus() {
        String status = String.format("Listening: %s;\tPeer Connected: %s;\tName: %s", isConnected, isFriendConnected, name);
        statusArea.setText(status);
    }
}
