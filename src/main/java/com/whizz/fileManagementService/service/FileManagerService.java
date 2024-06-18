package com.whizz.fileManagementService.service;

import com.whizz.fileManagementService.FileManagerRepository;
import com.whizz.fileManagementService.pojo.FileMaster;
import com.whizz.fileManagementService.pojo.enums.StorageType;
import com.whizz.fileManagementService.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileManagerService {

    @Autowired
    private FileManagerRepository fileManagerRepository;

    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    public String uploadFileOnLocal(MultipartFile file) throws IOException {
        // Get the formatted file name
        String fileName = ProjectUtils.getFileName(file.getOriginalFilename()) + "_" + ProjectUtils.getCurrentTimeStamp();

        // Get the file extension
        String fileExtension = ProjectUtils.getFileExtension(file.getOriginalFilename());

        File destinationFile = null;

        // Define the upload directory and ensure it exists
        File uploadDirectory = new File(fileUploadDirectory);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }

        // Determine the specific directory based on the file extension and ensure it exists
        switch (fileExtension.toLowerCase()) {
            case "png":
            case "jpeg":
            case "jpg":
                destinationFile = createAndReturnFile("images", fileName, fileExtension);
                break;
            case "pdf":
                destinationFile = createAndReturnFile("pdf", fileName, fileExtension);
                break;
            case "video":
                destinationFile = createAndReturnFile("video", fileName, fileExtension);
                break;
            case "songs":
                destinationFile = createAndReturnFile("songs", fileName, fileExtension);
                break;
            default:
                destinationFile = createAndReturnFile("document", fileName, fileExtension);
                break;
        }

        // Transfer the file to the destination
        file.transferTo(destinationFile);

        // Save file metadata to the database
        FileMaster fileMaster = FileMaster.builder()
                .fileName(fileName)
                .filePath(destinationFile.getParentFile().toString())
                .storageType(StorageType.LocalStorage)
                .contentType(file.getContentType())
                .fileExtension(fileExtension)
                .build();
        fileManagerRepository.save(fileMaster);

        return fileMaster.getUuid();
    }

    private File createAndReturnFile(String subDirectory, String fileName, String fileExtension) throws IOException {
        File directory = new File(fileUploadDirectory + File.separator + subDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return new File(directory, fileName + "." + fileExtension);
    }

    public File getFile(String uuid) throws Exception {
        Optional<FileMaster> optionalFileMaster = Optional.ofNullable(fileManagerRepository.findByUuid(uuid));

        if (optionalFileMaster.isPresent()) {
            FileMaster fileMaster = optionalFileMaster.get();
            Path filePath = Paths.get(fileMaster.getFilePath(), fileMaster.getFileName() + "." + fileMaster.getFileExtension());
            return filePath.toFile();
        } else {
            throw new Exception("File not found with UUID: " + uuid);
        }
    }
}
