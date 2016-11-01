package ru.mit.spbau.antonpp.bash.execution.builtin;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.TooManyArgumentsException;
import ru.mit.spbau.antonpp.bash.execution.Executable;
import ru.mit.spbau.antonpp.bash.io.IOStreamsWrapper;

import java.nio.charset.Charset;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class Pwd implements Executable {
    @Override
    public int execute(Environment env, String[] args, IOStreamsWrapper io) throws Exception {
        if (args.length > 0) {
            throw new TooManyArgumentsException(args.length, 0);
        }
        io.getOut().write(env.getEnv("pwd").getBytes(Charset.defaultCharset()));
        return 0;
    }
}
