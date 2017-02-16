package ru.mit.spbau.antonpp.bash.execution.builtin;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.util.List;

/**
 * This exit. Use it to exit with 0 rc.
 *
 * Usage: exit
 *
 * @author antonpp
 * @since 01/11/2016
 */
public class Exit extends AbstractBuiltinExecutable {
    public Exit() {
        super(0);
    }

    @Override
    public int executeInternal(Environment env, List<String> args, IOStreams io) throws Exception {
        writeString(io, env.getEnv("Bye!"));
        System.exit(0);
        return 0;
    }
}
