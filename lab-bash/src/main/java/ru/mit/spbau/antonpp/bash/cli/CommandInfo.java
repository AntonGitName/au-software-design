package ru.mit.spbau.antonpp.bash.cli;

import lombok.Getter;
import lombok.ToString;
import ru.mit.spbau.antonpp.bash.execution.CommandExecutor;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class that simplifies passing arguments from {@link CommandLineParser} to {@link CommandExecutor}.
 *
 * @author antonpp
 * @since 31/10/2016
 */
@ToString
public class CommandInfo {

    @Getter
    private final String name;

    @Getter
    private final List<String> args;


    public CommandInfo(String name, List<String> args) {
        this.name = name;
        this.args = new ArrayList<>(args);
    }
}
