package ru.mit.spbau.antonpp.bash.execution.builtin;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.mit.spbau.antonpp.bash.exceptions.CommandInvalidArgumentsException;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * @author Anton Mordberg
 * @since 24.01.17
 */
public class GrepTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    private Grep grep;
    private ByteArrayOutputStream out;
    private IOStreams streams;

    private static String removeColors(String text) {
        return text.replace(Grep.RED_COLOR, "").replace(Grep.RESET_COLOR, "");
    }

    private static IOStreams createIO(String text, ByteArrayOutputStream out) {
        return new IOStreams(new ByteArrayInputStream(text.getBytes()), out, out);
    }

    @Before
    public void setUp() throws Exception {
        grep = new Grep();
        streams = null;
        out = new ByteArrayOutputStream();
    }

    @Test
    public void executeSimple() throws Exception {
        final String text = "123";
        final String query = "2";

        streams = createIO(text, out);

        grep.execute(null, Collections.singletonList(query), streams);

        checkResult("123");
    }

    @Test
    public void executeSearchWordsOnly() throws Exception {
        final String text = "abc\nabc abcw\nabcdef\nab c\n defabc\n def abc def";
        final String query = "abc";

        streams = createIO(text, out);

        grep.execute(null, Arrays.asList("-w", query), streams);

        final String actual = new String(out.toByteArray());

        checkResult("abc", "abc abcw", " def abc def");
    }

    @Test
    public void executeMultipleLine() throws Exception {
        final String text = "123\n" + "123\n" + "123\n";
        final String query = "2";

        streams = createIO(text, out);

        grep.execute(null, Collections.singletonList(query), streams);

        checkResult("123", "123", "123");
    }

    @Test
    public void executeMultipleLineRegex() throws Exception {
        final String text = "123\n" + "456\n" + "789\n";
        final String query = "3|7";

        streams = createIO(text, out);

        grep.execute(null, Arrays.asList("-e", query), streams);

        checkResult("123", "789");
    }

    @Test
    public void executeLinesAfter() throws Exception {
        final String dumpLines = "1\n2\n3\n";
        final String text = "a\n" + dumpLines + "b\n" + dumpLines + "c\n" + dumpLines;
        final String query = "b";

        streams = createIO(text, out);

        grep.execute(null, Arrays.asList("-A", "3", query), streams);

        checkResult("b", "1", "2", "3");
    }

    @Test
    public void executeCaseInsensitive() throws Exception {
        final String[] good = {"Hello, World", "hello, World", "HELLO, World", "helLo, World", "HELLo, World"};
        final String[] bad = {"abc", "h ell o, World", "", "HELL, O World"};

        final String text =
                Arrays.stream(new String[]{good[0], bad[0], good[1], bad[1], good[2], bad[2], good[3], bad[3], good[4]})
                        .collect(Collectors.joining("\n"));
        final String query = "HeLlo";

        streams = createIO(text, out);

        grep.execute(null, Arrays.asList("-i", query), streams);

        checkResult(good);
    }

    @Test
    public void executeWithFile() throws Exception {
        final String[] good = {"Pattern1", "Pattern2", "Pattern3"};
        final String[] bad = {"abc", "h ell o, World", ""};

        final String text =
                Arrays.stream(new String[]{good[0], bad[0], good[1], bad[1], good[2], bad[2]})
                        .collect(Collectors.joining("\n"));


        File testFile = testFolder.newFile("source-file.txt");
        FileOutputStream fout = new FileOutputStream(testFile);
        fout.write(text.getBytes());
        fout.close();

        streams = createIO("unused", out);
        grep.execute(null, Arrays.asList("Pattern", testFile.getAbsolutePath()), streams);

        checkResult(good);
    }

    @Test(expected = CommandInvalidArgumentsException.class)
    public void executeFileNotFound() throws Exception {
        streams = createIO("unused", out);
        grep.execute(null, Arrays.asList("pattern", "no-such-file.txt"), streams);
    }

    private void checkResult(String... good) {
        final String actual = new String(out.toByteArray());
        final String expected = Arrays.stream(good).collect(Collectors.joining("\n")) + "\n";
        assertEquals(expected, removeColors(actual));
    }

}