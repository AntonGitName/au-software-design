package ru.mit.spbau.antonpp.bash.execution.builtin;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class Echo extends AbstractBuiltinExecutable {
    @Override
    public int execute(Environment env, List<String> args, IOStreams io) throws Exception {
        writeString(io, args.stream().collect(Collectors.joining(" ")));
        return 0;
    }
}
