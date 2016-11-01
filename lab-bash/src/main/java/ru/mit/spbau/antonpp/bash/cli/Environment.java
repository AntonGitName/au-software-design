package ru.mit.spbau.antonpp.bash.cli;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author antonpp
 * @since 31/10/2016
 */
@Slf4j
public class Environment {

    public static final String NO_QUOTES_OR_PIPE = "[^'\"|]+";
    public static final String NO_SPECIAL = "[^'\"|\\s]+";
    public static final String SINGLE_QUOTED_UNCAPTURED = "'(?:[^'\\\\]|\\\\.)*'";
    public static final String DOUBLE_QOUTED_UNCAPTURED = "\"(?:[^\"\\\\]|\\\\.)*\"";
    private static final String S_QUOTED_TEXT = "^'((?:[^'\\\\]|\\\\.)*)'$";
    private static final String D_QUOTED_TEXT = "^\"((?:[^\"\\\\]|\\\\.)*)\"$";
    private static final Pattern S_QUOTED_PATTERN = Pattern.compile(S_QUOTED_TEXT);
    private static final Pattern D_QUOTED_PATTERN = Pattern.compile(D_QUOTED_TEXT);
    private static final Pattern SUBST_PATTERN = Pattern.compile("(\\$(\\w*))");

    private final Map<String, String> env;

    public Environment() {
        this(Collections.emptyMap());
    }

    private Environment(Map<String, String> env) {
        this.env = new HashMap<>(env);
    }

    public String getEnv(String key) {
        return env.get(key);
    }

    public String setEnv(String key, String value) {
        return env.put(key, value);
    }

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

    public Environment copy() {
        return new Environment(env);
    }
}
