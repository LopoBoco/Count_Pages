package com.example.countpages.analyzers.directory;

import com.example.countpages.analyzers.file.FileInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectoryInfoTest {

    @Test
    void testConstructorAndGetters() {
        // given
        DirectoryInfo result = new DirectoryInfo(5, 10);

        // then
        assertEquals(5, result.getTotalDocuments(), "Total documents should be 5");
        assertEquals(10, result.getTotalPages(), "Total pages should be 10");
    }

    @Test
    void testZeroValues() {
        // given
        DirectoryInfo result = new DirectoryInfo(0, 0);

        // then
        assertEquals(0, result.getTotalDocuments(), "Total documents should be 0");
        assertEquals(0, result.getTotalPages(), "Total pages should be 0");
    }

    @Test
    void testMergesWithAnotherDirectoryInfo() {
        // given
        DirectoryInfo info1 = new DirectoryInfo(5, 5);
        DirectoryInfo info2 = new DirectoryInfo(5, 5);

        // when
        info1.merge(info2);

        assertEquals(10, info1.getTotalDocuments());
        assertEquals(10, info1.getTotalPages());
    }

    @Test
    void testMergesWithFileInfo() {
        // given
        DirectoryInfo dirInfo = new DirectoryInfo(5, 5);
        FileInfo fileInfo = new FileInfo(5);

        // when
        dirInfo.merge(fileInfo);

        assertEquals(6, dirInfo.getTotalDocuments());
        assertEquals(10, dirInfo.getTotalPages());
    }
}
