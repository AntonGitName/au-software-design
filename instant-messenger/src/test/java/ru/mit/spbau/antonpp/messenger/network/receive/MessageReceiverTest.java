package ru.mit.spbau.antonpp.messenger.network.receive;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import ru.mit.spbau.antonpp.messenger.network.data.SignedMessage;
import ru.mit.spbau.antonpp.messenger.network.receive.MessageReceiver.ReceiverCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @author Anton Mordberg
 * @since 17.02.17
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageReceiverTest {

    private static final SignedMessage TEST_MSG = SignedMessage.builder().sender("JESUS").text("CALL ME LATER").build();
    @Mock
    private Socket clientSocket;
    @Mock
    private ServerConnectionProvider provider;
    @Mock
    private ReceiverCallback callback;
    private MessageReceiver receiver;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void receive() throws Exception {
        Mockito.when(provider.accept())
                .thenReturn(clientSocket)  // return first connection immediately
                .then(invocation -> {
                    Thread.sleep(10000);
                    return null;
                });   // do not return second connection until the end of test when it will be interrupted by close()

        send(TEST_MSG);
        receiver = new MessageReceiver(callback, provider);
        Mockito.verify(clientSocket).getInputStream();
        Mockito.verify(callback).onReceive(Mockito.eq(TEST_MSG));
    }

    private void send(SignedMessage msg) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeUTF(msg.getSender());
            dos.writeUTF(msg.getText());
        }
        Mockito.when(clientSocket.getInputStream()).thenReturn(new ByteArrayInputStream(baos.toByteArray()));
    }

    @After
    public void tearDown() throws Exception {
        receiver.close();
    }

}