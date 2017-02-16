package ru.mit.spbau.antonpp.bash.exceptions;

import ru.mit.spbau.antonpp.bash.execution.Executable;

/**
 * Exception that must be thrown by {@link Executable} in case of invalid arguments.
 *
 * @author antonpp
 * @since 01/11/2016
 */
public class CommandInvalidArgumentsException extends Exception {

    public CommandInvalidArgumentsException(String message) {
        super(message);
    }
}
