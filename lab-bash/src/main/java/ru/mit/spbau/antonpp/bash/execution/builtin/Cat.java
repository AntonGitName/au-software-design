package ru.mit.spbau.antonpp.bash.execution.builtin;

import com.google.common.io.ByteStreams;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.SpecifiedFileNotFoundException;
import ru.mit.spbau.antonpp.bash.io.IOStreams;

import java.io.*;
import java.util.List;

/**
 * Cat is cat. cat prints from file or stdin to stdout.
 *
 * Usage: cat [file]
 *
 * @author antonpp
 * @since 01/11/2016
 */
public class Cat extends AbstractBuiltinExecutable {

    public Cat() {
        super(1);
    }

    @Override
    public int executeInternal(Environment env, List<String> args, IOStreams io) throws Exception {
        if (args.size() == 1) {
            try (InputStream in = new FileInputStream(args.get(0))) {
                cat(in, io.getOut());
            } catch (FileNotFoundException e) {
                throw new SpecifiedFileNotFoundException(args.get(0));
            }
        } else {
            cat(io.getIn(), io.getOut());
        }
        return 0;
    }

    private void cat(InputStream in, OutputStream out) throws IOException {
        ByteStreams.copy(in, out);
    }
}
