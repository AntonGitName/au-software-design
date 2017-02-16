package ru.mit.spbau.antonpp.bash.exceptions;

import ru.mit.spbau.antonpp.bash.execution.Executable;

/**
 * Most common error that can occur in any {@link Executable}.
 *
 * @author antonpp
 * @see CommandInvalidArgumentsException
 * @since 01/11/2016
 */
public class TooManyArgumentsException extends CommandInvalidArgumentsException {

    public TooManyArgumentsException(int provided, int expected) {
        super(String.format("Expected at most %s argument, but received %s", expected, provided));
    }
}
