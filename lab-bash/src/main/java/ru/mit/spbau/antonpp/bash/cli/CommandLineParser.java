package ru.mit.spbau.antonpp.bash.cli;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.mit.spbau.antonpp.bash.exceptions.LineArgumentsParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.mit.spbau.antonpp.bash.cli.Environment.*;

/**
 * @author antonpp
 * @since 31/10/2016
 */
@Slf4j
public class CommandLineParser {

    private static final String ADD_ENV_REGEX = "^([a-zA-Z]\\w*)=(.*)$";
    private static final String ARGUMENT_REGEX = String.format("(?:\\s*)((?:%s|%s|%s)+)",
            NO_SPECIAL, SINGLE_QUOTED_UNCAPTURED, DOUBLE_QUOTED_UNCAPTURED);
    private static final String COMMAND_REGEX = String.format("\\s*((?:%s|%s|%s)+)",
            NO_QUOTES_OR_PIPE, SINGLE_QUOTED_UNCAPTURED, DOUBLE_QUOTED_UNCAPTURED);

    private static final Pattern ARGUMENT_PATTERN = Pattern.compile(ARGUMENT_REGEX);
    private static final Pattern ADD_ENV_PATTERN = Pattern.compile(ADD_ENV_REGEX);
    private static final Pattern COMMAND_PATTERN = Pattern.compile(COMMAND_REGEX);

    /**
     * Splits text into commands.
     *
     * @param input text to parse
     * @param env   environment with all available variables
     * @return list of parsed commands
     * @throws LineArgumentsParseException if line parsing failed
     */
    public static List<CommandInfo> parse(String input, Environment env) throws LineArgumentsParseException {
        final List<CommandInfo> infos = new ArrayList<>();
        log.debug("Input: `{}`", input);
        val matcher = COMMAND_PATTERN.matcher(input);
        while (matcher.find()) {
            val group = matcher.group(1);
            log.debug("Found command with arguments: `{}`", group);

            val split = splitArguments(group);
            if (split.isEmpty()) {
                throw new LineArgumentsParseException("Failed to parse command: " + group);
            }
            val cmdArgs = split.stream().skip(1).map(env::unquoteAndSubstitute).collect(Collectors.toList());
            val name = env.unquoteAndSubstitute(split.get(0));
            log.debug("Found command: `{}`", name);
            infos.add(new CommandInfo(name, cmdArgs));
        }
        if (infos.isEmpty()) {
            throw new LineArgumentsParseException("No commands found");
        }
        return infos;
    }

    /**
     * This method tests if a string defines a variable assignment. It also updates application environment if the
     * result is true.
     *
     * @param str a string to test
     * @return true if {@code str} is an assignment and false otherwise
     */
    public static boolean tryAddSubstitution(String str, Environment env) {
        val matcher = ADD_ENV_PATTERN.matcher(str);
        if (matcher.find()) {
            val key = matcher.group(1);
            val value = matcher.group(2);
            env.setEnv(key, env.unquoteAndSubstitute(value));
            return true;
        }
        return false;
    }

    private static List<String> splitArguments(String str) {
        val matcher = ARGUMENT_PATTERN.matcher(str);
        val result = new ArrayList<String>();
        while (matcher.find()) {
            val group = matcher.group(1);
            log.debug("Found argument: `{}`", group);
            result.add(group);
        }
        return result;
    }
}
