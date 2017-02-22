package ru.mit.spbau.antonpp.bash.execution.builtin;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.TooManyArgumentsException;
import ru.mit.spbau.antonpp.bash.execution.Executable;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * This class extends {@link Executable} interface with commonly used util methods.
 *
 * Pattern used: template method
 *
 * @author antonpp
 * @see Executable
 * @since 01/11/2016
 */
public abstract class AbstractBuiltinExecutable implements Executable {

    private final int maxArguments;

    protected AbstractBuiltinExecutable(int maxArguments) {
        this.maxArguments = maxArguments;
    }

    @Override
    public final int execute(Environment env, List<String> args, IOStreams io) throws Exception {
        if (args.size() > maxArguments) {
            throw new TooManyArgumentsException(args.size(), maxArguments);
        }
        return executeInternal(env, args, io);
    }

    protected abstract int executeInternal(Environment env, List<String> args, IOStreams io) throws Exception;

    protected void writeString(OutputStream out, String s) throws IOException {
        out.write(String.format("%s%n", s).getBytes(Charset.defaultCharset()));
    }

    protected void writeString(IOStreams io, String s) throws IOException {
        writeString(io.getOut(), s);
    }
}
