package ru.mit.spbau.antonpp.messanger.network.send;

import ru.mit.spbau.antonpp.messanger.network.data.SignedMessage;

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
    private final ClientConnectionProvider provider;


    public MessageSender(SenderStatusCallback callback, ClientConnectionProvider provider) {
        this.provider = provider;
        statusService.scheduleAtFixedRate(() -> {
            if (checkConnection()) {
                callback.onConnected();
            } else {
                callback.onDisconnected();
            }
        }, 100, 1000, TimeUnit.MILLISECONDS);
    }


    boolean checkConnection() {
        try (Socket ignored = provider.newSocket()) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void send(SignedMessage msg) throws IOException {
        try (Socket socket = provider.newSocket();
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
