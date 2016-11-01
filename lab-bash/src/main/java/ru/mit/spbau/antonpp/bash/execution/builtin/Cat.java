package ru.mit.spbau.antonpp.bash.execution.builtin;

import com.google.common.io.ByteStreams;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.TooManyArgumentsException;
import ru.mit.spbau.antonpp.bash.execution.Executable;
import ru.mit.spbau.antonpp.bash.io.IOStreamsWrapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class Cat implements Executable {
    @Override
    public int execute(Environment env, String[] args, IOStreamsWrapper io) throws Exception {
        if (args.length > 1) {
            throw new TooManyArgumentsException(args.length, 1);
        }
        if (args.length == 1) {
            try (InputStream in = new FileInputStream(args[0])) {
                cat(in, io.getOut());
            }
        } else {
            cat(io.getIn(), io.getOut());
        }
        return 0;
    }

    public void cat(InputStream in, OutputStream out) throws IOException {
        ByteStreams.copy(in, out);
    }
}
