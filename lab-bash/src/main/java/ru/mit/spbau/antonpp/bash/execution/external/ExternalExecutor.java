package ru.mit.spbau.antonpp.bash.execution.external;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents all executable files that can be called via command line of the application.
 *
 * @author Anton Mordberg
 * @since 17.02.17
 */
@Slf4j
class ExternalExecutor {

    private final Path path;

    public ExternalExecutor(Path path) {
        this.path = path;
    }

    public int run(List<String> args) throws Exception {
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
