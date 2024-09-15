package com.example.countpages.analyzers.file;

import java.io.File;
import java.util.Optional;

public interface FileAnalyzer {
    Optional<FileInfo> analyze(File file);

    boolean supports(File file);
}
