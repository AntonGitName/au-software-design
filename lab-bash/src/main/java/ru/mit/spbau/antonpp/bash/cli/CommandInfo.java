package ru.mit.spbau.antonpp.bash.cli;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author antonpp
 * @since 31/10/2016
 */
public class CommandInfo {

    @Getter
    private final String name;

    @Getter
    private final String[] args;


    public CommandInfo(String name, String[] args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public String toString() {
        return "CommandInfo{" +
                "name=" + name +
                ", args=" + Arrays.toString(args) +
                '}';
    }
}
