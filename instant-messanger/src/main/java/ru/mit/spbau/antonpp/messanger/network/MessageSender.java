package ru.mit.spbau.antonpp.messanger.network;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for sending messages and checking connection status.
 *
 * @author Anton Mordberg
 * @since 17.02.17
 */
public class MessageSender implements Closeable {

    private final ScheduledExecutorService statusService = Executors.newSingleThreadScheduledExecutor();

    private final String host;
    private final int port;

    public MessageSender(SenderStatusCallback callback, String host, int port) {
        this.host = host;
        this.port = port;
        statusService.scheduleAtFixedRate(() -> {
            if (checkConnection()) {
                callback.onConnected();
            } else {
                callback.onDisconnected();
            }
        }, 100, 1000, TimeUnit.MILLISECONDS);
    }


    private boolean checkConnection() {
        try (Socket ignored = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void send(SignedMessage msg) throws IOException {
        try (Socket socket = new Socket(host, port);
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {
            dos.writeUTF(msg.getSender());
            dos.writeUTF(msg.getText());
        }
    }

    @Override
    public void close() throws IOException {
        statusService.shutdownNow();
    }

    public interface SenderStatusCallback {
        void onConnected();

        void onDisconnected();
    }
}
