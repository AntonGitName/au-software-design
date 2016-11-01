package ru.mit.spbau.antonpp.bash.execution.builtin;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.TooManyArgumentsException;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.util.List;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class Exit extends AbstractBuiltinExecutable {
    @Override
    public int execute(Environment env, List<String> args, IOStreams io) throws Exception {
        if (args.size() > 0) {
            throw new TooManyArgumentsException(args.size(), 0);
        }
        writeString(io, env.getEnv("Bye!"));
        System.exit(0);
        return 0;
    }
}
