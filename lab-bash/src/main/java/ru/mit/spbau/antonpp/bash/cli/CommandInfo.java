package ru.mit.spbau.antonpp.bash.cli;

import lombok.Getter;
import lombok.ToString;

/**
 * Wrapper class that simplifies passing arguments from {@link CommandLineParser} to
 * {@link ru.mit.spbau.antonpp.bash.execution.CommandExecutor}.
 *
 * @author antonpp
 * @since 31/10/2016
 */
@ToString
public class CommandInfo {

    @Getter
    private final String name;

    @Getter
    private final String[] args;


    public CommandInfo(String name, String[] args) {
        this.name = name;
        this.args = args;
    }
}
