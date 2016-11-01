package ru.mit.spbau.antonpp.bash.exceptions;

/**
 * @author antonpp
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
