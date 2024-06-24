package com.whizz.fileManagementService.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.whizz.fileManagementService.FileManagerRepository;
import com.whizz.fileManagementService.bean.BeanFileResponse;
import com.whizz.fileManagementService.pojo.FileMaster;
import com.whizz.fileManagementService.pojo.enums.StorageTypeEnum;
import com.whizz.fileManagementService.utils.ProjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

@Service
public class FileManagerService {

    @Autowired
    private FileManagerRepository fileManagerRepository;
    @Autowired
    private Cloudinary cloudinary;

    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    public BeanFileResponse uploadFileOnLocal(MultipartFile file) throws IOException {
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
        String fileUrl=destinationFile.getParentFile().toString()+File.separator+fileName+"."+fileExtension;

        // Save file metadata to the database
        BeanFileResponse beanFileResponse = saveInDatabase(fileName, fileUrl, StorageTypeEnum.LOCALSTORAGE, file.getContentType(), fileExtension);

        return beanFileResponse;
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
            Path filePath = Paths.get(fileMaster.getFileUrl(), fileMaster.getFileName() + "." + fileMaster.getFileExtension());
            return filePath.toFile();
        } else {
            throw new Exception("File not found with UUID: " + uuid);
        }
    }

    public BeanFileResponse uploadCloudinaryFile(MultipartFile file) throws IOException {
        String folderName = ProjectUtils.getFolderName(file.getContentType());
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", folderName));
        String fileExtension = ProjectUtils.getFileExtension(file.getOriginalFilename());
        BeanFileResponse beanFileResponse=saveInDatabase(uploadResult.get("display_name").toString(), uploadResult.get("url").toString(), StorageTypeEnum.CLOUDINARY, file.getContentType(), fileExtension);
        System.out.println(uploadResult);
        return beanFileResponse;

    }

    public BeanFileResponse saveInDatabase(String fileName, String destinationPath, StorageTypeEnum storageTypeEnum, String contentType, String fileExtension) {
        FileMaster fileMaster = FileMaster.builder()
                .fileName(fileName)
                .fileUrl(destinationPath)
                .storageType(storageTypeEnum)
                .contentType(contentType)
                .fileExtension(fileExtension)
                .build();
        fileManagerRepository.save(fileMaster);
       BeanFileResponse beanFileResponse= BeanFileResponse.builder()
                .uuid(fileMaster.getUuid())
                .imageUrl(fileMaster.getFileUrl()).build();

        return beanFileResponse;

    }

}
