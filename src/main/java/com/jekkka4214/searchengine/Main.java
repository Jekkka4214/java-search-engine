package com.jekkka4214.searchengine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        InvertedIndex index = new InvertedIndex();
        DocumentLoader loader = new DocumentLoader();

        try {
            int count = loader.loadIntoIndex(
                    Path.of("documents"),
                    index
            );

            System.out.println("Search Engine started!");
            System.out.println("Documents: " + count);
            System.out.println("Unique words: " + index.getUniqueWordCount());
            System.out.println("Commands: /and, /or, /exit");

            SearchService searchService = new SearchService(index);
            SearchMode mode = SearchMode.OR;

            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {
                    System.out.print("\n[" + mode + "] Search: ");

                    if (!scanner.hasNextLine()) {
                        break;
                    }

                    String query = scanner.nextLine().strip();

                    if (query.equalsIgnoreCase("/exit")) {
                        break;
                    }

                    if (query.equalsIgnoreCase("/and")) {
                        mode = SearchMode.AND;
                        System.out.println("AND mode enabled.");
                        continue;
                    }

                    if (query.equalsIgnoreCase("/or")) {
                        mode = SearchMode.OR;
                        System.out.println("OR mode enabled.");
                        continue;
                    }

                    if (query.isEmpty()) {
                        System.out.println("Enter a search query.");
                        continue;
                    }

                    List<String> results = searchService.search(query, mode);

                    String label = results.size() == 1
                            ? " document:"
                            : " documents:";

                    System.out.println("Found " + results.size() + label);

                    for (String result : results) {
                        System.out.println("- " + result);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Cannot read documents: " + e.getMessage());
        }
    }
}