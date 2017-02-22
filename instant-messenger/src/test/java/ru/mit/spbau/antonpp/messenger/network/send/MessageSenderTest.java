package ru.mit.spbau.antonpp.messenger.network.send;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import ru.mit.spbau.antonpp.messenger.network.data.SignedMessage;
import ru.mit.spbau.antonpp.messenger.network.send.MessageSender.SenderStatusCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;

/**
 * @author Anton Mordberg
 * @since 17.02.17
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageSenderTest {

    private static final SignedMessage TEST_MSG = SignedMessage.builder().sender("JESUS").text("CALL ME LATER").build();
    @Mock
    private Socket socket;
    @Mock
    private ClientConnectionProvider provider;
    @Mock
    private SenderStatusCallback callback;
    private MessageSender sender;
    private ByteArrayOutputStream baos;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        baos = new ByteArrayOutputStream();
        sender = new MessageSender(callback, provider);
        Mockito.when(provider.newSocket()).thenReturn(socket);
        Mockito.when(socket.getOutputStream()).thenReturn(baos);
    }

    @After
    public void tearDown() throws Exception {
        sender.close();
    }

    @Test
    public void send() throws Exception {
        final int n = 10;
        for (int i = 0; i < n; ++i) {
            sender.send(TEST_MSG);
            assertEquals(TEST_MSG, readResult());
        }
        Mockito.verify(socket, Mockito.times(n)).getOutputStream();
    }

    @Test
    public void checkConnectionOn() throws Exception {
        assertTrue(sender.checkConnection());
    }

    @Test
    public void checkConnectionOff() throws Exception {
        Mockito.when(provider.newSocket()).thenThrow(IOException.class);
        assertFalse(sender.checkConnection());
    }

    private SignedMessage readResult() throws IOException {
        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            return SignedMessage.builder().sender(dis.readUTF()).text(dis.readUTF()).build();
        }
    }


}