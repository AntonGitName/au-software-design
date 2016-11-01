package ru.mit.spbau.antonpp.bash.execution;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import ru.mit.spbau.antonpp.bash.cli.CommandInfo;
import ru.mit.spbau.antonpp.bash.cli.Environment;
import ru.mit.spbau.antonpp.bash.exceptions.CommandExecutionException;
import ru.mit.spbau.antonpp.bash.io.IOStreamsWrapper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * This class provides methods to execute commands.
 *
 * @see Executable
 * @author antonpp
 * @since 01/11/2016
 */
@Slf4j
public class CommandExecutor {

    private CommandExecutor() {
    }

    /**
     * Method to start chained execution of the commands. Organizes piped input/output and prepares environment (e.g.
     * adds variables $0, $1, ...).
     *
     * @param infos Information about commands that must be executed
     * @param io    input/output/error streams
     * @param env   environment with all variables that will available during the execution of commands
     * @return return code of the last executable in the chain
     * @throws CommandExecutionException in case if executable with specified name was not found and also thrwon if any
     *                                   exception appears during the execution of the commands.
     */
    public static int executePiped(List<CommandInfo> infos, IOStreamsWrapper io, Environment env) throws CommandExecutionException {
        if (infos.size() == 1) {
            return execute(infos.get(0), io, env);
        } else {
            boolean isFirst = true;
            boolean isLast;
            byte[] buffer = null;
            int rc = 0;
            InputStream pipedInOut;
            OutputStream pipedOutIn;
            for (int i = 0; i < infos.size(); i++) {
                pipedInOut = isFirst ? io.getIn() : new ByteArrayInputStream(buffer);
                isLast = i + 1 == infos.size();
                pipedOutIn = isLast ? io.getOut() : new ByteArrayOutputStream();
                rc = execute(infos.get(i), new IOStreamsWrapper(pipedInOut, pipedOutIn, io.getErr()), env);
                isFirst = false;
                if (!isLast) {
                    buffer = ((ByteArrayOutputStream) pipedOutIn).toByteArray();
                }
            }
            return rc;
        }
    }

    private static int execute(CommandInfo commandInfo, IOStreamsWrapper io, Environment env) throws CommandExecutionException {
        val executableEnv = env.copy();
        val args = commandInfo.getArgs();
        for (int i = 0; i < args.length; i++) {
            executableEnv.setEnv(Integer.toString(i + 1), args[i]);
        }
        val name = commandInfo.getName();
        executableEnv.setEnv("0", name);
        try {
            val executable = getExecutable(name);
            if (executable == null) {
                throw new CommandExecutionException(String.format("Command `%s` not found", name));
            }
            return executable.execute(executableEnv, args, io);
        } catch (Exception e) {
            log.warn(String.format("Command %s failed during an execution", name), e);
            throw new CommandExecutionException(String.format("Command %s failed during an execution", name), e);
        }
    }

    /**
     * Looks for an {@link Executable} for the command with specified name. It first looks through built-in commands and
     * then checks if an executable with such path exists.
     *
     * @param name a name of built-in function or a path to executable file
     * @return an instance of {@link Executable} with this name or null if it was not found
     */
    @Nullable
    private static Executable getExecutable(String name) {
        val command = BuiltInCommands.getCommand(name);
        if (command == null) {
            val path = Paths.get(name);
            if (Files.isExecutable(path)) {
                return new External(path);
            }
        } else {
            return command.getExecutable();
        }
        return null;
    }
}
