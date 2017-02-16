package ru.mit.spbau.antonpp.bash.cli;

import lombok.Builder;
import lombok.Data;
import ru.mit.spbau.antonpp.bash.execution.CommandExecutor;

import java.util.List;

/**
 * Wrapper class that simplifies passing arguments from {@link CommandLineParser} to {@link CommandExecutor}.
 *
 * Used pattern: Builder
 *
 * @author antonpp
 * @since 31/10/2016
 */
@Builder
@Data
public class CommandInfo {
    private final String name;
    private final List<String> args;
}
