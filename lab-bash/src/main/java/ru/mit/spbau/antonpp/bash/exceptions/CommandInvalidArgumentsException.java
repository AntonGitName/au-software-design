package ru.mit.spbau.antonpp.bash.exceptions;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class CommandInvalidArgumentsException extends Exception {

    public CommandInvalidArgumentsException(String message) {
        super(message);
    }
}
