package ru.mit.spbau.antonpp.messanger.network.receive;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Anton Mordberg
 * @since 17.02.17
 */
public interface ServerConnectionProvider extends Closeable {
    Socket accept() throws IOException;
}
