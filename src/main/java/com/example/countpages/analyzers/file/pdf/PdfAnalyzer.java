package com.example.countpages.analyzers.file.pdf;

import com.example.countpages.analyzers.file.FileAnalyzer;
import com.example.countpages.analyzers.file.FileInfo;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class PdfAnalyzer implements FileAnalyzer {
    @Override
    public Optional<FileInfo> analyze(File file) {
        try {
            int pages = countPages(file);
            return Optional.of(new FileInfo(pages));
        } catch (IOException e) {
            System.err.println("Error reading PDF file: " + file.getName() + " - " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public boolean supports(File f) {
        return f.getName().endsWith(".pdf");
    }

    private int countPages(File file) throws IOException {
        try (var reader = new PdfReader(file); var pdfDocument = new PdfDocument(reader)) {
            return pdfDocument.getNumberOfPages();
        }
    }
}
