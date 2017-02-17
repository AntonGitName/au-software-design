package ru.mit.spbau.antonpp.messenger.network.receive;

import lombok.extern.slf4j.Slf4j;
import ru.mit.spbau.antonpp.messenger.network.data.SignedMessage;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that is responsible for receiving messages.
 *
 * @author Anton Mordberg
 * @since 17.02.17
 */
@Slf4j
public class MessageReceiver implements Closeable {

    private final ExecutorService listeningService = Executors.newSingleThreadExecutor();
    private final ReceiverCallback callback;
    private final ServerConnectionProvider provider;

    /**
     * @param callback A subscriber to all new messages.
     * @param provider Network provider that will supply @this with new connections.
     */
    public MessageReceiver(ReceiverCallback callback, ServerConnectionProvider provider) {
        this.callback = callback;
        this.provider = provider;

        listeningService.execute(() -> {
            while (true) {
                try {
                    onConnection(provider.accept());
                } catch (IOException e) {
                    log.warn("Failed to accept new connection", e);
                    break;
                }
            }
        });
    }

    @Override
    public void close() throws IOException {
        listeningService.shutdownNow();
        provider.close();
    }

    /**
     * A method that receives new socket then decodes data and notifies subscriber.
     *
     * @param clientSocket socket from a new connection.
     */
    private void onConnection(Socket clientSocket) {
        try {
            final DataInputStream dos = new DataInputStream(clientSocket.getInputStream());
            callback.onReceive(SignedMessage.builder()
                    .sender(dos.readUTF())
                    .text(dos.readUTF())
                    .build());
        } catch (IOException e) {
            log.warn("Connection failed", e);
        }
    }

    /**
     * A callback to notify subscriber when a new message arrives.
     */
    public interface ReceiverCallback {
        void onReceive(SignedMessage msg);
    }
}
