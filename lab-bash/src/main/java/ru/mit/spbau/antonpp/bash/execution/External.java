package ru.mit.spbau.antonpp.bash.execution;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.io.IOStreamsWrapper;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

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
    public int execute(Environment env, String[] args, IOStreamsWrapper io) throws Exception {
        log.debug("executing external executable: `{} {}`", path, Arrays.toString(args));
        val params = new ArrayList<String>();
        params.add(path.toString());
        Arrays.stream(args).forEach(params::add);
        val process = new ProcessBuilder(params)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectInput(ProcessBuilder.Redirect.INHERIT)
                .start();

        return process.waitFor();
    }
}
