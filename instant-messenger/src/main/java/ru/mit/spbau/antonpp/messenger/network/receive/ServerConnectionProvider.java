package ru.mit.spbau.antonpp.messenger.network.receive;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * This interface describes what API is needed by {@link MessageReceiver} from java.net. The decision of splitting
 * receiver and socket was made because of tests as we need low coupling in order to mock network part of the code.
 *
 * @author Anton Mordberg
 * @since 17.02.17
 */
public interface ServerConnectionProvider extends Closeable {
    Socket accept() throws IOException;
}
