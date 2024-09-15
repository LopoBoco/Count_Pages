package com.example.countpages.analyzers.directory;

import com.example.countpages.analyzers.file.FileAnalyzer;
import com.example.countpages.analyzers.file.doc.DocxAnalyzer;
import com.example.countpages.analyzers.file.pdf.PdfAnalyzer;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.example.countpages.analyzers.testutils.TestUtils.getResourceAsPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecursivePathAnalyzerTest {
    private static final String TEST_DATA_PATH = "test-data/";
    private final RecursivePathAnalyzer recursivePathAnalyzer
            = new RecursivePathAnalyzer(List.of(new PdfAnalyzer(), new DocxAnalyzer()));

    @BeforeEach
    public void setUp() throws FileNotFoundException {
        Path testFilePath = Paths.get(getResourceAsPath(TEST_DATA_PATH).toFile().getAbsolutePath() + "testFile.pdf");

        if (!Files.exists(testFilePath)) {
            PdfWriter writer = new PdfWriter(testFilePath.toString());
            PdfDocument pdfDocument = new PdfDocument(writer);
            pdfDocument.addNewPage();
            pdfDocument.close();
        }
    }

    @ParameterizedTest
    @MethodSource("analyzeTestData")
    public void testAnalyzeDirectory(String path, int expectedFiles, int expectedPages) {
        //when
        DirectoryInfo result = recursivePathAnalyzer.analyzeDirectory(getResourceAsPath(path));

        //then
        assertEquals(expectedFiles, result.getTotalDocuments(), "Количество документов должно совпадать");
        assertEquals(expectedPages, result.getTotalPages(), "Количество страниц должно совпадать");
    }

    @Test
    public void testAnalyzeDirectoryWithNullPath() {
        assertThrows(NullPointerException.class, () -> {
            recursivePathAnalyzer.analyzeDirectory(null);
        });
    }

    @Test
    public void testAnalyzeDirectoryWithEmptyListFiles() {
        File tempDir = new File("emptyDir");

        List<FileAnalyzer> analyzers = new ArrayList<>();
        DirectoryInfo result = recursivePathAnalyzer.analyzeDirectory(tempDir.toPath());

        assertEquals(0, result.getTotalDocuments(), "Количество документов должно быть 0");
        assertEquals(0, result.getTotalPages(), "Количество страниц должно быть 0");

        tempDir.delete();
    }

    @Test
    public void testAnalyzeDirectoryWithUnsupportedFormat() throws Exception {
        File tempFile = new File("unsupportedFile.txt");
        tempFile.createNewFile();

        DirectoryInfo result = recursivePathAnalyzer.analyzeDirectory(tempFile.toPath());

        assertEquals(0, result.getTotalDocuments(), "Количество документов должно быть 0");
        assertEquals(0, result.getTotalPages(), "Количество страниц должно быть 0");

        tempFile.delete();
    }

    public static Stream<Arguments> analyzeTestData() {
        return Stream.of(
                Arguments.arguments(TEST_DATA_PATH, 10, 12),
                Arguments.arguments(TEST_DATA_PATH + "DIR_1", 2, 2),
                Arguments.arguments(TEST_DATA_PATH + "DIR_2", 1, 1),
                Arguments.arguments(TEST_DATA_PATH + "DIR_3", 2, 2)
        );
    }
}
