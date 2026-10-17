
package com.jekkka4214.searchengine;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTokenizer {

    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{N}]+");

    public Matcher findWords(String text) {
        return WORD_PATTERN.matcher(text);
    }

    public String normalize(String word) {
        return word.toLowerCase(Locale.ROOT);
    }
}
