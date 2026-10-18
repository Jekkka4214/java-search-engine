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
        return search(query, SearchMode.OR);
    }

    public List<String> search(String query, SearchMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Search mode must not be null");
        }

        Set<String> words = extractUniqueWords(query);

        if (words.isEmpty()) {
            return new ArrayList<>();
        }

        Set<String> matches;

        if (mode == SearchMode.AND) {
            matches = findAllWords(words);
        } else {
            matches = findAnyWord(words);
        }

        List<String> results = new ArrayList<>(matches);
        results.sort(null);

        return results;
    }

    private Set<String> extractUniqueWords(String query) {
        Set<String> words = new HashSet<>();
        Matcher matcher = tokenizer.findWords(query);

        while (matcher.find()) {
            String word = tokenizer.normalize(matcher.group());
            words.add(word);
        }

        return words;
    }

    private Set<String> findAnyWord(Set<String> words) {
        Set<String> matches = new HashSet<>();

        for (String word : words) {
            index.addMatches(word, matches);
        }

        return matches;
    }

    private Set<String> findAllWords(Set<String> words) {
        String rarestWord = null;
        int smallestCount = Integer.MAX_VALUE;

        for (String word : words) {
            int count = index.getDocumentCount(word);

            if (count == 0) {
                return new HashSet<>();
            }

            if (count < smallestCount) {
                smallestCount = count;
                rarestWord = word;
            }
        }

        Set<String> matches = new HashSet<>();
        index.addMatches(rarestWord, matches);

        for (String word : words) {
            if (word.equals(rarestWord)) {
                continue;
            }

            index.retainMatches(word, matches);

            if (matches.isEmpty()) {
                break;
            }
        }

        return matches;
    }
}