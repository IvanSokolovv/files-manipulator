package ru.sokolov.files_manipulator.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FileDTO {

    private String name;

    private LocalDateTime modificationDate;

    private Integer size;
}
