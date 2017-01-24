package ru.mit.spbau.antonpp.bash.execution.builtin;

import com.google.common.collect.Range;
import com.google.common.io.ByteStreams;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.io.IOStreams;
import se.softhouse.jargo.Argument;
import se.softhouse.jargo.ArgumentException;
import se.softhouse.jargo.CommandLineParser;
import se.softhouse.jargo.ParsedArguments;

import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static se.softhouse.jargo.Arguments.*;

/**
 * @author Anton Mordberg
 * @since 23.01.17
 */
public class Grep extends AbstractBuiltinExecutable {

    //    https://en.wikipedia.org/wiki/ANSI_escape_code
    static final String RESET_COLOR = "\u001B[0m";
    static final String RED_COLOR = "\u001B[31m";

    private static final String EXECUTABLE_NAME = "grep";

    private static final Argument<?> HELP_ARGUMENT = helpArgument("-h", "--help");

    private static final Argument<Boolean> CASE_INSENSITIVE_ARGUMENT = optionArgument("-i")
            .description("Ignore case distinctions in both the PATTERN and the input files.")
            .defaultValue(false).build();

    private static final Argument<Integer> LINES_AFTER_ARGUMENT = integerArgument("-A")
            .description("Print NUM lines of trailing context after matching lines.")
            .defaultValue(0)
            .limitTo(Range.closed(0, Integer.MAX_VALUE)).build();

    private static final Argument<Boolean> WORDS_ONLY_ARGUMENT = optionArgument("-w")
            .description("Select only those lines containing matches that form whole words.").defaultValue(false).build();

    private static final Argument<Boolean> USE_REGEX_ARGUMENT = optionArgument("-e")
            .description("Use PATTERN as the pattern for the regex search.").defaultValue(false).build();

    private static final Argument<String> PATTERN_ARGUMENT = stringArgument()
            .description("A regular expression that is searched.").required().build();

    @Override
    public int execute(Environment env, List<String> args, IOStreams io) throws Exception {
        final ParsedArguments arguments;
        try {
            arguments = CommandLineParser
                    .withArguments(CASE_INSENSITIVE_ARGUMENT, LINES_AFTER_ARGUMENT, WORDS_ONLY_ARGUMENT, USE_REGEX_ARGUMENT)
                    .andArguments(PATTERN_ARGUMENT, HELP_ARGUMENT)
                    .programName(EXECUTABLE_NAME)
                    .parse(args);
        } catch (ArgumentException e) {
            io.getErr().write(e.getMessageAndUsage().toString().getBytes());
            io.getErr().flush();
            throw e;
        }

        final GrepState state = new GrepState();

        final Integer tmp = arguments.get(LINES_AFTER_ARGUMENT);
        state.maxLinesToPrint = tmp != null ? tmp : 1;
        String query = "" + arguments.get(PATTERN_ARGUMENT);

        final Boolean useRegex = arguments.get(USE_REGEX_ARGUMENT);
        if (useRegex == null || !useRegex) {
            query = Pattern.quote(query);
        }

        final Boolean onlyWords = arguments.get(WORDS_ONLY_ARGUMENT);
        if (onlyWords != null && onlyWords) {
            query = "\\b" + query + "\\b";
        }

        final Boolean caseInsensitive = arguments.get(CASE_INSENSITIVE_ARGUMENT);
        if (caseInsensitive != null && caseInsensitive) {
            query = "(?i)" + query;
        }

        state.pattern = Pattern.compile(query);

        grep(state, io.getIn(), io.getOut());

        return 0;
    }

    private void grep(GrepState state, InputStream in, OutputStream out) throws IOException {
        try(Scanner scanner = new Scanner(in)) {
            state.linesToPrint = 0;
            while (scanner.hasNext()) {
                String highlightedString = highlightMatch(state, scanner.nextLine());
                if (state.linesToPrint > 0) {
                    out.write(highlightedString.getBytes());
                    --state.linesToPrint;
                }
            }
        }
    }

    private String highlightMatch(GrepState state, String line) {
        final Matcher matcher = state.pattern.matcher(line);
        String result = "";
        int start = 0;
        while (matcher.find()) {
            state.linesToPrint = state.maxLinesToPrint + 1;
            result += line.substring(start, matcher.start());
            result += RED_COLOR + matcher.group() + RESET_COLOR;
            start = matcher.end();
        }
        return result + line.substring(start) + "\n";
    }

    private static final class GrepState {
        private int linesToPrint;
        private int maxLinesToPrint;
        private Pattern pattern;
    }


}
