package ru.sokolov.files_manipulator.service;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.sokolov.files_manipulator.model.FileDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class FilesManipulatorServiceImpl implements FilesManipulatorService {

    @Override
    public List<FileDTO> files(@NonNull String directory) {
        return Arrays.stream(
                Objects.requireNonNull(new File(directory).listFiles())
        ).map(file ->
                FileDTO.builder()
                        .name(file.getName())
                        .size((int) file.length())
                        .modificationDate(new Timestamp(file.lastModified()).toLocalDateTime())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public void rename(@NonNull String pathFrom, @NonNull String toName) {
        // имя директории исходного файла
        String directoryFrom = pathFrom.substring(0, pathFrom.lastIndexOf("/") + 1);
        // полный пусть конечного файла
        String pathToFull = directoryFrom + toName;
        new File(pathFrom).renameTo(new File(pathToFull));
    }

    @Override
    public void copy(
            @NonNull String pathFrom, @NonNull String toDirectory
    ) throws IOException {
        if (!Files.exists(Path.of(pathFrom))) throw new NoSuchFileException("Копируемый файл не найден");

        // имя файла с расширением
        String fileNameWithExt = pathFrom.substring(pathFrom.lastIndexOf("/") + 1);
        // имя файла без расширения
        String fileNameWithoutExt = fileNameWithExt.substring(0, fileNameWithExt.lastIndexOf("."));
        // расширение
        String fileExt = fileNameWithExt.substring(fileNameWithExt.lastIndexOf("."));
        // обновленное значение целевой директории
        String toDirectoryMod = toDirectory.endsWith("/") ? toDirectory : toDirectory + "/";
        // обновленное значение файла, куда копируем
        String pathToFull = toDirectoryMod + fileNameWithExt;

        if (Files.exists(Path.of(pathToFull))) {
            // имя файла + uuid + расширение
            String fileNameWithExtMod = fileNameWithoutExt + "_" + UUID.randomUUID() + fileExt;
            // полный путь файла c uuid
            String pathToFullMod = toDirectoryMod + fileNameWithExtMod;
            com.google.common.io.Files.copy(new File(pathFrom), new File(pathToFullMod));
            return;
        }

        com.google.common.io.Files.copy(new File(pathFrom), new File(pathToFull));
    }

    @Override
    public void remove(@NonNull String pathStr) throws IOException {
        Path ofPath = Path.of(pathStr);
        if (Files.exists(ofPath)) Files.delete(ofPath);
    }

    @Override
    public byte[] download(@NonNull String pathStr) throws IOException {
        Path path = Path.of(pathStr);
        if (!Files.exists(path)) return new byte[0];
        return Files.readAllBytes(path);
    }

    @Override
    public void upload(Collection<MultipartFile> files, @NonNull String directory) throws IOException {
        for (MultipartFile multipartFile : files) {
            // обновленное значение директории
            String directoryMod = directory.endsWith("/") ? directory : directory + "/";
            // полный путь
            directoryMod = directoryMod + multipartFile.getOriginalFilename();
            File file = new File(directoryMod);
            file.createNewFile();
            com.google.common.io.Files.write(multipartFile.getBytes(), file);
        }
    }
}
