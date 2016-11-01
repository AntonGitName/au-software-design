package ru.mit.spbau.antonpp.bash.execution;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.io.IOStreamsWrapper;

/**
 * This interface represents everything that can be executed with {@link CommandExecutor}. In fact all executables are
 * separated into two categories:
 * <ul>
 *     <li> built-in commands </li>
 *     <li> external executable files </li>
 * </ul>
 *
 * @see External
 * @author antonpp
 * @since 01/11/2016
 */
public interface Executable {

    int execute(Environment env, String[] args, IOStreamsWrapper io) throws Exception;
}
