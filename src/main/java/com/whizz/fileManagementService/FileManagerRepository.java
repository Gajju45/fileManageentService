package com.whizz.fileManagementService;

import com.whizz.fileManagementService.pojo.FileMaster;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileManagerRepository extends CrudRepository<FileMaster,Long> {

    FileMaster findByUuid(@Param("uuid") String uuid);
}
