package ru.mit.spbau.antonpp.bash;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.mit.spbau.antonpp.bash.cli.CommandParser;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.CommandExecutionException;
import ru.mit.spbau.antonpp.bash.exceptions.LineArgumentsParseException;
import ru.mit.spbau.antonpp.bash.execution.CommandExecutor;
import ru.mit.spbau.antonpp.bash.io.IOStreamsWrapper;

import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author antonpp
 * @since 31/10/2016
 */
@Slf4j
public class Application {

    private static final String ADD_ENV_REGEX = "^([a-zA-Z]\\w*)=(.*)$";
    private static final Pattern ADD_ENV_PATTERN = Pattern.compile(ADD_ENV_REGEX);

    private final Environment env = new Environment();
    private final IOStreamsWrapper io = new IOStreamsWrapper(System.in, System.out, System.err);

    private Application() {
    }

    public static void main(String[] args) throws LineArgumentsParseException {
        new Application().start();
    }

    private void start() {

        prepareEnv();

        try (Scanner scanner = new Scanner(System.in)) {

            while (true) {

                printPS();

                val line = scanner.nextLine();
                log.debug("User typed: {}", line);

                if (tryAddSubstitution(line)) {
                    log.debug("Updated env");
                } else {
                    try {
                        val infos = new CommandParser(line, env).getCommandInfos();
                        CommandExecutor.executePiped(infos, io, env);
                    } catch (LineArgumentsParseException e) {
                        val msg = "Failed to parse input";
                        System.err.println(msg);
                        log.warn(msg, e);
                    } catch (CommandExecutionException e) {
                        val msg = e.getMessage();
                        System.err.println(msg);
                        log.warn("Piped commands failed", e);
                    }
                }
            }
        }
    }

    private boolean tryAddSubstitution(String str) {
        val matcher = ADD_ENV_PATTERN.matcher(str);
        if (matcher.find()) {
            val key = matcher.group(1);
            val value = matcher.group(2);
            env.setEnv(key, env.unquoteAndSubstitute(value));
            return true;
        }
        return false;
    }

    private void prepareEnv() {
        env.setEnv("pwd", System.getProperty("user.dir"));
        env.setEnv("user", System.getProperty("user.name"));
        env.setEnv("home", System.getProperty("user.home"));
    }

    private void printPS() {
        val user = env.getEnv("user");
        val path = Paths.get(env.getEnv("home")).relativize(Paths.get(env.getEnv("pwd")));
        System.out.printf("%s@~/%s$ ", user, path);
    }
}
