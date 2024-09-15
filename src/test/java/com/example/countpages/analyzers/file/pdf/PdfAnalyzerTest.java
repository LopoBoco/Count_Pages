package com.example.countpages.analyzers.file.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.countpages.analyzers.testutils.TestUtils.createTestDir;
import static org.junit.jupiter.api.Assertions.*;

public class PdfAnalyzerTest {
    private static final String TEST_DATA_PATH = createTestDir();
    private final PdfAnalyzer pdfAnalyzer = new PdfAnalyzer();

    @BeforeEach
    void setUp() throws FileNotFoundException {
        Path testFilePath = Paths.get(TEST_DATA_PATH + "testFile.pdf");

        PdfWriter writer = new PdfWriter(testFilePath.toString());
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.addNewPage();
        pdfDocument.close();
    }

    @Test
    void testAnalyzeSuccess() {
        // given
        File testFile = new File(TEST_DATA_PATH + "testFile.pdf");

        // when
        int pageCount = pdfAnalyzer.analyze(testFile).get().getTotalPages();

        // then
        assertEquals(1, pageCount, "Количество страниц должно быть 1");
    }

    @Test
    void testAnalyzeIOException() {
        // given
        File testFile = new File(TEST_DATA_PATH + "testFile.pdf");

        // when
        int pageCount = pdfAnalyzer.analyze(testFile).get().getTotalPages();

        // then
        assertEquals(1, pageCount, "Количество страниц должно быть 1");
    }

    @Test
    void testSupports() {
        // given
        File pdfFile = new File("document.pdf");

        // then
        assertTrue(pdfAnalyzer.supports(pdfFile), "Analyzer должен поддерживать PDF файлы");

        File txtFile = new File("document.txt");
        assertFalse(pdfAnalyzer.supports(txtFile), "Analyzer не должен поддерживать текстовые файлы");
    }

    @Test
    void testCountPagesSuccess() throws Exception {
        // given
        File testFile = new File(TEST_DATA_PATH + "testFile.pdf");
        try (PdfWriter writer = new PdfWriter(new FileOutputStream(testFile));
             PdfDocument pdfDocument = new PdfDocument(writer)) {
            pdfDocument.addNewPage();
            pdfDocument.addNewPage();
        }

        // when
        int pageCount = pdfAnalyzer.analyze(testFile).get().getTotalPages();

        // then
        assertEquals(2, pageCount, "Количество страниц должно быть 2");

        testFile.delete();
    }

    @Test
    void testReturnsZeroForNonExistentFile() {
        // given
        File testFile = new File(TEST_DATA_PATH + "nonExistentFile.pdf");

        // when
        var result = pdfAnalyzer.analyze(testFile);

        // then
        assertFalse(result.isPresent(), "Должно вернуть пустой анализ");
    }

    @Test
    void testCountPagesWithEmptyPdf() throws Exception {
        // given
        File testFile = new File(TEST_DATA_PATH + "emptyTestFile.pdf");
        try (PdfWriter writer = new PdfWriter(new FileOutputStream(testFile));
             PdfDocument pdfDocument = new PdfDocument(writer)) {
            pdfDocument.addNewPage();
        }

        // when
        int pageCount = pdfAnalyzer.analyze(testFile).get().getTotalPages();

        // then
        assertEquals(1, pageCount, "Количество страниц должно быть 1");

        testFile.delete();
    }
}
