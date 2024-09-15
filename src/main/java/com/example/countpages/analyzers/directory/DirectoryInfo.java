package com.example.countpages.analyzers.directory;

import com.example.countpages.analyzers.file.FileInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryInfo {
    private int totalDocuments;
    private int totalPages;

    public void merge(DirectoryInfo other) {
        this.totalDocuments += other.getTotalDocuments();
        this.totalPages += other.getTotalPages();
    }

    public void merge(FileInfo fileInfo) {
        this.totalDocuments++;
        this.totalPages += fileInfo.getTotalPages();
    }
}
