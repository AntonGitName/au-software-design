package ru.mit.spbau.antonpp.messenger.network.send;

import ru.mit.spbau.antonpp.messenger.network.data.SignedMessage;

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


    /**
     * @param callback a subscriber to our `health` checks that we do regularly
     * @param provider A supplier that can establish connection to another network peer
     */
    public MessageSender(SenderStatusCallback callback, ClientConnectionProvider provider) {
        this.provider = provider;
        statusService.scheduleAtFixedRate(() -> {
            if (checkConnection()) {
                callback.onPeerConnected();
            } else {
                callback.onPeerDisconnected();
            }
        }, 100, 1000, TimeUnit.MILLISECONDS);
    }


    /**
     * Checks if a new connection can be established. Pretty straight forward.
     *
     * @return true if it is possible to open a new client socket and false otherwise.
     */
    boolean checkConnection() {
        try (Socket ignored = provider.newSocket()) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Encodes message and sends it over the interface provided by {@link ClientConnectionProvider}.
     *
     * @param msg A message to send.
     * @throws IOException in case we failed to deliver the message.
     */
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

    /**
     * Since we are going to monitor our peer status, we need a way to notify our GUI about all changes. This class
     * represents a subscriber that can handle all of them.
     */
    public interface SenderStatusCallback {
        void onPeerConnected();

        void onPeerDisconnected();
    }
}
