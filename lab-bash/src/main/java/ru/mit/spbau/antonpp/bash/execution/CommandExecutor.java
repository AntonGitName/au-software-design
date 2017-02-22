package ru.mit.spbau.antonpp.bash.execution;

import lombok.Setter;
import ru.mit.spbau.antonpp.bash.cli.CommandInfo;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.CommandExecutionException;
import ru.mit.spbau.antonpp.bash.exceptions.CommandInvalidArgumentsException;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.util.List;

/**
 * This class provides one public method to execute chained commands.
 *
 * Patterns used: Strategy, State
 *
 * @see Executable
 * @author antonpp
 * @since 01/11/2016
 */
public class CommandExecutor {

    @Setter
    private ExecutorStrategy strategy = ExecutorStrategy.PIPED;

    /**
     * Method to start chained execution of the commands. How exactly chain is controlled by current state which is
     * represented by strategy.
     *
     * Organizes piped input/output and prepares environment (e.g.adds variables $0, $1, ...).
     *
     * @param infos Information about commands that must be executed
     * @param io    input/output/error streams
     * @param env   environment with all variables that will available during the execution of commands
     * @return return code of the last executable in the chain
     * @throws CommandExecutionException in case if executable with specified name was not found or if any exception
     * is thrown during the execution of the commands.
     * @throws CommandInvalidArgumentsException in case user passed (real user, not coder) invalid arguments
     *
     * @see ExecutorStrategy#executeSingle(CommandInfo, IOStreams, Environment)
     */
    public int invoke(List<CommandInfo> infos, IOStreams io, Environment env)
            throws CommandExecutionException, CommandInvalidArgumentsException {
        return strategy.execute(infos, io, env);
    }
}
