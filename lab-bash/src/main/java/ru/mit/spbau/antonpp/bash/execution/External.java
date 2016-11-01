package ru.mit.spbau.antonpp.bash.execution;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents all executable files that can be called via command line of the application.
 *
 * @author antonpp
 * @since 01/11/2016
 */
@Slf4j
public class External implements Executable {

    private final Path path;

    public External(Path path) {
        this.path = path;
    }

    @Override
    public int execute(Environment env, List<String> args, IOStreams io) throws Exception {
        log.debug("executing external executable: `{} {}`", path, args);
        val params = new ArrayList<String>();
        params.add(path.toString());
        params.addAll(args);

        // No idea how to pipe external process anywhere except stdin/stdout
        val process = new ProcessBuilder(params)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectInput(ProcessBuilder.Redirect.INHERIT)
                .start();

        return process.waitFor();
    }
}
