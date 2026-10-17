package com.jekkka4214.searchengine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

public class SearchService {

    private final InvertedIndex index;

    private final TextTokenizer tokenizer = new TextTokenizer();

    public SearchService(InvertedIndex index) {

        this.index = index;
    }

    public List<String> search(String query) {

        Set<String> matches = new HashSet<>();

        Matcher matcher = tokenizer.findWords(query);

        while (matcher.find()) {

            String word = tokenizer.normalize(matcher.group());

            index.addMatches(word, matches);
        }

        List<String> results = new ArrayList<>(matches);

        results.sort(null);

        return results;
    }
}
