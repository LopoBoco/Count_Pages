package com.example.countpages.analyzers.directory;

import com.example.countpages.analyzers.file.FileAnalyzer;
import com.example.countpages.analyzers.file.FileInfo;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public class RecursivePathAnalyzer {
    private final List<FileAnalyzer> analyzers;

    public DirectoryInfo analyzeDirectory(Path path) {
        var result = new DirectoryInfo();

        if (!Files.isDirectory(path)) {
            System.err.println("Expected a directory, but received a file: " + path);
            return result;
        }

        for (File file : Objects.requireNonNull(path.toFile().listFiles())) {
            if (file.isDirectory()) {
                result.merge(analyzeDirectory(file.toPath()));
            } else {
                analyzeFile(file).ifPresent(result::merge);
            }
        }

        return result;
    }

    public Optional<FileInfo> analyzeFile(File file) {
        for (FileAnalyzer analyzer : analyzers) {
            if (analyzer.supports(file)) {
                return analyzer.analyze(file);
            }
        }
        return Optional.empty();
    }
}
