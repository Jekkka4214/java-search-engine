
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

            System.out.println(
                    "Unique words: " + index.getUniqueWordCount()
            );

            SearchService searchService =
                    new SearchService(index);

            try (Scanner scanner = new Scanner(System.in)) {

                while (true) {

                    System.out.print("\nSearch (exit to quit): ");

                    if (!scanner.hasNextLine()) {
                        break;
                    }

                    String query = scanner.nextLine();

                    if (query.trim().equalsIgnoreCase("exit")) {
                        break;
                    }

                    List<String> results =
                            searchService.search(query);

                    System.out.println(
                            "Found " + results.size() + " documents:"
                    );

                    for (String result : results) {
                        System.out.println("- " + result);
                    }
                }
            }

        } catch (IOException e) {

            System.err.println(
                    "Cannot read documents: " + e.getMessage()
            );
        }
    }
}
