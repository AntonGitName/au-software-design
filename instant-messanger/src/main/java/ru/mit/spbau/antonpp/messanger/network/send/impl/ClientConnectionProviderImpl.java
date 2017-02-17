package ru.mit.spbau.antonpp.messanger.network.send.impl;

import ru.mit.spbau.antonpp.messanger.network.send.ClientConnectionProvider;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Anton Mordberg
 * @since 17.02.17
 */
public class ClientConnectionProviderImpl implements ClientConnectionProvider {

    private final String host;
    private final int port;

    public ClientConnectionProviderImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Socket newSocket() throws IOException {
        return new Socket(host, port);
    }
}
