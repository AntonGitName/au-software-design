package ru.mit.spbau.antonpp.messanger.network.send;

import java.io.IOException;
import java.net.Socket;

/**
 * @author Anton Mordberg
 * @since 17.02.17
 */
public interface ClientConnectionProvider {
    Socket newSocket() throws IOException;
}
