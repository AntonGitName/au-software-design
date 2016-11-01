package ru.mit.spbau.antonpp.bash.execution.builtin;

import ru.mit.spbau.antonpp.bash.execution.Executable;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public abstract class AbstractBuiltinExecutable implements Executable {

    protected void writeString(OutputStream out, String s) throws IOException {
        out.write(String.format("%s%n", s).getBytes(Charset.defaultCharset()));
    }

    protected void writeString(IOStreams io, String s) throws IOException {
        writeString(io.getOut(), s);
    }
}
