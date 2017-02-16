package ru.mit.spbau.antonpp.bash.exceptions;

import ru.mit.spbau.antonpp.bash.execution.CommandExecutor;
import ru.mit.spbau.antonpp.bash.execution.Executable;

/**
 * Exception that must be thrown by {@link CommandExecutor} in case of any error during execution of {@link Executable}.
 *
 * @author antonpp
 * @see CommandExecutor
 * @see Executable
 * @since 01/11/2016
 */
public class CommandExecutionException extends Exception {
    public CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandExecutionException(String message) {
        super(message);
    }
}
