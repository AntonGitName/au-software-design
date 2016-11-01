package ru.mit.spbau.antonpp.bash.execution.builtin;

import com.google.common.io.ByteStreams;
import lombok.val;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.TooManyArgumentsException;
import ru.mit.spbau.antonpp.bash.execution.Executable;
import ru.mit.spbau.antonpp.bash.io.IOStreamsWrapper;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class WordCount implements Executable {
    @Override
    public int execute(Environment env, String[] args, IOStreamsWrapper io) throws Exception {
        if (args.length > 0) {
            throw new TooManyArgumentsException(args.length, 0);
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {
            ByteStreams.copy(io.getIn(), bos);
            val bytes = bos.toByteArray();
            val text = new String(bytes, Charset.defaultCharset());
            int length = bytes.length;
            int lines = text.split("\n").length;
            int words = text.split("\\s+").length;
            val result = String.format("%d %d %d", lines, words, length);
            io.getOut().write(result.getBytes(Charset.defaultCharset()));
        }
        return 0;
    }
}
