package ru.mit.spbau.antonpp.bash.execution.builtin;

import com.google.common.io.ByteStreams;
import lombok.val;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.TooManyArgumentsException;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class WordCount extends AbstractBuiltinExecutable {
    @Override
    public int execute(Environment env, List<String> args, IOStreams io) throws Exception {
        if (args.size() > 0) {
            throw new TooManyArgumentsException(args.size(), 0);
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {
            ByteStreams.copy(io.getIn(), bos);
            val bytes = bos.toByteArray();
            val text = new String(bytes, Charset.defaultCharset());
            int length = bytes.length;
            int lines = text.split("\n").length;
            int words = text.split("\\s+").length;
            val result = String.format("%d %d %d", lines, words, length);
            writeString(io, result);
        }
        return 0;
    }
}
