package ru.sokolov.files_manipulator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.sokolov.files_manipulator.model.FileDTO;
import ru.sokolov.files_manipulator.service.FilesManipulatorService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/filesManipulator")
public class FilesManipulatorController {

    @Autowired
    private FilesManipulatorService filesManipulatorService;

    @GetMapping("/files")
    public List<FileDTO> files(@RequestParam String directory) {
        return filesManipulatorService.files(directory);
    }

    @PutMapping("/rename")
    public void rename(@RequestParam String pathFrom, @RequestParam String toName) {
        filesManipulatorService.rename(pathFrom, toName);
    }

    @PutMapping("/copy")
    public void copy(@RequestParam String pathFrom, @RequestParam String pathTo) throws IOException {
        filesManipulatorService.copy(pathFrom, pathTo);
    }

    @GetMapping("/download")
    public byte[] download(@RequestParam String path) throws IOException {
        return filesManipulatorService.download(path);
    }

    @DeleteMapping("/remove")
    public void remove(@RequestParam String path) throws IOException {
        filesManipulatorService.remove(path);
    }

    @PostMapping("/upload")
    public void upload(MultipartHttpServletRequest multipartRequest) throws IOException {
        filesManipulatorService.upload(
                multipartRequest.getFileMap().values(), multipartRequest.getParameter("directory"));
    }
}
