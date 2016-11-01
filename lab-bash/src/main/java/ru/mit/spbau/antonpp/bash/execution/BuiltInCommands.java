package ru.mit.spbau.antonpp.bash.execution;

import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import ru.mit.spbau.antonpp.bash.execution.builtin.*;

/**
 * @author antonpp
 * @since 31/10/2016
 */
public enum BuiltInCommands {
    CAT(new Cat()), PWD(new Pwd()), WC(new WordCount()), ECHO(new Echo()), EXIT(new Exit());

    @Getter
    private final Executable executable;

    BuiltInCommands(Executable executable) {
        this.executable = executable;
    }

    @Nullable
    public static BuiltInCommands getCommand(String name) {
        for (BuiltInCommands cmd : values()) {
            if (cmd.toString().equalsIgnoreCase(name)) {
                return cmd;
            }
        }
        return null;
    }
}
