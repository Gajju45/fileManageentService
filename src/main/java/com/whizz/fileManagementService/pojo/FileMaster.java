package com.whizz.fileManagementService.pojo;

import com.whizz.fileManagementService.pojo.enums.StorageTypeEnum;
import jakarta.persistence.*;
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
    @Column(name = "file_name")
    private String fileName;
    @Lob
    @Column(name = "file_url")
    private String fileUrl;
    @Column(name = "extension")
    private String fileExtension;
    @Column(name = "content_type")
    private String contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "storage_type")
    private StorageTypeEnum storageType;

}
