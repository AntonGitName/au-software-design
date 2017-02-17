package ru.mit.spbau.antonpp.messanger.network;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Anton Mordberg
 * @since 17.02.17
 */
@Slf4j
public class MessageReceiver implements Closeable {

//    private static final int TIMEOUT = 30000;

    private final ExecutorService listeningService = Executors.newSingleThreadExecutor();
    private final ReceiverCallback callback;
    private final ServerSocket socket;

    public MessageReceiver(ReceiverCallback callback, int port) throws IOException {
        this.callback = callback;
        socket = new ServerSocket(port);
//        socket.setSoTimeout(TIMEOUT);

        listeningService.execute(() -> {
            while (true) {
                try {
                    onConnection(socket.accept());
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
        socket.close();
    }

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

    public interface ReceiverCallback {
        void onReceive(SignedMessage msg);
    }
}
