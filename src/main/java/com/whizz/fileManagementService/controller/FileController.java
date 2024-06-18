package com.whizz.fileManagementService.controller;

import com.whizz.fileManagementService.bean.ResponsePacket;
import com.whizz.fileManagementService.service.FileManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    FileManagerService fileManagerService;

    @GetMapping("/test")
    public ResponseEntity<ResponsePacket> test() {
        ResponsePacket responsePacket;
        responsePacket = new ResponsePacket(0, "Api Testing", null);
        return new ResponseEntity<>(responsePacket, HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponsePacket> singleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            System.out.println("Please Select File First.");
        }
        ResponsePacket responsePacket;
        String uuid = fileManagerService.uploadFileOnLocal(file);
        responsePacket = ResponsePacket.builder()
                .statusCode(0).message("file Uploaded  Successfully").data(uuid).build();
        return new ResponseEntity<>(responsePacket, HttpStatus.OK);

    }

    @GetMapping("/get/{uuid}")
    public ResponseEntity<Resource> getFileByUuid(@PathVariable("uuid") String uuid) {
        try {
            File file = fileManagerService.getFile(uuid);
            Path path = file.toPath();
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(path);

                return ResponseEntity.ok()
                        .contentType(org.springframework.http.MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

