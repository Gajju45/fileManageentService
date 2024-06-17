package com.whizz.fileManagementService.pojo;

import com.whizz.fileManagementService.pojo.enums.StorageType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "file_master")
public class FileMaster extends BaseEntity {
    private String fileName;
    private String filePath;
    private String fileType;
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

}
