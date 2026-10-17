package com.jekkka4214.searchengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

public class InvertedIndex {

    private final Map<String, Set<String>> index = new HashMap<>();

    private final TextTokenizer tokenizer = new TextTokenizer();

    public void addDocument(Document document) {

        Matcher matcher = tokenizer.findWords(document.getContent());

        while (matcher.find()) {

            String word = tokenizer.normalize(matcher.group());

            index.computeIfAbsent(
                    word, key -> new HashSet<>()
            ).add(document.getName());
        }
    }

    public void addMatches(String word, Set<String> results) {

        Set<String> documents = index.get(word);

        if (documents != null) {
            results.addAll(documents);
        }
    }

    public int getUniqueWordCount() {
        return index.size();
    }
}
