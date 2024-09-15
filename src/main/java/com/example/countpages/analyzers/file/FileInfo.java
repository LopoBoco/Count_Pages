package com.example.countpages.analyzers.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
   В будущем может понадобиться больше информации о файле,
   поэтому лучше сразу подготовить структуру и расширять уже ее
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfo {
    private int totalPages;
}