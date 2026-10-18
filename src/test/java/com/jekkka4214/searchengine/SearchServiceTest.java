package com.jekkka4214.searchengine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchServiceTest {

    private SearchService searchService;

    @BeforeEach
    void setUp() {
        InvertedIndex index = new InvertedIndex();

        index.addDocument(new Document(
                "java.txt",
                "Java is a programming language. Java Java."
        ));

        index.addDocument(new Document(
                "algorithms.txt",
                "Java algorithms and data structures."
        ));

        index.addDocument(new Document(
                "networking.txt",
                "Computer networking and protocols."
        ));

        index.addDocument(new Document(
                "empty.txt",
                ""
        ));

        searchService = new SearchService(index);
    }

    @Test
    void defaultSearchUsesOrMode() {
        List<String> results =
                searchService.search("programming networking");

        assertEquals(
                List.of("java.txt", "networking.txt"),
                results
        );
    }

    @Test
    void orSearchReturnsDocumentsContainingAnyQueryWord() {
        List<String> results =
                searchService.search("java networking", SearchMode.OR);

        assertEquals(
                List.of("algorithms.txt", "java.txt", "networking.txt"),
                results
        );
    }

    @Test
    void andSearchReturnsDocumentsContainingAllQueryWords() {
        List<String> results =
                searchService.search("java programming", SearchMode.AND);

        assertEquals(
                List.of("java.txt"),
                results
        );
    }

    @Test
    void andSearchReturnsEmptyWhenWordsOccurInDifferentDocuments() {
        List<String> results =
                searchService.search("java networking", SearchMode.AND);

        assertTrue(results.isEmpty());
    }

    @Test
    void searchIgnoresCase() {
        for (SearchMode mode : SearchMode.values()) {
            assertEquals(
                    List.of("algorithms.txt", "java.txt"),
                    searchService.search("JaVa", mode)
            );
        }
    }

    @Test
    void repeatedQueryWordsDoNotChangeResults() {
        for (SearchMode mode : SearchMode.values()) {
            assertEquals(
                    List.of("algorithms.txt", "java.txt"),
                    searchService.search("JAVA java Java", mode)
            );
        }
    }

    @Test
    void unknownWordReturnsEmpty() {
        for (SearchMode mode : SearchMode.values()) {
            assertTrue(
                    searchService.search("unknownword", mode).isEmpty()
            );
        }
    }

    @Test
    void orSearchKeepsMatchesWhenAnotherWordIsUnknown() {
        assertEquals(
                List.of("algorithms.txt", "java.txt"),
                searchService.search("java unknownword", SearchMode.OR)
        );
    }

    @Test
    void andSearchReturnsEmptyWhenOneWordIsUnknown() {
        assertTrue(
                searchService.search(
                        "java unknownword",
                        SearchMode.AND
                ).isEmpty()
        );
    }

    @Test
    void blankAndPunctuationOnlyQueriesReturnEmpty() {
        List<String> queries = List.of("", "   ", "!!!");

        for (SearchMode mode : SearchMode.values()) {
            for (String query : queries) {
                assertTrue(searchService.search(query, mode).isEmpty());
            }
        }
    }

    @Test
    void searchMatchesWholeWordsOnly() {
        assertEquals(
                List.of("java.txt"),
                searchService.search("a")
        );

        assertTrue(searchService.search("jav").isEmpty());
    }

    @Test
    void punctuationSeparatesQueryWords() {
        assertEquals(
                List.of("java.txt"),
                searchService.search("JAVA, programming!", SearchMode.AND)
        );
    }

    @Test
    void searchDoesNotModifyTheIndex() {
        searchService.search("java networking", SearchMode.AND);

        assertEquals(
                List.of("algorithms.txt", "java.txt"),
                searchService.search("java")
        );
    }
}