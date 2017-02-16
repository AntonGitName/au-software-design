package ru.mit.spbau.antonpp.bash.execution.builtin;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This echo -_-
 *
 * Usage: echo [STRING]...
 *
 * @author antonpp
 * @since 01/11/2016
 */
public class Echo extends AbstractBuiltinExecutable {
    public Echo() {
        super(Integer.MAX_VALUE);
    }

    @Override
    public int executeInternal(Environment env, List<String> args, IOStreams io) throws Exception {
        writeString(io, args.stream().collect(Collectors.joining(" ")));
        return 0;
    }
}
