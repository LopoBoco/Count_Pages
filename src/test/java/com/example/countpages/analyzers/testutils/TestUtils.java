package com.example.countpages.analyzers.testutils;

import lombok.SneakyThrows;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestUtils {

    public static Path getResourceAsPath(String resourcePath) {
        try {
            URL resourceUrl = TestUtils.class.getClassLoader().getResource(resourcePath);
            return Path.of(resourceUrl.toURI());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @SneakyThrows
    public static String createTestDir() {
        return Files.createTempDirectory("testDir").toFile().getAbsolutePath();
    }
}
