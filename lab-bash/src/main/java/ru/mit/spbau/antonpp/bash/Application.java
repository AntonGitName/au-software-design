package ru.mit.spbau.antonpp.bash;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.mit.spbau.antonpp.bash.cli.CommandLineParser;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.CommandExecutionException;
import ru.mit.spbau.antonpp.bash.exceptions.CommandInvalidArgumentsException;
import ru.mit.spbau.antonpp.bash.exceptions.LineArgumentsParseException;
import ru.mit.spbau.antonpp.bash.execution.CommandExecutor;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.nio.file.Paths;
import java.util.Scanner;

/**
 * This class is an entry point of the application. It has a looping mechanism that handles user input.
 *
 * @author antonpp
 * @since 31/10/2016
 */
@Slf4j
public class Application {

    private final Environment env = new Environment();
    private final IOStreams io = new IOStreams(System.in, System.out, System.err);

    private Application() {
    }

    public static void main(String[] args) throws LineArgumentsParseException {
        new Application().start();
    }

    /**
     * This method has code that handles user input. User input can be of two types:
     * <ul>
     * <li> variable assignment </li>
     * <li> command call (possibly chained via pipe) </li>
     * </ul>
     * Code of this method has an infinite loop that reads and handles input. The only way to break the loop is to call
     * `exit`.
     */
    private void start() {

        prepareEnv();

        try (Scanner scanner = new Scanner(System.in)) {

            while (true) {

                printPS();

                val line = scanner.nextLine();
                log.debug("User typed: {}", line);

                if (line.isEmpty()) {
                    continue;
                }

                if (CommandLineParser.tryAddSubstitution(line, env)) {
                    log.debug("Updated env");
                } else {
                    try {
                        val infos = CommandLineParser.parse(line, env);
                        CommandExecutor.executePiped(infos, io, env);
                    } catch (LineArgumentsParseException e) {
                        val msg = "Failed to parse input";
                        System.err.println(msg);
                        log.warn(msg, e);
                    } catch (CommandExecutionException e) {
                        val msg = e.getMessage();
                        System.err.println(msg);
                        log.warn("Piped commands failed", e);
                    } catch (CommandInvalidArgumentsException e) {
                        System.err.println(e.getMessage());
                    } finally {
                        System.err.flush();
                    }
                }
            }
        }
    }

    /**
     * Updates application environment with some basic properties
     */
    private void prepareEnv() {
        env.setEnv("pwd", System.getProperty("user.dir"));
        env.setEnv("user", System.getProperty("user.name"));
        env.setEnv("home", System.getProperty("user.home"));
    }

    /**
     * Prints console prompt
     */
    private void printPS() {
        val user = env.getEnv("user");
        val path = Paths.get(env.getEnv("home")).relativize(Paths.get(env.getEnv("pwd")));
        System.out.printf("%s@~/%s$ ", user, path);
        System.out.flush();
    }
}
