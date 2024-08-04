package ru.sokolov.files_manipulator.service;

import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;
import ru.sokolov.files_manipulator.model.FileDTO;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public interface FilesManipulatorService {

    List<FileDTO> files(@NonNull String directory);

    void rename(@NonNull String pathFrom, @NonNull String toName);

    void copy(@NonNull String pathFrom, @NonNull String pathTo) throws IOException;

    void remove(@NonNull String pathStr) throws IOException;

    byte[] download(@NonNull String pathStr) throws IOException;

    void upload(Collection<MultipartFile> files, @NonNull String directory) throws IOException;
}
