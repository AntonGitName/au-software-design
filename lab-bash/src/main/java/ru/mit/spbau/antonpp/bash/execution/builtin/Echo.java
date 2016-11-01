package ru.mit.spbau.antonpp.bash.execution.builtin;

import lombok.val;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.execution.Executable;
import ru.mit.spbau.antonpp.bash.io.IOStreamsWrapper;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author antonpp
 * @since 01/11/2016
 */
public class Echo implements Executable {
    @Override
    public int execute(Environment env, String[] args, IOStreamsWrapper io) throws Exception {
        val result = Arrays.stream(args).collect(Collectors.joining(" ")) + "\n";
        io.getOut().write(result.getBytes(Charset.defaultCharset()));
        return 0;
    }
}
