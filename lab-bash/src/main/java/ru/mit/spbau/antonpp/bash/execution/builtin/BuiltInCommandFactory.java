package ru.mit.spbau.antonpp.bash.execution.builtin;

import lombok.Getter;
import ru.mit.spbau.antonpp.bash.execution.Executable;

/**
 * Patterns used: Lazy initialization, Singleton, Factory
 *
 * @author Anton Mordberg
 * @since 17.02.17
 */
public class BuiltInCommandFactory {

    private BuiltInCommandFactory() {
    }

    public static Executable get(String name) {
        for (LazyHolder holder : LazyHolder.values()) {
            if (holder.toString().equalsIgnoreCase(name)) {
                return holder.getExecutable();
            }
        }
        return null;
    }

    private enum LazyHolder {
        CAT(new Cat()), PWD(new Pwd()), WC(new WordCount()), ECHO(new Echo()), EXIT(new Exit()), GREP(new Grep());
        @Getter
        private final Executable executable;

        LazyHolder(Executable executable) {
            this.executable = executable;
        }
    }
}
