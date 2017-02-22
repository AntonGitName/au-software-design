package ru.mit.spbau.antonpp.bash.execution.builtin;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.util.List;

/**
 * This tool allows user to print current $PWD
 *
 * Usage: pwd
 *
 * @author antonpp
 * @since 01/11/2016
 */
public class Pwd extends AbstractBuiltinExecutable {
    public Pwd() {
        super(0);
    }

    @Override
    public int executeInternal(Environment env, List<String> args, IOStreams io) throws Exception {
        writeString(io, env.getEnv("pwd"));
        return 0;
    }
}
