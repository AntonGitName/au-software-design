package ru.mit.spbau.antonpp.bash.execution.external;

import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.execution.Executable;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.nio.file.Path;
import java.util.List;

/**
 * This class adapts ExternalExecutor class to Executable interface
 * <p>
 * Patterns used: Adapter
 *
 * @author Anton Mordberg
 * @since 17.02.17
 */
public class ExternalExecutorAdapter extends ExternalExecutor implements Executable {
    public ExternalExecutorAdapter(Path path) {
        super(path);
    }

    @Override
    public int execute(Environment env, List<String> args, IOStreams io) throws Exception {
        return run(args);
    }
}
