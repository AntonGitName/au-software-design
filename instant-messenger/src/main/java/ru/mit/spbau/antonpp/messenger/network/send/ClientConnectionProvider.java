package ru.mit.spbau.antonpp.messenger.network.send;

import java.io.IOException;
import java.net.Socket;

/**
 * This interface describes what API is needed by {@link MessageSender} from java.net. The decision of splitting
 * sender and socket was made because of unit tests as we need low coupling in order to mock network part of the code.
 *
 * @author Anton Mordberg
 * @since 17.02.17
 */
public interface ClientConnectionProvider {
    Socket newSocket() throws IOException;
}
