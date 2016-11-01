package ru.mit.spbau.antonpp.bash.execution;

import org.junit.Test;
import ru.mit.spbau.antonpp.bash.cli.CommandInfo;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author antonpp
 * @since 02/11/2016
 */
public class CommandExecutorTest {

    @Test
    public void executePipedBuiltin() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOStreams io = new IOStreams(null, out, null);
        String text = "123";
        String expected = text + "\n";
        List<CommandInfo> infos = Collections.singletonList(new CommandInfo("echo", Collections.singletonList(text)));
        CommandExecutor.executePiped(infos, io, new Environment());
        String actual = new String(out.toByteArray(), Charset.defaultCharset());
        assertEquals(expected, actual);
    }

    @Test
    public void executePipedExternal() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOStreams io = new IOStreams(null, out, null);
        int rc = 123;

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL urlExecutable = classloader.getResource("test");
        String pathExecutable = urlExecutable.getFile();

        CommandInfo info = new CommandInfo(pathExecutable, Collections.singletonList(Integer.toString(rc)));
        List<CommandInfo> infos = Collections.singletonList(info);

        assertEquals(rc, CommandExecutor.executePiped(infos, io, new Environment()));

    }

}