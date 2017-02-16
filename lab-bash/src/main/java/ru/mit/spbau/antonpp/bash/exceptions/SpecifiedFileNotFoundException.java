package ru.mit.spbau.antonpp.bash.exceptions;

import ru.mit.spbau.antonpp.bash.execution.Executable;

/**
 * Most common error that can occur in any {@link Executable}.
 *
 * @author Anton Mordberg
 * @since 16.02.17
 */
public class SpecifiedFileNotFoundException extends CommandInvalidArgumentsException {
    public SpecifiedFileNotFoundException(String fname) {
        super(String.format("File `%s` was not found", fname));
    }
}
