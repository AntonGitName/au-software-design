package ru.mit.spbau.antonpp.bash.io;

import lombok.Getter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Wrapper class that combines all three streams used by any application
 *
 * @author antonpp
 * @since 01/11/2016
 */
public class IOStreams {
    @Getter
    private final InputStream in;
    @Getter
    private final OutputStream out;
    @Getter
    private final OutputStream err;

    public IOStreams(InputStream in, OutputStream out, OutputStream err) {
        this.in = in;
        this.out = out;
        this.err = err;
    }
}
