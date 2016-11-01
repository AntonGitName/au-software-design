package ru.mit.spbau.antonpp.bash.exceptions;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class TooManyArgumentsException extends CommandInvalidArgumentsException {

    public TooManyArgumentsException(int provided, int expected) {
        super(String.format("Expected at most %s, but received %s", expected, provided));
    }
}
