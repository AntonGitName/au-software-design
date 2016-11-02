package ru.mit.spbau.antonpp.bash.execution.builtin;

import com.google.common.io.ByteStreams;
import lombok.val;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.TooManyArgumentsException;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class WordCount extends AbstractBuiltinExecutable {
    @Override
    public int execute(Environment env, List<String> args, IOStreams io) throws Exception {
        if (args.size() > 1) {
            throw new TooManyArgumentsException(args.size(), 1);
        }
        if (args.size() == 1) {
            try (InputStream in = new FileInputStream(args.get(0))) {
                wc(in, io.getOut());
            }
        } else {
            wc(io.getIn(), io.getOut());
        }
        return 0;
    }

    private void wc(InputStream in, OutputStream out) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(1024)) {
            ByteStreams.copy(in, bos);
            val bytes = bos.toByteArray();
            val text = new String(bytes, Charset.defaultCharset());
            int length = bytes.length;
            int lines = text.split("\n").length;
            int words = text.split("\\s+").length;
            val result = String.format("%d %d %d", lines, words, length);
            writeString(out, result);
        }
    }
}
