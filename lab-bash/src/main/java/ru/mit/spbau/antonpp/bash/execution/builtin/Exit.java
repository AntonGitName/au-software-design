package ru.mit.spbau.antonpp.bash.execution.builtin;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.TooManyArgumentsException;
import ru.mit.spbau.antonpp.bash.execution.Executable;
import ru.mit.spbau.antonpp.bash.io.IOStreamsWrapper;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class Exit implements Executable {
    @Override
    public int execute(Environment env, String[] args, IOStreamsWrapper io) throws Exception {
        if (args.length > 0) {
            throw new TooManyArgumentsException(args.length, 0);
        }
        System.exit(0);
        return 0;
    }
}
