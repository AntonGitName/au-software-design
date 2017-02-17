package ru.mit.spbau.antonpp.messenger.network.receive.impl;

import ru.mit.spbau.antonpp.messenger.network.receive.ServerConnectionProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class is an adapter between {@link ServerConnectionProvider} and {@link ServerSocket}
 *
 * @author Anton Mordberg
 * @since 17.02.17
 */
public class ServerConnectionProviderImpl implements ServerConnectionProvider {

    private final ServerSocket socket;

    public ServerConnectionProviderImpl(int port) throws IOException {
        socket = new ServerSocket(port);
    }

    @Override
    public Socket accept() throws IOException {
        return socket.accept();
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
