package com.jekkka4214.searchengine;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DocumentLoader {

    public int loadIntoIndex(Path folder, InvertedIndex index) throws IOException {

        int count = 0;

        try (DirectoryStream<Path> paths = Files.newDirectoryStream(folder, "*.txt")) {

            for (Path path : paths) {

                if (!Files.isRegularFile(path)) {
                    continue;
                }

                String content = Files.readString(path);

                Document document = new Document(
                        path.getFileName().toString(),
                        content
                );

                index.addDocument(document);

                count++;
            }
        }

        return count;
    }
}
