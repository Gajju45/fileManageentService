package com.whizz.fileManagementService.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long recordId;

    @Basic(optional = false)
    @Column(name = "record_id", length = 40, unique = true, nullable = false)
    protected String uuid = UUID.randomUUID().toString();

    @Basic(optional = false)
    @Column(name = "active")
    protected boolean active = true;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt = LocalDateTime.now();

    @Basic(optional = false)
    @Column(name = "deleted")
    protected boolean deleted = false;
}

