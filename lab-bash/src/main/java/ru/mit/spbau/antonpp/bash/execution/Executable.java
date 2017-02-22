package ru.mit.spbau.antonpp.bash.execution;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.execution.builtin.AbstractBuiltinExecutable;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.util.List;

/**
 * This interface represents everything that can be executed with {@link CommandExecutor}. In fact all executables are
 * separated into two categories:
 * <ul>
 *     <li> built-in commands </li>
 *     <li> external executable files </li>
 * </ul>
 *
 * Pattern used: Command
 *
 * @see AbstractBuiltinExecutable
 * @see External
 * @author antonpp
 * @since 01/11/2016
 */
public interface Executable {

    int execute(Environment env, List<String> args, IOStreams io) throws Exception;
}
