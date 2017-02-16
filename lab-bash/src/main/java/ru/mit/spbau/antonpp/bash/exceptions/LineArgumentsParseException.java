package ru.mit.spbau.antonpp.bash.exceptions;

import ru.mit.spbau.antonpp.bash.cli.CommandLineParser;

/**
 * Exception that must be used by {@link CommandLineParser} in case of unexpected input.
 *
 * @author antonpp
 * @since 01/11/2016
 */
public class LineArgumentsParseException extends Exception {
    public LineArgumentsParseException(String message) {
        super(message);
    }
}
