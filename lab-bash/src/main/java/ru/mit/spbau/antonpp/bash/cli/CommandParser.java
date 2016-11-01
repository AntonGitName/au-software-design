package ru.mit.spbau.antonpp.bash.cli;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.mit.spbau.antonpp.bash.exceptions.LineArgumentsParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static ru.mit.spbau.antonpp.bash.cli.Environment.*;

/**
 * @author antonpp
 * @since 31/10/2016
 */
@Slf4j
public class CommandParser {

    private static final String ARGUMENT_REGEX = String.format("(?:\\s*)((?:%s|%s|%s)+)",
            NO_SPECIAL, SINGLE_QUOTED_UNCAPTURED, DOUBLE_QOUTED_UNCAPTURED);
    private static final String COMMAND_REGEX = String.format("(?:\\s*)((?:%s|%s|%s)+)",
            NO_QUOTES_OR_PIPE, SINGLE_QUOTED_UNCAPTURED, DOUBLE_QOUTED_UNCAPTURED);

    private static final Pattern ARGUMENT_PATTERN = Pattern.compile(ARGUMENT_REGEX);
    private static final Pattern COMMAND_PATTERN = Pattern.compile(COMMAND_REGEX);

    @Getter
    private final List<CommandInfo> commandInfos = new ArrayList<>();

    public CommandParser(String input, Environment env) throws LineArgumentsParseException {
        log.debug("Input: `{}`", input);
        val matcher = COMMAND_PATTERN.matcher(input);
        while (matcher.find()) {
            val group = matcher.group(1);
            log.debug("Found command with arguments: `{}`", group);

            val split = splitARguments(group);
            if (split.isEmpty()) {
                throw new LineArgumentsParseException("Failed to parse command" + group);
            }
            val cmdArgs = new String[split.size() - 1];
            for (int j = 1; j < split.size(); j++) {
                cmdArgs[j - 1] = env.unquoteAndSubstitute(split.get(j));
            }
            val name = env.unquoteAndSubstitute(split.get(0));
            log.debug("Found command: `{}`", name);
            commandInfos.add(new CommandInfo(name, cmdArgs));
        }
        if (commandInfos.isEmpty()) {
            throw new LineArgumentsParseException("No commands found");
        }
    }

    private static List<String> splitARguments(String str) {
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
