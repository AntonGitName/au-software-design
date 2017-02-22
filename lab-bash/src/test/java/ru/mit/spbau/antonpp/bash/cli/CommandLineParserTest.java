package ru.mit.spbau.antonpp.bash.cli;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author antonpp
 * @since 02/11/2016
 */
public class CommandLineParserTest {

    private static final String COMMAND = "CMD";
    private static final String ARG1 = "arg1";
    private static final String ARG2 = "'arg2'";
    private static final String ARG3 = "\"arg3\"";
    private static final String ARG4 = "\"'arg4'\"";
    private static final String ARG5 = "\"   'arg5'    \"";
    private static final String ARG6 = "'  \"arg6  \"  '";
    private static final String ARG7 = "'  \"$x\"  '";
    private static final String ARG8 = "\"   '$y'    \"";
    private static final String[] ARGS = {ARG1, ARG2, ARG3, ARG4, ARG5, ARG6, ARG7, ARG8};
    private static final String ARG1_UNQUOTED = "arg1";
    private static final String ARG2_UNQUOTED = "arg2";
    private static final String ARG3_UNQUOTED = "arg3";
    private static final String ARG4_UNQUOTED = "'arg4'";
    private static final String ARG5_UNQUOTED = "   'arg5'    ";
    private static final String ARG6_UNQUOTED = "  \"arg6  \"  ";
    private static final String ARG7_UNQUOTED = "  \"$x\"  ";
    private static final String ARG8_UNQUOTED = "   'arg8'    ";
    private static final String[] ARGS_UNQUOTED = {ARG1_UNQUOTED, ARG2_UNQUOTED, ARG3_UNQUOTED, ARG4_UNQUOTED,
            ARG5_UNQUOTED, ARG6_UNQUOTED, ARG7_UNQUOTED, ARG8_UNQUOTED};
    private static final String VAR1_KEY = "x";
    private static final String VAR2_KEY = "y";
    private static final String VAR1_VALUE = "arg7";
    private static final String VAR2_VALUE = "arg8";
    private Environment env = new Environment();

    @Before
    public void setUp() throws Exception {
        env.setEnv(VAR1_KEY, VAR1_VALUE);
        env.setEnv(VAR2_KEY, VAR2_VALUE);
    }

    @Test
    public void tryAddSubstitution() throws Exception {
        CommandLineParser.tryAddSubstitution("abc=y", env);
        assertEquals("y", env.getEnv("abc"));
        CommandLineParser.tryAddSubstitution("abc=z", env);
        assertEquals("z", env.getEnv("abc"));

        for (int i = 0; i < ARGS.length; i++) {
            CommandLineParser.tryAddSubstitution(String.format("test_var=%s", ARGS[i]), env);
            assertEquals(ARGS_UNQUOTED[i], env.getEnv("test_var"));
        }


    }

    @Test
    public void parse() throws Exception {
        for (int i = 0; i < ARGS.length; i++) {
            String text = String.format("%s %s", COMMAND, ARGS[i]);
            CommandInfo info = CommandInfo.builder()
                    .name(COMMAND).args(Collections.singletonList(ARGS_UNQUOTED[i])).build();
            List<CommandInfo> expected = Collections.singletonList(info);
            List<CommandInfo> actual = CommandLineParser.parse(text, env);
            for (int j = 0; j < expected.size(); j++) {
                assertThat(actual.get(j).getArgs(), is(expected.get(j).getArgs()));
                assertThat(actual.get(j).getName(), is(expected.get(j).getName()));
            }
        }


        for (int i = 0; i < ARGS.length; i++) {
            String text = String.format("   %s      %s      ", COMMAND, ARGS[i]);
            CommandInfo info = CommandInfo.builder()
                    .name(COMMAND).args(Collections.singletonList(ARGS_UNQUOTED[i])).build();
            List<CommandInfo> expected = Collections.singletonList(info);
            List<CommandInfo> actual = CommandLineParser.parse(text, env);
            for (int j = 0; j < expected.size(); j++) {
                assertThat(actual.get(j).getArgs(), is(expected.get(j).getArgs()));
                assertThat(actual.get(j).getName(), is(expected.get(j).getName()));
            }
        }

        for (int i = 0; i < ARGS.length; i++) {
            String text = String.format("   %s      %s    %s  %s   %s  ", COMMAND, ARGS[i], ARGS[i], ARGS[i], ARGS[i]);
            CommandInfo info = CommandInfo.builder().name(COMMAND).args(
                    Arrays.asList(ARGS_UNQUOTED[i], ARGS_UNQUOTED[i], ARGS_UNQUOTED[i], ARGS_UNQUOTED[i])
            ).build();
            List<CommandInfo> expected = Collections.singletonList(info);
            List<CommandInfo> actual = CommandLineParser.parse(text, env);
            for (int j = 0; j < expected.size(); j++) {
                assertThat(actual.get(j).getArgs(), is(expected.get(j).getArgs()));
                assertThat(actual.get(j).getName(), is(expected.get(j).getName()));
            }
        }

    }

}