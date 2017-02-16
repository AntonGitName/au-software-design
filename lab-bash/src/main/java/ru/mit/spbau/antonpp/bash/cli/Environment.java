package ru.mit.spbau.antonpp.bash.cli;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class that stores all variables that available in console. it also provides methods for substitution of these
 * variables in to the text.
 *
 * @author antonpp
 * @since 31/10/2016
 */
@Slf4j
public class Environment {

    public static final String NO_QUOTES_OR_PIPE = "[^'\"|]+";
    public static final String NO_SPECIAL = "[^'\"|\\s]+";
    public static final String SINGLE_QUOTED_UNCAPTURED = "'(?:[^'\\\\]|\\\\.)*'";
    public static final String DOUBLE_QUOTED_UNCAPTURED = "\"(?:[^\"\\\\]|\\\\.)*\"";
    private static final String S_QUOTED_TEXT = "^'((?:[^'\\\\]|\\\\.)*)'$";
    private static final String D_QUOTED_TEXT = "^\"((?:[^\"\\\\]|\\\\.)*)\"$";
    private static final Pattern S_QUOTED_PATTERN = Pattern.compile(S_QUOTED_TEXT);
    private static final Pattern D_QUOTED_PATTERN = Pattern.compile(D_QUOTED_TEXT);
    private static final Pattern SUBST_PATTERN = Pattern.compile("(\\$(\\w*))");

    private final Map<String, String> env;

    /**
     * Creates an empty environment.
     */
    public Environment() {
        this(Collections.emptyMap());
    }

    /**
     * Creates environment with specified properties.
     *
     * @param env map with properties
     */
    private Environment(Map<String, String> env) {
        this.env = new HashMap<>(env);
    }

    /**
     * Returns value of the variable.
     *
     * @param key name of the variable
     * @return value of the variable if it exists or empty string otherwise
     */
    public String getEnv(String key) {
        return env.containsKey(key) ? env.get(key) : "";
    }

    /**
     * Updates {@link Environment} with a new value for the variable
     *
     * @param key   name of the variable
     * @param value new value of the variable
     * @return the previous value associated with key, or null if there was no mapping for key.
     */
    public String setEnv(String key, String value) {
        return env.put(key, value);
    }

    /**
     * Applies all possible substitutions to a string
     * @param str a string to perform substitutions
     * @return a string with all possible substitutions applied
     */
    private String trySubstitute(String str) {
        val matcher = SUBST_PATTERN.matcher(str);
        while (matcher.find()) {
            val subst = matcher.group(1);
            val key = matcher.group(2);
            val value = getEnv(key);
            log.debug("Replacing `${}` -> `{}`", key, value);
            str = str.replace(subst, value);
        }
        return str;
    }

    /**
     * This method removes quotes (single and double) if necessary and tries to perform all possible substitutions.
     *
     * @param str a string to process
     * @return resulting string
     */
    public String unquoteAndSubstitute(String str) {
        val sMatcher = S_QUOTED_PATTERN.matcher(str);
        String result = str;
        if (sMatcher.find()) {
            result = sMatcher.group(1);
        } else {
            val dMacher = D_QUOTED_PATTERN.matcher(str);
            if (dMacher.find()) {
                result = dMacher.group(1);
            }
            result = trySubstitute(result);
        }
        log.debug("Unquoting `{}` -> `{}`", str, result);
        return result;
    }

    /**
     * Creates a copy of this {@link Environment}
     * @return the copy
     */
    public Environment copy() {
        return new Environment(env);
    }
}
