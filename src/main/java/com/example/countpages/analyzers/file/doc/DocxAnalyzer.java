package com.example.countpages.analyzers.file.doc;

import com.example.countpages.analyzers.file.FileAnalyzer;
import com.example.countpages.analyzers.file.FileInfo;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class DocxAnalyzer implements FileAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(DocxAnalyzer.class);
    private static final int DEFAULT_HEURISTIC_LINES_PER_PAGE = 30;

    private static final List<String> supportedFormats = List.of(".docx", ".txt");

    @Override
    public Optional<FileInfo> analyze(File file) {
        try {
            return Optional.of(new FileInfo(countPages(file)));
        } catch (IOException e) {
            logger.error("Error reading DOCX file: {} - {}", file.getName(), e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean supports(File f) {
        return supportedFormats.stream()
                .anyMatch(extension -> f.getName().endsWith(extension));
    }

    private int countPages(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {

            int totalLines = 0;
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText();
                totalLines += text.split("\n").length;
            }

            return  totalLines / 30 + 1;
        }
    }
}
