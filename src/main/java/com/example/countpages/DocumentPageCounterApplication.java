package com.example.countpages;

import com.example.countpages.analyzers.directory.DirectoryInfo;
import com.example.countpages.analyzers.directory.RecursivePathAnalyzer;
import com.example.countpages.analyzers.file.doc.DocxAnalyzer;
import com.example.countpages.analyzers.file.pdf.PdfAnalyzer;
import com.example.countpages.analyzers.file.FileAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class DocumentPageCounterApplication implements CommandLineRunner {
    @Value("${app.rootPath}")
    private String defaultRootPath;

    private static final Logger logger = LoggerFactory.getLogger(DocumentPageCounterApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(DocumentPageCounterApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String rootPath = args.length > 0 ? args[0] : defaultRootPath;

        List<FileAnalyzer> analyzers = Arrays.asList(new PdfAnalyzer(), new DocxAnalyzer());
        RecursivePathAnalyzer recursivePathAnalyzer = new RecursivePathAnalyzer(analyzers);

        try {
            DirectoryInfo result = recursivePathAnalyzer.analyzeDirectory(Paths.get(rootPath));
            System.out.printf("Documents: %d\nPages: %d%n", result.getTotalDocuments(), result.getTotalPages());
        } catch (Exception e) {
            logger.error("Error while processing files: {}", e.getMessage(), e);
            System.exit(1);
        }
    }
}