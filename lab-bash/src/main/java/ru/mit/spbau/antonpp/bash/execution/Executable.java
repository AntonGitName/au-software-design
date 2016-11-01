package ru.mit.spbau.antonpp.bash.execution;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.io.IOStreamsWrapper;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public interface Executable {

    int execute(Environment env, String[] args, IOStreamsWrapper io) throws Exception;
}
