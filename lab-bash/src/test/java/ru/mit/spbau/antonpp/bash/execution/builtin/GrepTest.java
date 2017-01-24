package ru.mit.spbau.antonpp.bash.execution.builtin;

import org.junit.Before;
import org.junit.Test;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * @author Anton Mordberg
 * @since 24.01.17
 */
public class GrepTest {

    private Grep grep;
    private IOStreams streams;

    private static String removeColors(String text) {
        return text.replace(Grep.RED_COLOR, "").replace(Grep.RESET_COLOR, "");
    }

    @Before
    public void setUp() throws Exception {
        grep = new Grep();
    }

    private static IOStreams createIO(String text, ByteArrayOutputStream out) {
        return new IOStreams(new ByteArrayInputStream(text.getBytes()), out, out);
    }

    @Test
    public void executeSimple() throws Exception {
        final String text = "123";
        final String query = "2";

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final IOStreams streams = createIO(text, out);

        grep.execute(null, Collections.singletonList(query), streams);

        final String actual = new String(out.toByteArray());

        assertEquals("123", removeColors(actual));
    }

    @Test
    public void executeSearchWordsOnly() throws Exception {
        final String text = "abc\nabc abcw\nabcdef\nab c\n defabc\n def abc def";
        final String query = "abc";

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final IOStreams streams = createIO(text, out);

        grep.execute(null, Arrays.asList("-w", query), streams);

        final String actual = new String(out.toByteArray());

        assertEquals("abc\nabc abcw\n def abc def", removeColors(actual));
    }

    @Test
    public void executeMultipleLine() throws Exception {
        final String text = "123\n" + "123\n" + "123\n";
        final String query = "2";

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final IOStreams streams = createIO(text, out);

        grep.execute(null, Collections.singletonList(query), streams);

        final String actual = new String(out.toByteArray());

        assertEquals("123\n123\n123", removeColors(actual));
    }

    @Test
    public void executeMultipleLineRegex() throws Exception {
        final String text = "123\n" + "456\n" + "789\n";
        final String query = "3|7";

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final IOStreams streams = createIO(text, out);

        grep.execute(null, Arrays.asList("-e", query), streams);

        final String actual = new String(out.toByteArray());

        assertEquals("123\n789", removeColors(actual));
    }

    @Test
    public void executeLinesAfter() throws Exception {
        final String dumpLines = "1\n2\n3\n";
        final String text = "a\n" + dumpLines + "b\n" + dumpLines + "c\n" + dumpLines;
        final String query = "b";

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final IOStreams streams = createIO(text, out);

        grep.execute(null, Arrays.asList("-A", "3", query), streams);

        final String actual = new String(out.toByteArray());

        assertEquals("b\n1\n2\n3", removeColors(actual));
    }

    @Test
    public void executeCaseInsensitive() throws Exception {
        final String[] good = {"Hello, World", "hello, World", "HELLO, World", "helLo, World", "HELLo, World"};
        final String[] bad = {"abc", "h ell o, World", "", "HELL, O World"};

        final String text =
                Arrays.stream(new String[]{good[0], bad[0], good[1], bad[1], good[2], bad[2], good[3], bad[3], good[4]})
                        .collect(Collectors.joining("\n"));
        final String query = "HeLlo";

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final IOStreams streams = createIO(text, out);

        grep.execute(null, Arrays.asList("-i", query), streams);

        final String actual = new String(out.toByteArray());

        final String expected = Arrays.stream(good).collect(Collectors.joining("\n"));
        assertEquals(expected, removeColors(actual));
    }

}