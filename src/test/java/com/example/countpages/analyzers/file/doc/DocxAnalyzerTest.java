package com.example.countpages.analyzers.file.doc;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.countpages.analyzers.testutils.TestUtils.createTestDir;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class DocxAnalyzerTest {
    private static final String TEST_DATA_PATH = createTestDir();
    private DocxAnalyzer docxAnalyzer;

    @BeforeEach
    void setUp() throws IOException {
        File testFile = new File(TEST_DATA_PATH + "testFile.docx");

        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            paragraph.createRun().setText("Это тестовый документ DOCX.");
            try (FileOutputStream out = new FileOutputStream(testFile)) {
                document.write(out);
            }
        }

        docxAnalyzer = new DocxAnalyzer();
    }

    @Test
    void testAnalyzeIOException() {
        // given
        var testFile = new File(TEST_DATA_PATH + "testFile.docx");

        // when
        var result = docxAnalyzer.analyze(testFile);

        // then
        assertEquals(1, result.get().getTotalPages(), "Количество страниц должно быть 1");
    }

    @ParameterizedTest
    @ValueSource(strings = {"document.docx", "document.txt"})
    void testSupports(String path) {
        // given
        var file = new File(path);

        // then
        assertTrue(docxAnalyzer.supports(file), "Analyzer должен поддерживать DOCX файлы");
    }

    @Test
    void testAnalyzeSuccess() throws IOException {
        // given
        var testFile = new File(TEST_DATA_PATH + "testFile.docx");
        try (var document = new XWPFDocument()) {
            for (int i = 0; i < 60; i++) {
                XWPFParagraph paragraph = document.createParagraph();
                paragraph.createRun().setText("Это тестовый параграф " + (i + 1));
            }
            try (FileOutputStream out = new FileOutputStream(testFile)) {
                document.write(out);
            }
        }

        // when
        int pageCount = docxAnalyzer.analyze(testFile).get().getTotalPages();

        // then
        assertEquals(3, pageCount, "Количество страниц должно быть 2");

        testFile.delete();
    }

    @Test
    void testReturnZeroOnIOException() {
        // given
        var fileMock = mock(File.class);
        doAnswer((Answer<Object>) invocation -> {
            throw new IOException("Checked exception on any method call");
        }).when(fileMock).getPath();

        // when
        var result = docxAnalyzer.analyze(fileMock);

        // then
        assertFalse(result.isPresent(), "Должно вернуть пустой анализ");
    }

    @Test
    void testCountPagesEmptyDocument() throws IOException {
        // given
        var testFile = new File(TEST_DATA_PATH + "emptyTestFile.docx");
        try (var document = new XWPFDocument(); var out = new FileOutputStream(testFile)) {
            document.write(out);
        }

        // when
        int pageCount = docxAnalyzer.analyze(testFile).get().getTotalPages();

        // then
        assertEquals(1, pageCount, "Количество страниц должно быть 1");

        testFile.delete();
    }
}